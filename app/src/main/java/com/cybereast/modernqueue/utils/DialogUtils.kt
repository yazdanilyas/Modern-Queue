package com.cybereast.modernqueue.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.TextUtils
import androidx.appcompat.widget.AppCompatEditText
import com.cybereast.modernqueue.R
import com.cybereast.modernqueue.models.Session
import java.util.*


object DialogUtils {
    fun addSessionDialog(
        activity: Activity,
        buttonListener: DialogButtonListener,
    ) {
        var time: String? = null
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_session_layout, null)
        val builder = AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.new_session))
            .setCancelable(false)
            .setView(view)

        val startTimeEt = view.findViewById<AppCompatEditText>(R.id.startTimeEt)
        val endTimeEt = view.findViewById<AppCompatEditText>(R.id.endTimeEt)
        val noOfTokens = view.findViewById<AppCompatEditText>(R.id.noOfTokens)

        setDialogButtons(activity, builder, buttonListener)
        val alertDialog = builder.show()

        startTimeEt.setOnClickListener {
            time = timePicker(activity)
            AppUtils.showToast(activity, "time" + time)
            startTimeEt.setText(time)
        }

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (!TextUtils.isEmpty(startTimeEt.text.toString())
                && !TextUtils.isEmpty(endTimeEt.text.toString())
                &&
                !TextUtils.isEmpty(
                    noOfTokens.text.toString()
                )
            ) {
//                val session = Session(
//                    startTimeEt.text.toString(),
//                    endTimeEt.text.toString(),
//                    Integer.parseInt(noOfTokens.text.toString())
//                )

                alertDialog.dismiss()
            } else {
                AppUtils.showToast(activity, activity.getString(R.string.missing_fields_message))
            }
        }

    }

    private fun setDialogButtons(
        context: Context,
        builder: AlertDialog.Builder,
        buttonListener: DialogButtonListener,

        ) {
        builder.setPositiveButton(context.getString(R.string.add_str)) { dialog, id ->
        }
        builder.setNegativeButton(context.getString(R.string.cancel_str)) { dialog, id ->
            buttonListener.onNegativeClick()
            dialog.dismiss()
        }

    }

    fun timePicker(activity: Activity): String? {
        var time: String? = "10:00 AM"
        val c: Calendar = Calendar.getInstance()
        var mHour = c.get(Calendar.HOUR_OF_DAY)
        var mMinute = c.get(Calendar.MINUTE)

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            activity, { view, hourOfDay, minute ->
                time = setTime12Hour(hourOfDay, minute)
            },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
        return time
    }

    private fun setTime12Hour(hoursOfDay: Int, minute: Int): String {
        var time: String? = null
        var amPm: String? = null
        var hourOfDay = hoursOfDay
        var AM_PM = "AM"
        var minPrecede = ""
        if (hourOfDay >= 12) {
            AM_PM = "PM"
            if (hourOfDay >= 13 && hourOfDay < 24) {
                hourOfDay -= 12
            } else {
                hourOfDay = 12
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12
        }

        if (minute < 10) {
            minPrecede = "0"
        }
        time = "$hourOfDay:$minPrecede$minute $amPm"
        amPm = AM_PM
        return time
    }


    interface DialogButtonListener {
        fun onPositiveClick(session: Session)
        fun onNegativeClick()
    }

}