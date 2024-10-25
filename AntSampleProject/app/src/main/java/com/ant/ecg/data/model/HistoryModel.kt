package com.ant.ecg.data.model


/**
 * seq : 고유값
 * measurementTime : 측정시간
 * heartRate : 심장수
 * respiratoryRate : 호흡수
 * bodyTemperature : 체온
 * currentDate : 저장한 시간
 * **/
data class HistoryModel(val seq : Long , val measurementTime : String ,
                        val heartRate : String ,val respiratoryRate : String ,
                        val bodyTemperature : String , val currentDate : String ) {
}