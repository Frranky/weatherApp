package com.example.weatherapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AboutDialogFragment : DialogFragment() {

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		return activity?.let {
			val builder = AlertDialog.Builder(it)
			builder.setTitle("About")
				.setMessage("It's weather app dude.")
				.setPositiveButton("OK") { dialog, id ->
					dialog.cancel()
				}
			builder.create()
		} ?: throw IllegalStateException("Activity cannot be null")
	}
}