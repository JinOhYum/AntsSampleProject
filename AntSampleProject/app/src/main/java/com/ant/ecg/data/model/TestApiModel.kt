package com.ant.ecg.data.model

import com.google.gson.annotations.Expose

data class TestApiModel(
    @Expose val success: Boolean?,
    @Expose val data: TestApiDataModel?
)
data class TestApiDataModel(
    @Expose val id: Int?,
    @Expose val name: String?
)
