package com.kareemdev.themoviedb.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.kareemdev.themoviedb.data.BaseResponseObject
import com.kareemdev.themoviedb.data.BaseResult
import com.kareemdev.themoviedb.data.response.MovieResponse
import com.kareemdev.themoviedb.presentation.search.SearchPage

@Composable
fun HomePage(homeViewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val upcoming by homeViewModel.getUpComing.observeAsState()
    val nowPlaying by homeViewModel.getNowPlaying.observeAsState()
    val popular by homeViewModel.getPopular.observeAsState()

    var selectedMovie by remember { mutableStateOf<MovieResponse?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    var query by remember { mutableStateOf("") }
    var navigateSearch by remember { mutableStateOf(false)}
    var showSearchField by remember { mutableStateOf(false) }

    LaunchedEffect("") {
        homeViewModel.getUpComing()
        homeViewModel.getPopular()
        homeViewModel.getNowPlaying()
    }

    if (showDialog && selectedMovie != null) {
        nowPlaying?.let {
            if (it is BaseResult.Success) {
                val movies = it.data.results ?: emptyList()
                MovieDetailDialog(
                    movie = selectedMovie!!,
                    nowPlayingMovies = movies,
                    onDismissRequest = { showDialog = false }
                )
            }
        }
    }

    if(navigateSearch){
        SearchPage(homeViewModel = homeViewModel, onBack = {navigateSearch = false})
    }else {
        Scaffold(
            modifier = Modifier.systemBarsPadding().statusBarsPadding(),
            topBar = {
                TopAppBar(
                    title = {
                        if (showSearchField) {
                            CustomSearchField(
                                query = query,
                                onQueryChanged = { query = it },
                                onSearchClicked = {
                                    if (query.isNotEmpty()) {
                                        homeViewModel.getSearchMovie(query)
                                        navigateSearch = true
                                    } else {
                                        Toast.makeText(context, "Enter a search query", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onClearClicked = {
                                    query = ""
                                    showSearchField = false
                                },
                                Modifier.fillMaxWidth()
                            )
                        } else {
                            Text("Home")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (showSearchField && query.isNotEmpty()) {
                                homeViewModel.getSearchMovie(query)
                                navigateSearch = true
                            } else {
                                showSearchField = !showSearchField
                                if (!showSearchField) {
                                    query = ""
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Toggle Search"
                            )
                        }
                    },
                    elevation = 4.dp
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp
                        )

                ) {
                    //image slider
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        upcoming?.let {
                            if (it is BaseResult.Success) {
                                val movies = it.data.results ?: emptyList()
                                ImageSlider(movies)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column {
                            Text(text = "Now Playing")
                            when (nowPlaying) {
                                is BaseResult.Loading -> {
                                    Text(text = "Loading notes...")
                                }

                                is BaseResult.Success -> {
                                    val movies =
                                        (nowPlaying as BaseResult.Success<BaseResponseObject<List<MovieResponse>>>).data.results
                                            ?: emptyList()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        movies.forEach { movie ->
                                            NowPlayingCard(
                                                movie = movie,
                                                onClick = { selected ->
                                                    Log.d("TAG", "HomePage: selected")
                                                    selectedMovie = selected
                                                    showDialog = true
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                    }
                                }

                                is BaseResult.Failed -> {
                                    val errorMessage = (nowPlaying as BaseResult.Failed).error
                                    Text(text = "Error: $errorMessage")
                                }

                                is BaseResult.Idle -> {}
                                else -> {
                                    Text(text = "Loading user data..")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column {
                            Text(text = "Action")
                            when (popular) {
                                is BaseResult.Loading -> {
                                    Text(text = "Loading notes...")
                                }

                                is BaseResult.Success -> {
                                    val movies = (popular as BaseResult.Success<BaseResponseObject<List<MovieResponse>>>).data.results
                                            ?: emptyList()
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        movies.forEach { movie ->
                                            ActionCard(
                                                movie = movie,
                                                onClick = { selected ->
                                                    Log.d("TAG", "HomePage: selected")
                                                    selectedMovie = selected
                                                    showDialog = true
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                    }
                                }

                                is BaseResult.Failed -> {
                                    val errorMessage = (nowPlaying as BaseResult.Failed).error
                                    Text(text = "Error: $errorMessage")
                                }

                                is BaseResult.Idle -> {}
                                else -> {
                                    Text(text = "Loading user data..")
                                }
                            }
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun MovieDetailDialog(
    movie: MovieResponse,
    nowPlayingMovies: List<MovieResponse>,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .width(900.dp)
                .height(900.dp)
                .wrapContentSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Text(
                    text = movie.title ?: "No Title",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Image(
                    painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.backdropPath}"),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.overview ?: "No Overview",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Other Now Playing Movies")

                LazyRow {
                    items(nowPlayingMovies) { movieItem ->
                        NowPlayingCard(
                            movie = movieItem,
                            onClick = { /* Handle card click */ }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingCard(movie: MovieResponse, onClick: (MovieResponse) -> Unit) {
    Card(
        onClick = { onClick(movie) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            movie.title?.let {
                Text(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionCard(movie: MovieResponse, onClick: (MovieResponse) -> Unit) {
    Card(
        onClick = { onClick(movie) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            movie.title?.let {
                Text(
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
                    .height(220.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(movies: List<MovieResponse>) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = movies.size,
            state = pagerState,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) { page ->
            val movie = movies[page]
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.backdropPath}"),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun CustomSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = { newQuery -> onQueryChanged(newQuery) },
        placeholder = { Text("Search...") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClicked) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black,
            fontWeight = FontWeight.Normal
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color.Blue,
            placeholderColor = Color.Gray
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClicked() }
        )
    )
}
