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


@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: SplashApiRepository) :ViewModel(){


    /**
     * ViewModel 에서 사용되는 방식의 변수
     * MutableLiveData 은 데이터 수정이 가능한 변수이며 viewModel 내부에서 데이터 변경이 되어야 하면 ViewModel이 아닌 다른곳에서 데이터 변동을 방지 하기 위해 private
     * 외부에서 사용할경우 LiveData 는 데이터 수정이 불가능함
     **/
    private val _testApiResponse = MutableLiveData<TestApiModel>()
    val testApiResponse : LiveData<TestApiModel> get() = _testApiResponse

    private var _isApiSuccess : Boolean  = false
    val isApiSuccess : Boolean get() = _isApiSuccess

    //API 호출
    fun getTestApi(){
        viewModelScope.launch {
            val response = repository.getTestApi()

            when(response.isSuccessful){
                true ->{
                    Log.d("SplashViewModel" , "왔다 "+response.body())
                    _testApiResponse.value = response.body()
                    _isApiSuccess = true
                }
                false->{
                    Log.d("SplashViewModel" , "왔다 "+response.message())
                }
            }
        }
    }

}