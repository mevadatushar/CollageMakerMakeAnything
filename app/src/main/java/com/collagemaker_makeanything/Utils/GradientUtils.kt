package com.collagemaker_makeanything.Utils

import android.graphics.drawable.GradientDrawable

object GradientUtils {
    val gradients = listOf(
        // Earth Tones (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFA52A2A.toInt(), 0xFFDEB887.toInt(), 0xFFD2691E.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF8B4513.toInt(), 0xFFFFD700.toInt(), 0xFFDAA520.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFF5DEB3.toInt(), 0xFFA52A2A.toInt(), 0xFFDEB887.toInt())
        ),

        // Pastels (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFFFB6C1.toInt(), 0xFFFF69B4.toInt(), 0xFFE6E6FA.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFF0E68C.toInt(), 0xFFFFF0F5.toInt(), 0xFFD8BFD8.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFE0FFFF.toInt(), 0xFFF08080.toInt(), 0xFFE6E6FA.toInt())
        ),

        // Neutrals (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF808080.toInt(), 0xFFA9A9A9.toInt(), 0xFFD3D3D3.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFC0C0C0.toInt(), 0xFFF5F5F5.toInt(), 0xFF808080.toInt())
        ),

        // Basic Colors (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFFF0000.toInt(), 0xFFFFA500.toInt(), 0xFF00FF00.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF0000FF.toInt(), 0xFF8A2BE2.toInt(), 0xFF00FFFF.toInt())
        ),

        // Cool Tones (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF4682B4.toInt(), 0xFF87CEFA.toInt(), 0xFF1E90FF.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF0000CD.toInt(), 0xFF4169E1.toInt(), 0xFF1E90FF.toInt())
        ),

        // Greens (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF32CD32.toInt(), 0xFF3CB371.toInt(), 0xFF20B2AA.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF8FBC8F.toInt(), 0xFF2E8B57.toInt(), 0xFF006400.toInt())
        ),

        // Reds and Oranges (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFFF6347.toInt(), 0xFFFF4500.toInt(), 0xFFFF8C00.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFFFF7F50.toInt(), 0xFFFFA500.toInt(), 0xFFB22222.toInt())
        ),

        // Purples and Pinks (3-color gradient)
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF800080.toInt(), 0xFFDA70D6.toInt(), 0xFFA020F0.toInt())
        ),
        GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(0xFF9370DB.toInt(), 0xFFD8BFD8.toInt(), 0xFFFF1493.toInt())
        )
    )
}
