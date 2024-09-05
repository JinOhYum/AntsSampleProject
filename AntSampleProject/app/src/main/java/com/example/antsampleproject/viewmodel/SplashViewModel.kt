package com.example.antsampleproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.antsampleproject.data.model.TestApiModel
import com.example.antsampleproject.data.repostory.SplashApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Activity 와 동일하게 ViewModel 에서 DI 를 사용하기 위해 @HiltViewModel 어노테이션 지정
 * di/AppModule.kt 에 의존성 주입 모듈을 통해 SplashApiRepository 주입 받고있음
 * **/
@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashApiRepository) :ViewModel(){


    /**
     * ViewModel 에서 사용되는 방식의 변수
     * MutableLiveData 는 데이터 수정이 가능한 변수이며 viewModel 내부에서 데이터 변경이 되어야 하면 ViewModel이 아닌 다른곳에서 데이터 변동을 방지 하기 위해 private
     * 외부에서 변수를 사용할경우 LiveData 형의 변수를 사용
     * LiveData 는 데이터 수정이 불가능하며 최신 데이터 및 데이터 변동사항만 알수있음
     *
     **/

    private val _testApiResponse = MutableLiveData<TestApiModel>()
    val testApiResponse : LiveData<TestApiModel> get() = _testApiResponse


    private var _isApiSuccess : Boolean  = false
    val isApiSuccess : Boolean get() = _isApiSuccess

    /**
     * API 호출
     * **/
    fun getTestApi(){
        viewModelScope.launch {
            repository.getTestApi()
                .onStart {
                    /**
                     * Flow 시작 시 호출됨
                     * **/
                    Log.d("여기","onStart")
                }
                .catch {
                    /**
                     * 예외 발생 시 호출됨
                     * **/
                    Log.d("여기","catch"+it.message)
                }
                .onCompletion {
                    /**
                     * Flow가 완료되었을 때 호출됨 (성공, 실패 모두 포함)
                     * **/
                    Log.d("여기","onCompletion")
                    _isApiSuccess = true
                }
                .collectLatest {
                    /**
                     * API 에서 받은 최신데이터 수집
                     * collect 존재
                     * collect 와 collectLatest 차이점
                     *
                     * collect = 들어오는 데이터를 순차적으로 표시
                     * collectLatest = 마지막으로 들어온 데이터만 표시
                     * **/
                    Log.d("여기","collectLatest"+it.data)
                    _testApiResponse.value = it
                }
        }

    }

}