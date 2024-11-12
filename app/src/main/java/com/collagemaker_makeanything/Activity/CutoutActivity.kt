package com.collagemaker_makeanything.Activity

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.collagemaker_makeanything.R
import com.collagemaker_makeanything.databinding.ActivityCutoutBinding
import dev.eren.removebg.RemoveBg
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.InputStream

class CutoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCutoutBinding
    private lateinit var removeBg: RemoveBg
    private var originalBitmap: Bitmap? = null
    private var processedBitmap: Bitmap? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCutoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        removeBg = RemoveBg(this)

        val imageUriString = intent.getStringExtra("imageUri")
        imageUriString?.let {
            val imageUri = Uri.parse(it)
            originalBitmap = uriToBitmap(imageUri)

            originalBitmap?.let { bitmap ->
                // Set the original image immediately
                binding.imageView.setImageBitmap(bitmap)

                // Wait for the imageView to be laid out before processing
                binding.imageView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        processImage(bitmap) // Start processing after showing the original image
                    }
                })
            }
        }

        setupButtonClickListeners()
    }

    private fun setupButtonClickListeners() {
        binding.autoButton.setOnClickListener {
            changeButtonBackgrounds(isAuto = true)
            // Trigger processing if needed, but only once when the button is pressed
            originalBitmap?.let { processImage(it) }
        }
    }

    private fun changeButtonBackgrounds(isAuto: Boolean) {
        val blueBackground = ContextCompat.getDrawable(this, R.drawable.button_background_blue)
        val grayBackground = ContextCompat.getDrawable(this, R.drawable.button_background_gray)
        val whiteTextColor = ContextCompat.getColor(this, android.R.color.white)
        val blackTextColor = ContextCompat.getColor(this, android.R.color.black)

        if (isAuto) {
            binding.autoButton.background = blueBackground
            binding.autoText.setTextColor(whiteTextColor)
            binding.aiPreciseButton.background = grayBackground
            binding.aiPreciseText.setTextColor(blackTextColor)
        } else {
            binding.autoButton.background = grayBackground
            binding.autoText.setTextColor(blackTextColor)
            binding.aiPreciseButton.background = blueBackground
            binding.aiPreciseText.setTextColor(whiteTextColor)
        }
    }

    private fun processImage(bitmap: Bitmap) {
        lifecycleScope.launch {
            try {
                removeBg.clearBackground(bitmap).collect { result ->
                    result?.let {
                        processedBitmap = it // Store the processed bitmap
                        startRevealAnimation() // Start animation after processing is complete
                    }
                }
            } catch (e: Exception) {
                // Handle any errors
                binding.imageView.visibility = View.GONE
            }
        }
    }

    private fun startRevealAnimation() {
        // Set the processed bitmap to the ImageView immediately
        binding.imageView.setImageBitmap(processedBitmap)

        // Make the view visible for the animation
        binding.viewAutoAi.visibility = View.VISIBLE

        // Start the reveal animation for the view_auto_ai
        val viewHeight = binding.viewAutoAi.height.toFloat()

        // Use translationY to move the view down
        val animator = ObjectAnimator.ofFloat(binding.viewAutoAi, "translationY", -viewHeight, 0f).apply {
            duration = 2000 // Duration of the animation
            start()
        }
    }



    private fun uriToBitmap(uri: Uri): Bitmap? {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun onDestroy() {
        super.onDestroy()
        originalBitmap?.recycle()
        processedBitmap?.recycle()
    }
}
