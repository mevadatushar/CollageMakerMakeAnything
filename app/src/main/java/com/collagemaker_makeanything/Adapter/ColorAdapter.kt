package com.collagemaker_makeanything.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.collagemaker_makeanything.R


class ColorAdapter(
    private val colors: List<Int>,
    private val onColorSelected: (Int) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            // Store the previous selected position
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify the adapter to update the views
            notifyItemChanged(previousPosition) // Update previous item to return to normal
            notifyItemChanged(selectedPosition) // Update current item to lift up

            // Notify the listener about the selected color
            onColorSelected(colors[position])
        }
    }

    override fun getItemCount(): Int = colors.size

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorView: View = itemView.findViewById(R.id.color_view)

        fun bind(color: Int, isSelected: Boolean) {
            colorView.setBackgroundColor(color)

            // Animate the lifting effect
            val translationY = if (isSelected) -40f else 0f // Move up 40 pixels when selected
            colorView.animate().translationY(translationY).setDuration(200).start()

            // Adjust elevation based on selection
            colorView.elevation = if (isSelected) 8f else 0f // Lift effect
        }
    }
}


