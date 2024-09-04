package com.kareemdev.themoviedb.data.remote.retrofit

import com.kareemdev.themoviedb.data.BaseResponseObject
import com.kareemdev.themoviedb.data.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apikey: String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): BaseResponseObject<List<MovieResponse>>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apikey: String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): BaseResponseObject<List<MovieResponse>>

    @GET("movie/top_rated")
    suspend fun getPopular(
        @Query("api_key") apikey: String,
        @Query("language") language:String,
        @Query("page") page:Int
    ): BaseResponseObject<List<MovieResponse>>

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("api_key") apikey: String,
        @Query("query") query: String,
        @Query("include_adult") adult: Boolean,
        @Query("language") language:String,
        @Query("page") page:Int
    ): BaseResponseObject<List<MovieResponse>>

}