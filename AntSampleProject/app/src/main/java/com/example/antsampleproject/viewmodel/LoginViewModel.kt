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

    /**
     * ViewModel 에서 사용되는 방식의 변수
     * MutableLiveData 는 데이터 수정이 가능한 변수이며 viewModel 내부에서 데이터 변경이 되어야 하면 ViewModel이 아닌 다른곳에서 데이터 변동을 방지 하기 위해 private
     * 외부에서 변수를 사용할경우 LiveData 형의 변수를 사용
     * LiveData 는 데이터 수정이 불가능하며 최신 데이터 및 데이터 변동사항만 알수있음
     **/

    private var _isLoginCheck = MutableLiveData<Boolean>(false)
    val isLoginCheck : LiveData<Boolean> get() = _isLoginCheck

    private val TAG = "LoginViewModel"

    /**
     * 로그인 API 통신
     * **/
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