package com.example.antsampleproject.util

object DefineConfig {
    /**
     * IP 같은경우 고정 IP 가 아니기에
     * 서버 열때마다 IP 체크 필요
     * **/
    const val DOMAIN = "http://172.30.1.47:10001"
    const val DOMAIN_SOCKET = "http://172.30.1.47:8080"
    const val DOMAIN_SECOND_SOCKET = "http://172.30.1.47:8081"
}