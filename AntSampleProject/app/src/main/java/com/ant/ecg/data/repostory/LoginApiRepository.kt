package com.ant.ecg.data.repostory

import com.ant.ecg.api.ApiService
import com.ant.ecg.data.model.TestApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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