package com.cybereast.modernqueue.utils

import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("setCheckedStatus")
fun setCheckedStatus(switch: SwitchCompat, isChecked: Boolean) {
    switch.isChecked = isChecked


}