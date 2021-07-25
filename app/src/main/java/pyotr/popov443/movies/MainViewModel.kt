package pyotr.popov443.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pyotr.popov443.movies.data.Movie
import pyotr.popov443.movies.data.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var searchText = ""
    val exceptionHappened = MutableLiveData<Boolean>().apply {
        value = false
    }

    val movies = MutableLiveData<List<Movie>>().apply {
        viewModelScope.launch {
            try {
                value = Repository.getMovies(getApplication())
            } catch (e: Exception) {
                exceptionHappened.value = true
                this.cancel()
            }
        }
    }

    fun searchMovies(query: String? = null) {
        viewModelScope.launch {
            try {
                movies.value = Repository.getMovies(getApplication(), query)
            } catch (e: Exception) {
                exceptionHappened.value = true
                this.cancel()
            }
        }
    }

    val movieProgress get() = movies.value!!.size * 5

    val movieCount get() = movies.value!!.size

    fun updateMovie(movie: Movie) {
        val i = movies.value!!.indexOf(movie)
        if (i == -1) return
        movies.value!![i].favorite = movie.favorite

        viewModelScope.launch {
            if (movie.favorite)
                Repository.addToFavorites(getApplication(), movie)
            else
                Repository.removeFromFavorites(getApplication(), movie)
        }
    }

}