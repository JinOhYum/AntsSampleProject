package com.example.antsampleproject.data.repostory

import com.example.antsampleproject.api.ApiService
import com.example.antsampleproject.data.model.TestApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Repository 패턴
 * SplashActivity 에서 사용되는 API는 여기서 관리
 * **/

@Singleton
class LoginApiRepository @Inject constructor(private val apiService: ApiService){

    /**
     * 테스트 set API
     * **/
    fun setTestApi(id : String , pw : String) : Flow<TestApiModel> = flow{

        val response = apiService.setTestApi(id,pw)

        if(response.isSuccessful){
            emit(response.body()!!)
        }
        else{
            throw Exception("Error: ${response.message()}")
        }
    }
}