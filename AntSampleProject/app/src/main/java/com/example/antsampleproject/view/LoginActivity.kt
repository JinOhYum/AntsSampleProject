package com.example.antsampleproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.antsampleproject.R
import com.example.antsampleproject.databinding.ActivityLoginBinding
import com.example.antsampleproject.util.SnackBarOption
import com.example.antsampleproject.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * AndroidEntryPoint = Hilt 에서 제공되는 DI 함수로 Activity 위에 어노테이션으로 지정 해주면된다
 * **/
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val viewModel : LoginViewModel  by  viewModels<LoginViewModel>()
    private val snackBarOption = SnackBarOption()
    private lateinit var snackBar : Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)


        init()
        onObserve()
    }

    private fun init(){
        binding.btLogin.setOnClickListener {
            if(binding.etId.text.toString().replace(" ","") == "" || binding.etPw.text.toString().replace(" ","") == ""){
                snackBar = Snackbar.make(it , "아이디 , 비밀번호 입력해주세요" , Snackbar.LENGTH_SHORT)
                snackBar.setAction("확인") {

                }
                snackBarOption.setSnackBarOption(snackBar)
                if(!snackBar.isShown){
                    snackBar.show()
                }
            }
            else{
                val id = binding.etId.text.toString()
                val pw = binding.etPw.text.toString()
                viewModel.setIdPw(id,pw)
            }
        }
    }


    private fun onObserve(){
        viewModel.isLoginCheck.observe(this){data->
            if(data){
                val intent = Intent(this , MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}