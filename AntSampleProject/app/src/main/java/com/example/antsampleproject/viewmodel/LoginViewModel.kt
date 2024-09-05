package com.example.antsampleproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antsampleproject.data.repostory.LoginApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
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
            repository.setTestApi(id,pw)
                .catch {
                    /**
                     * 예외 발생 시 호출됨
                     * **/
                    Log.d("여기","catch"+it.message)
                    _isLoginCheck.value = false
                }
                .onCompletion {
                    /**
                     * Flow가 완료되었을 때 호출됨 (성공, 실패 모두 포함)
                     * **/
                    Log.d("여기","onCompletion")
                    _isLoginCheck.value = true
                }
                .collectLatest {
                    /**
                     * API 에서 받은 최신데이터 수집
                     * **/
                    Log.d("여기","collectLatest"+it.data)
                }
        }
    }
}