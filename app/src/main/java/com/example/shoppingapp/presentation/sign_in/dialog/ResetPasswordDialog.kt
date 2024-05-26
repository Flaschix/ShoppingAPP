package com.example.shoppingapp.presentation.sign_in.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.dialog_reset_password, null)

    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val edMail = view.findViewById<EditText>(R.id.edEmailToReset)
    val btnCancel = view.findViewById<Button>(R.id.btnCancelReset)
    val btnSend = view.findViewById<Button>(R.id.btnSendLink)

    btnSend.setOnClickListener {
        val email = edMail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
}