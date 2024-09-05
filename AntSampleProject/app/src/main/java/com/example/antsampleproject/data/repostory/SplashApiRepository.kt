package com.example.antsampleproject.data.repostory

import com.example.antsampleproject.api.ApiService
import com.example.antsampleproject.data.model.TestApiModel
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
     * **/
    suspend fun getTestApi() : Response<TestApiModel> {

        return apiService.getTestApi()
    }


}