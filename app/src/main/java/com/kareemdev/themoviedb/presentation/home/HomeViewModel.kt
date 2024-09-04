package com.kareemdev.themoviedb.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kareemdev.themoviedb.data.BaseResponseObject
import com.kareemdev.themoviedb.data.BaseResult
import com.kareemdev.themoviedb.data.response.MovieResponse
import com.kareemdev.themoviedb.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
):ViewModel(){

    private val _getSearchMovie = MutableLiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>>(BaseResult.Idle)
    val getSearchMovie: LiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>> get() = _getSearchMovie

    private val _getUpComing = MutableLiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>>(BaseResult.Idle)
    val getUpComing: LiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>> get() = _getUpComing

    private val _getNowPlaying = MutableLiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>>(BaseResult.Idle)
    val getNowPlaying: LiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>> get() = _getNowPlaying

    private val _getPopular = MutableLiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>>(BaseResult.Idle)
    val getPopular: LiveData<BaseResult<BaseResponseObject<List<MovieResponse>>>> get() = _getPopular


    fun getSearchMovie(query: String) {
        _getSearchMovie.value = BaseResult.Loading
        viewModelScope.launch {
            repository.getSearchMovie(query).collect { result ->
                _getSearchMovie.value = result
            }
        }
    }

    fun getUpComing(){
        _getUpComing.value = BaseResult.Loading
        viewModelScope.launch {
            repository.getUpcoming().collect{result ->
                _getUpComing.value = result
            }
        }
    }

    fun getNowPlaying(){
        _getNowPlaying.value = BaseResult.Loading
        viewModelScope.launch {
            repository.getNowPlaying().collect{result ->
                _getNowPlaying.value = result
            }
        }
    }

    fun getPopular(){
        _getPopular.value = BaseResult.Loading
        viewModelScope.launch {
            repository.getPopular().collect{result ->
                _getPopular.value = result
            }
        }
    }
}