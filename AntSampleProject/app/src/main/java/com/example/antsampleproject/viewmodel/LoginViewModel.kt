package com.example.antsampleproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antsampleproject.data.repostory.LoginApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginApiRepository) : ViewModel() {


    private var _isLoginCheck = MutableLiveData<Boolean>(false)
    val isLoginCheck : LiveData<Boolean> get() = _isLoginCheck

    private val TAG = "LoginViewModel"

    fun setIdPw(id : String , pw : String){
        viewModelScope.launch {
            val response = repository.setTestApi(id,pw)

            when(response.isSuccessful){
                true ->{
                    Log.d(TAG,"로그인 서버통신 성공")
                    _isLoginCheck.value = true
                }
                false ->{
                    Log.d(TAG,"로그인 서버통신 실패")
                    _isLoginCheck.value = false
                }

            }
        }
    }
}