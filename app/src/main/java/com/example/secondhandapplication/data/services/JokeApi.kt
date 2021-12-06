package com.example.secondhandapplication.data.services

import retrofit2.Call
import retrofit2.http.GET

interface JokeApi {
    @GET("joke/Programming?type=single")
    fun getRandomJoke(): Call<Joke>
}