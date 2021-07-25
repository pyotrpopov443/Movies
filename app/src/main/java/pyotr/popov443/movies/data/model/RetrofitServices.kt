package pyotr.popov443.movies.data.model

import pyotr.popov443.movies.data.MoviesRespond
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String): MoviesRespond

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String): MoviesRespond
}