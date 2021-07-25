package pyotr.popov443.movies.data

import android.content.Context
import pyotr.popov443.movies.data.localdb.Favorite
import pyotr.popov443.movies.data.localdb.FavoriteRoomDatabase
import pyotr.popov443.movies.data.model.Common

object Repository {

    private const val API_KEY = "6ccd72a2a8fc239b13f209408fc31c33"
    private const val LANGUAGE = "ru-RU"

    var mService = Common.retrofitService

    suspend fun getMovies(context: Context, query: String? = null) : List<Movie> {
        val respond = if (query == null)
            mService.discoverMovies(API_KEY, LANGUAGE)
        else
            mService.searchMovies(API_KEY, LANGUAGE, query)

        return mutableListOf<Movie>().apply {
            val favorites = FavoriteRoomDatabase.getDatabase(context).favoriteDao().getFavorites()
            respond.results.forEach {
                add(Movie(it.id, it.poster_path, it.title, it.overview, dateToString(it.release_date),
                    favorites.indexOfFirst { f -> f.id == it.id } >= 0 ))
            }
        }
    }

    suspend fun addToFavorites(context: Context, movie: Movie) {
        FavoriteRoomDatabase.getDatabase(context).favoriteDao().insert(Favorite(movie.id))
    }

    suspend fun removeFromFavorites(context: Context, movie: Movie) {
        FavoriteRoomDatabase.getDatabase(context).favoriteDao().delete(movie.id)
    }

    private fun dateToString(date: String? = null) : String {
        if (date == null || date.length != 10) return "Дата неизвестна"
        return date.substring(8, 10).toInt().toString() +
                " " + month(date.substring(5, 7)) + " " + date.substring(0, 4)
    }

    private fun month(month: String) = when(month.toInt()) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> "месяца"
    }

}