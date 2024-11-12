package com.collagemaker_makeanything.Activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.canhub.cropper.CropImageView
import com.collagemaker_makeanything.Adapter.ColorAdapter
import com.collagemaker_makeanything.R
import com.collagemaker_makeanything.Utils.ColorUtils
import com.collagemaker_makeanything.databinding.ActivityEditBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditActivity : BaseActivity() {
    lateinit var binding: ActivityEditBinding
    private lateinit var imageUri: String
    private var croppedImageUri: String? = null
    private lateinit var containerEdit: LinearLayout
    private lateinit var imageBitmap: Bitmap


    val colorList = ColorUtils.colors


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive the data from the previous activity
        imageUri = intent.getStringExtra("imageUri") ?: ""
        croppedImageUri = intent.getStringExtra("croppedImageUri")
        containerEdit = findViewById(R.id.container_edit)

        initView()
    }

    private fun initView() {
        loadSelectedImage()

        with(binding) {

            imgBack.setOnClickListener {
                onBackPressed()
            }

            rlCrop.setOnClickListener {
                val intent = Intent(this@EditActivity, CropActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE)
            }

            rlFrame.setOnClickListener {
                val intent = Intent(this@EditActivity, FrameActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE)
            }

//            rlFilter.setOnClickListener {
//                val intent = Intent(this@EditActivity , FilterActivity::class.java)
//                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
//                startActivityForResult(intent, CROP_IMAGE_REQUEST_CODE)
//            }

            rlFilter.setOnClickListener {
                val intent = Intent(this@EditActivity, FilterActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }

            rlSticker.setOnClickListener {
                val intent = Intent(this@EditActivity, StickerActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }

            txtRatio.setOnClickListener {
                showRatioBottomSheet()
            }

            binding.txtBackground.setOnClickListener {
               val intent = Intent(this@EditActivity, BackgroundActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
                // for bottom to top animation
                 overridePendingTransition(R.anim.slide_out_bottom,R.anim.slide_out_top);
            }

            binding.rlDraw.setOnClickListener {
                val intent = Intent(this@EditActivity, DrawActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }

            binding.rlSave.setOnClickListener {
                val intent = Intent(this@EditActivity, SaveActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }

            binding.rlEnhance.setOnClickListener {
                val intent = Intent(this@EditActivity, EnhanceActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }

            binding.rlCutout.setOnClickListener {
                val intent = Intent(this@EditActivity, CutoutActivity::class.java)
                intent.putExtra("imageUri", croppedImageUri ?: imageUri)
                startActivityForResult(intent, FILTER_IMAGE_REQUEST_CODE)
            }


        }
    }

    private fun showBackgroundDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.dialog_background, null)

        val imgBack: ImageView = bottomSheetView.findViewById(R.id.imgBack)
        val llColor: LinearLayout = bottomSheetView.findViewById(R.id.llColor)
        val imgDone: ImageView = bottomSheetView.findViewById(R.id.imgDone)
        val imgBackgroundImage: ImageView = bottomSheetView.findViewById(R.id.imgBackgroundImage)
        val ll_image_container: LinearLayout = bottomSheetView.findViewById(R.id.ll_image_container)

        // Load the image using Glide if imageUri is available
        if (imageUri.isNotEmpty()) {
            val uri = Uri.parse(imageUri)
            try {
                Glide.with(this@EditActivity).asBitmap().load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            imgBackgroundImage.setImageBitmap(resource)
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

        // Dismiss bottom sheet on imgBack click
        imgBack.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Save cropped image on imgDone click
        imgDone.setOnClickListener {
            loadSelectedImage()
            bottomSheetDialog.dismiss()
        }

        llColor.setOnClickListener {
            // Inflate the custom color dialog layout
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_color_selection, null)
            val recyclerViewColors = dialogView.findViewById<RecyclerView>(R.id.recyclerViewColors)
            // Show the color selection dialog
            val colorDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()


            // Set up the RecyclerView with ColorAdapter
            recyclerViewColors.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val colorAdapter = ColorAdapter(colorList) { selectedColor ->
                // Apply the selected color as the background of ll_image_container
                ll_image_container.setBackgroundColor(selectedColor)

                // Dismiss the color dialog after selecting a color
                colorDialog.dismiss() // Dismiss the color dialog
            }
            recyclerViewColors.adapter = colorAdapter



            // Set the dialog to full screen
            colorDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            colorDialog.show()
        }

        // Set the custom view to the bottom sheet dialog and handle its behavior
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
                behavior.isDraggable = false
            }
        }

        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.show()
    }



    private fun showRatioBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(R.layout.dialog_ratio, null)

        val imgBack: ImageView = bottomSheetView.findViewById(R.id.imgBack)
        val imgDone: ImageView =
            bottomSheetView.findViewById(R.id.imgDone) // Add this for saving the image
        val imgCropSelectImage: CropImageView =
            bottomSheetView.findViewById(R.id.imgCropSelectImage)


        // Set up listeners for aspect ratio options (as you already have)
        bottomSheetView.findViewById<LinearLayout>(R.id.lnrCustom).setOnClickListener {
            imgCropSelectImage.setFixedAspectRatio(false)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrLG11).setOnClickListener {
            imgCropSelectImage.setAspectRatio(1, 1)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrLG45).setOnClickListener {
            imgCropSelectImage.setAspectRatio(4, 5)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img54).setOnClickListener {
            imgCropSelectImage.setAspectRatio(5, 4)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img34).setOnClickListener {
            imgCropSelectImage.setAspectRatio(3, 4)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img43).setOnClickListener {
            imgCropSelectImage.setAspectRatio(4, 3)
        }

        bottomSheetView.findViewById<ImageView>(R.id.imgA4).setOnClickListener {
            imgCropSelectImage.setAspectRatio(2, 3)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrCover).setOnClickListener {
            imgCropSelectImage.setAspectRatio(16, 9)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrDevice).setOnClickListener {
            imgCropSelectImage.setAspectRatio(9, 16)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img23).setOnClickListener {
            imgCropSelectImage.setAspectRatio(2, 3)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img32).setOnClickListener {
            imgCropSelectImage.setAspectRatio(3, 2)
        }

        bottomSheetView.findViewById<ImageView>(R.id.img12).setOnClickListener {
            imgCropSelectImage.setAspectRatio(1, 2)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrPost).setOnClickListener {
            imgCropSelectImage.setAspectRatio(16, 9)
        }

        bottomSheetView.findViewById<LinearLayout>(R.id.lnrHeader).setOnClickListener {
            imgCropSelectImage.setAspectRatio(3, 1)
        }


        // Load the image using Glide if imageUri is available
        if (imageUri.isNotEmpty()) {
            val uri = Uri.parse(imageUri)
            try {
                Glide.with(this@EditActivity).asBitmap().load(uri)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            imgCropSelectImage.setImageBitmap(resource)
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

        // Dismiss bottom sheet on imgBack click
        imgBack.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Save cropped image on imgDone click
        imgDone.setOnClickListener {
            // Get the cropped image from the CropImageView
            val croppedBitmap = imgCropSelectImage.croppedImage

            // Save the cropped image as a Uri or file
            croppedImageUri = croppedBitmap?.let { it1 -> saveCroppedImage(it1) }

            // Load the cropped image into the main ImageView in EditActivity
            loadSelectedImage()

            // Dismiss the dialog
            bottomSheetDialog.dismiss()
        }

        // Set the custom view to the dialog and handle its behavior
        bottomSheetDialog.setContentView(bottomSheetView)
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
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        bottomSheetDialog.show()
    }

    private fun saveCroppedImage(bitmap: Bitmap): String {
        // Create a unique filename using the current timestamp
        val filename = "cropped_image_${System.currentTimeMillis()}.png"

        // Get the cache directory to save the image (this directory is private and not scanned by the gallery)
        val directory = cacheDir

        // Create the file in the cache directory
        val file = File(directory, filename)

        // Try to save the bitmap to this file
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the file's URI as a string (which you will use to load the image later)
        return Uri.fromFile(file).toString()
    }


    private fun loadSelectedImage() {
        val uriToLoad = croppedImageUri ?: imageUri
        if (uriToLoad.isNotEmpty()) {
            Log.d("EditActivity", "Loading image URI: $uriToLoad")
            Glide.with(this)
                .load(uriToLoad)
                .into(binding.collageContainer)
        } else {
            Log.d("EditActivity", "No image URI found.")
            Toast.makeText(this, "No image found to load", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CROP_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            croppedImageUri = data?.getStringExtra("croppedImageUri")
            loadSelectedImage()
        } else if (requestCode == FILTER_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            croppedImageUri = data?.getStringExtra("filteredImageUri")
            loadSelectedImage()
        }
    }

    private fun logErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish() // Close the activity or handle it appropriately
    }


    companion object {
        private const val CROP_IMAGE_REQUEST_CODE = 1001
        private const val FILTER_IMAGE_REQUEST_CODE = 1002

    }
}
