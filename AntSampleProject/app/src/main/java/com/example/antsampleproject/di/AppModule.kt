package com.example.antsampleproject.di

import com.example.antsampleproject.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 에 핵심 Class 의존성 주입 모듈
 * Hilt 는 사용전 의존성 주입 관련 학습 필요
 * @Singleton 이 붙어있는 함수들은 앱이 실행이 되면 수명주기동안 싱글톤으로 유지가 되며 의존성 주입을 통해 객체를 공유하고 재사용 가능
 *
 * 예시) Retrofit , Gride ,SharedPreferences 등 싱글톤 형태로 액티비티 전역에서 사용할것들 여기서 처리
 * **/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //주입성을 통해 레트로핏을 싱글톤으로 만들어서 사용
    @Singleton
    @Provides
    fun provideServerApi(): ApiService {
        return ApiService.getInstance()
    }
}