package com.example.antsampleproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antsampleproject.data.model.TestApiModel
import com.example.antsampleproject.data.repostory.SplashApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
     **/
    private val _testApiResponse = MutableLiveData<TestApiModel>()
    val testApiResponse : LiveData<TestApiModel> get() = _testApiResponse


    private var _isApiSuccess : Boolean  = false
    val isApiSuccess : Boolean get() = _isApiSuccess

    /**
     * API 호출
     * **/
    fun getTestApi(){
        /**
         * viewModelScope 는 Kotlin 에서 제공하는 Coroutine 의 일종
         * 비동기 처리를 위한 쓰레드
         * **/
        viewModelScope.launch {

            val response = repository.getTestApi()

            when(response.isSuccessful){
                true ->{
                    Log.d("SplashViewModel" , "서버통신 성공 "+response.body())
                    /**
                     * MutableLiveData 데이터를 변경할때 2가지 형태가있음
                     * value , postValue
                     * value = 메인 쓰레드에서 값을 변경 하는 형태
                     * postValue = 백그라운드 쓰레드에서 값이 변경되는 형태
                     * **/
                    _testApiResponse.value = response.body()

                    _isApiSuccess = true
                }
                false->{
                    Log.d("SplashViewModel" , "서버통신 실패 "+response.message())
                }
            }
        }
    }

}