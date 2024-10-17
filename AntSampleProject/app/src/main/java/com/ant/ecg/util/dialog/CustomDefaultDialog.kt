package com.ant.ecg.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.ant.ecg.databinding.CustomDefaultDialogLayoutBinding

class CustomDefaultDialog (activity: Context, private val title : String, private val content : String , private val leftButtonText : String , private val rightButtonText : String)  : Dialog(activity) {

    private lateinit var binding : CustomDefaultDialogLayoutBinding

    private lateinit var customDialogListener : CustomDialogListener


    interface CustomDialogListener{
        fun onCheckClick()

        fun onNoClick()
    }

    fun setDialogListener(customDialogListener : CustomDialogListener){
        this.customDialogListener = customDialogListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomDefaultDialogLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setCancelable(false)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f
        window!!.attributes = lpWindow
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (context.resources.displayMetrics.widthPixels * 0.80).toInt()

        window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)


        init()
    }

    private fun init(){
        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.tvOk.text = rightButtonText
        binding.tvNo.text = leftButtonText


        binding.tvOk.setOnClickListener {
            customDialogListener.onCheckClick()
            dismiss()
        }

        binding.tvNo.setOnClickListener {
            customDialogListener.onNoClick()
            cancel()
        }
    }
}