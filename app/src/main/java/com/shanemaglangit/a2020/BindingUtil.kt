package com.shanemaglangit.a2020

import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("android:text")
fun setText(view: TextView, value: Int) {
    view.text = value.toString()
}

@BindingAdapter("android:text")
fun setText(view: TextView, value: Double) {
    when {
        value >= 8 -> view.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.ratingWonderful
            )
        )
        value >= 6 -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingNice))
        value >= 4 -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingGood))
        value >= 2 -> view.setTextColor(ContextCompat.getColor(view.context, R.color.ratingBad))
        value >= 0 -> view.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.ratingHorrible
            )
        )
    }
    view.text = String.format("%.2f", value)
}

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

@BindingAdapter("android:progress")
fun setProgress(view: SeekBar, value: Int) {
    if(view.progress != value / 5) {
        view.progress = value / 5
    }
}

@InverseBindingAdapter(attribute = "android:progress")
fun getProgress(view: SeekBar): Int? {
    return view.progress * 5
}