package com.ant.ecg.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtil {

    fun getYearMonthList(): List<String> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
        val startDate = LocalDate.of(2024, 1, 1)
        val toDay = LocalDate.now()
//        val endDate = LocalDate.of(2050, 12, 31)
        val yearMonthList = mutableListOf<String>()

        var currentDate = startDate
        while (currentDate.isBefore(toDay) || currentDate.isEqual(toDay)) {
            yearMonthList.add(currentDate.format(formatter))
            currentDate = currentDate.plusMonths(1)
        }

        return yearMonthList
    }

}