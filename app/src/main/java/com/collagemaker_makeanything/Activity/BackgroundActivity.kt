package com.collagemaker_makeanything.Activity

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.collagemaker_makeanything.Adapter.ColorAdapter
import com.collagemaker_makeanything.Adapter.GradientAdapter
import com.collagemaker_makeanything.R
import com.collagemaker_makeanything.Utils.ColorUtils
import com.collagemaker_makeanything.Utils.GradientUtils
import com.collagemaker_makeanything.databinding.ActivityBackgroundBinding
import com.collagemaker_makeanything.databinding.DialogColorSelectionBinding
import com.collagemaker_makeanything.databinding.DialogGradientSelectionBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BackgroundActivity : BaseActivity() {

    lateinit var binding: ActivityBackgroundBinding
    private lateinit var imageUri: String
    private var croppedImageUri: String? = null
    private lateinit var imageBitmap: Bitmap
    val colorList = ColorUtils.colors
    val gradientList = GradientUtils.gradients
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // initView()
        setupUI()
        loadImageFromUri()
        // Receive the data from the previous activity
        imageUri = intent.getStringExtra("imageUri") ?: ""
        croppedImageUri = intent.getStringExtra("croppedImageUri")



    }

    private fun setupUI() {
        // Set click listeners
        binding.imgBack.setOnClickListener {
            // Handle back button click
            finish()
        }

        binding.imgDone.setOnClickListener {
            // Save cropped image logic here
            loadSelectedImage()
        }

        binding.llColor.setOnClickListener {
            showColorSelectionBottomSheet()
        }


        binding.llGradient.setOnClickListener {
            showGradientSelectionBottomSheet()
        }

        binding.llWhite.setOnClickListener {

            binding.llImageContainer.setBackgroundColor(Color.WHITE)

        }

        binding.llPng.setOnClickListener {
            binding.llImageContainer.setBackgroundResource(R.drawable.png)
        }





    }

    private fun loadImageFromUri() {

        imageUri = intent.getStringExtra("imageUri") ?: ""

        if (imageUri.isNotEmpty()) {
            val uri = Uri.parse(imageUri)
            try {
                Glide.with(this).asBitmap().load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            binding.imgBackgroundImage.setImageBitmap(resource)
                            imageBitmap = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            } catch (e: Exception) {
                logErrorAndFinish("Glide error: ${e.message}")
            }
        } else {
            logErrorAndFinish("Image URI string is null")
        }
    }



    private fun logErrorAndFinish(message: String) {
        Log.e("BackgroundActivity", message)
        finish() // Finish the activity on error
    }


    private fun showColorSelectionBottomSheet() {
        // Inflate the layout binding
        val bottomSheetBinding = DialogColorSelectionBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)

        // Set the custom view to the dialog
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        // Customize the background of the bottom sheet
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Handle the bottom sheet behavior
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                // Disable dragging to prevent moving the bottom sheet
                behavior.isDraggable = false


            }
        }

        // Set the dialog to be non-cancelable
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        // Set up the RecyclerView with the color adapter
        bottomSheetBinding.recyclerViewColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter = ColorAdapter(colorList) { selectedColor ->
            binding.llImageContainer.setBackgroundColor(selectedColor)
        }
        bottomSheetBinding.recyclerViewColors.adapter = colorAdapter

        // Set up click listeners for the back and done buttons
        bottomSheetBinding.imgBack.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.imgDone.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }


    private fun showGradientSelectionBottomSheet() {
        // Inflate the layout binding
        val bottomSheetBinding = DialogGradientSelectionBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(this)

        // Set the custom view to the dialog
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        // Customize the background of the bottom sheet
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Handle the bottom sheet behavior
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true

                // Disable dragging to prevent moving the bottom sheet
                behavior.isDraggable = false
            }
        }

        // Set the dialog to be non-cancelable
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        // Initialize the RecyclerView with GradientAdapter
        bottomSheetBinding.recyclerViewColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val gradientAdapter = GradientAdapter(GradientUtils.gradients) { selectedGradient ->
            binding.llImageContainer.background = selectedGradient // Set gradient background
        }
        bottomSheetBinding.recyclerViewColors.adapter = gradientAdapter

        // Set up click listeners for the back and done buttons
        bottomSheetBinding.imgBack.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.imgDone.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }

    private fun loadSelectedImage() {
        val uriToLoad = croppedImageUri ?: imageUri
        if (uriToLoad.isNotEmpty()) {
            Log.d("EditActivity", "Loading image URI: $uriToLoad")
            Glide.with(this)
                .load(uriToLoad)
                .into(binding.imgBackgroundImage)
        } else {
            Log.d("EditActivity", "No image URI found.")
            Toast.makeText(this, "No image found to load", Toast.LENGTH_SHORT).show()
        }
    }

}