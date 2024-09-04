package com.kareemdev.themoviedb.presentation.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.kareemdev.themoviedb.data.BaseResponseObject
import com.kareemdev.themoviedb.data.BaseResult
import com.kareemdev.themoviedb.data.response.MovieResponse
import com.kareemdev.themoviedb.presentation.home.HomeViewModel

@Composable
fun SearchPage(
    onBack: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val searchResult by homeViewModel.getSearchMovie.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Results") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (searchResult) {
                    is BaseResult.Loading -> {
//                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is BaseResult.Success -> {
                        val movies = (searchResult as BaseResult.Success<BaseResponseObject<List<MovieResponse>>>).data.results
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(movies ?: emptyList()) { movie ->
                                MovieCard(movie = movie, onClick = { /* Handle click */ })
                            }
                        }
                    }

                    is BaseResult.Failed -> {
                        val errorMessage = (searchResult as BaseResult.Failed).error.message
                        Text(
                            text = "Error: $errorMessage",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {}
                }
            }
        },
        modifier = Modifier.statusBarsPadding(),
        )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieCard(movie: MovieResponse, onClick: (MovieResponse) -> Unit) {
    Card(
        onClick = { onClick(movie) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            movie.title?.let {
                androidx.compose.material3.Text(
                    text = it,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.backdropPath}"),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}