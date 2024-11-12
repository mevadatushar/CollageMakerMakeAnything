package com.collagemaker_makeanything.Adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.collagemaker_makeanything.R

class GradientAdapter(
    private val gradients: List<GradientDrawable>,
    private val onGradientSelected: (GradientDrawable) -> Unit
) : RecyclerView.Adapter<GradientAdapter.GradientViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gradientr, parent, false)
        return GradientViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradientViewHolder, position: Int) {
        holder.bind(gradients[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            // Store the previous selected position
            val previousPosition = selectedPosition
            selectedPosition = position

            // Notify the adapter to update the views
            notifyItemChanged(previousPosition) // Update previous item to return to normal
            notifyItemChanged(selectedPosition) // Update current item to lift up

            // Notify the listener about the selected gradient
            onGradientSelected(gradients[position])
        }
    }

    override fun getItemCount(): Int = gradients.size

    inner class GradientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gradientView: View = itemView.findViewById(R.id.color_view)

        fun bind(gradient: GradientDrawable, isSelected: Boolean) {
            // Set the gradient as the background
            gradientView.background = gradient

            // Animate the lifting effect
            val translationY = if (isSelected) -40f else 0f // Move up 40 pixels when selected
            gradientView.animate().translationY(translationY).setDuration(200).start()

            // Adjust elevation based on selection
            gradientView.elevation = if (isSelected) 0f else 0f // Lift effect
        }
    }
}
