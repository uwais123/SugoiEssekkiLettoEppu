package com.masuwes.notesapp

import android.view.View

class CustomOnClickListener (private val position: Int, private val onItemClickCallback: OnItemClickCallback) : View.OnClickListener {

    override fun onClick(view: View) {
        onItemClickCallback.onItemClicked(view, position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}