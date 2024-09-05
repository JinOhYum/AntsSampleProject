package com.example.antsampleproject.data.repostory

import com.example.antsampleproject.api.ApiService
import com.example.antsampleproject.data.model.TestApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository 패턴
 * SplashActivity 에서 사용되는 API는 여기서 관리
 * @Singleton 어노테이션을 통해 싱글톤을 사용하며 ApiService 를 의존성 주입 받고있음
 * **/
@Singleton
class SplashApiRepository @Inject constructor(private val apiService: ApiService) {


    /**
     * 테스트 get API
     * Retrofit2 + Flow 를 이용한 방식
     * Flow = RxJava 와 동일
     * kotlin 에서 사용하는 타입으로
     * 비동기 작업을 할때 성능적 보장이 된다
     * **/
    fun getTestApi() : Flow<TestApiModel> = flow {
        val response = apiService.getTestApi()
        if(response.isSuccessful){
            emit(response.body()!!)
        }
        else{
            throw Exception("Error: ${response.message()}")
        }
    }

}