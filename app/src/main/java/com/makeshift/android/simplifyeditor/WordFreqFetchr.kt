package com.makeshift.android.simplifyeditor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.makeshift.android.simplifyeditor.api.ResultResponse
import com.makeshift.android.simplifyeditor.api.WordFreqApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WordFreqFetchr"
class WordFreqFetchr {

    private val wordFreqApi: WordFreqApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.19:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        wordFreqApi= retrofit.create(WordFreqApi::class.java)
    }

    fun fetchWords(word: String): LiveData<List<Term>> {
        val responseLiveData: MutableLiveData<List<Term>> = MutableLiveData()

        val wordFreqRequest: Call<ResultResponse> =
            wordFreqApi.fetchWords(word)

        wordFreqRequest.enqueue(object: Callback<ResultResponse> {
            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch word", t)
            }

            override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                val resultResponse: ResultResponse? = response.body()
                //val wordFreqResponse: WordFreqResponse? = resultResponse?.result
                var terms: List<Term> = resultResponse?.results
                    ?: mutableListOf()
                responseLiveData.value = terms
            }

        })

        return responseLiveData
    }

}