package com.shanemaglangit.a2020

import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

/**
 * Used to set TextView text using an int value
 * @param view the view to be modified
 * @param value the new value
 */
@BindingAdapter("android:text")
fun setText(view: TextView, value: Int) {
    view.text = value.toString()
}


/**
 * Used to set the TextView for the rating
 * @param view the view to be modified
 * @param value the new value
 */
@BindingAdapter("setRatingText")
fun setRatingText(view: TextView, value: String) {
    // Change the text color based on the value
    when (value.toFloat()) {
        in 8F..10F -> view.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.ratingWonderful
            )
        )
        in 60F..80F -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingNice))
        in 40F..60F -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingGood))
        in 20F..40F -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingBad))
        in 00F..20F -> view.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.ratingHorrible
            )
        )
    }

    view.text = String.format("%s%%", value)
}

/**
 * Used to set the Button text and color
 * @param view the view to be modified
 * @param value the new value
 */
@BindingAdapter("android:text")
fun setText(view: Button, value: Boolean) {
    when(value) {
        true -> {
            view.text = view.resources.getString(R.string.button_stop)
            view.setTextColor(ContextCompat.getColor(view.context, R.color.primaryColor))
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.secondaryColor))
        }
        false -> {
            view.text = view.resources.getString(R.string.button_start)
            view.setTextColor(ContextCompat.getColor(view.context, R.color.secondaryColor))
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.primaryColor))
        }
    }
}

/**
 * Used to set the progress of the SeekBar
 * @param view the view to be modified
 * @param value the new value
 */
@BindingAdapter("android:progress")
fun setProgress(view: SeekBar, value: Int) {
    if(view.progress != value / 5) {
        view.progress = value / 5
    }
}

/**
 * Used to get the progress of the SeekBar
 * @param view the view containing the value
 */
@InverseBindingAdapter(attribute = "android:progress")
fun getProgress(view: SeekBar): Int? {
    return view.progress * 5
}