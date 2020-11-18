package com.makeshift.android.simplifyeditor.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WordFreqApi {

    @GET("/api/v1/resources/words")
    fun fetchWords(@Query("word") word:String): Call<ResultResponse>


}