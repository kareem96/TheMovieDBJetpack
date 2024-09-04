package com.kareemdev.themoviedb.repositories

import com.kareemdev.themoviedb.BuildConfig.API_KEY
import com.kareemdev.themoviedb.data.BaseResponseObject
import com.kareemdev.themoviedb.data.BaseResult
import com.kareemdev.themoviedb.data.remote.retrofit.ApiService
import com.kareemdev.themoviedb.data.response.MovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: ApiService
){
    private val language = "en-US"
    private val adult: Boolean = false
    private val page: Int = 1

    fun getSearchMovie(query:String): Flow<BaseResult<BaseResponseObject<List<MovieResponse>>>> = flow {
        try {
            val response = apiService.getSearchMovie(API_KEY, query, adult, language, page)
            emit(BaseResult.Success(response))
        }catch (e: Exception){
            emit(BaseResult.Failed(e))
        }
    }

    fun getUpcoming(): Flow<BaseResult<BaseResponseObject<List<MovieResponse>>>> = flow {
        try {
            val response = apiService.getUpcoming(API_KEY, language, page)
            emit(BaseResult.Success(response))
        }catch (e: Exception){
            emit(BaseResult.Failed(e))
        }
    }
    fun getNowPlaying(): Flow<BaseResult<BaseResponseObject<List<MovieResponse>>>> = flow {
        try {
            val response = apiService.getNowPlaying(API_KEY, language, page)
            emit(BaseResult.Success(response))
        }catch (e: Exception){
            emit(BaseResult.Failed(e))
        }
    }

    fun getPopular(): Flow<BaseResult<BaseResponseObject<List<MovieResponse>>>> = flow {
        try {
            val response = apiService.getPopular(API_KEY, language, page)
            emit(BaseResult.Success(response))
        }catch (e: Exception){
            emit(BaseResult.Failed(e))
        }
    }
}