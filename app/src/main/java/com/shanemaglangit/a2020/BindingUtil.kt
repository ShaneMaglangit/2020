package com.shanemaglangit.a2020

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("android:text")
fun setText(view: EditText, value: Int) {
    if(value.toString() != view.text.toString()) view.setText(value.toString())
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: EditText): Int? {
    return view.text.toString().toIntOrNull()
}