package pyotr.popov443.movies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import pyotr.popov443.movies.data.Movie
import pyotr.popov443.movies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), RecyclerMoviesAdapter.MovieAdapterCallback {

    private val mainViewModel: MainViewModel by viewModels()

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.pullToRefresh.setOnRefreshListener {
            loadMovies()
            binding.pullToRefresh.isRefreshing = false
        }

        binding.inputSearch.addTextChangedListener {
            updateBtnClear()
            searchMovies()
        }

        binding.btnSearchClear.setOnClickListener {
            binding.inputSearch.text.clear()
        }

        val adapter = RecyclerMoviesAdapter(listOf(), this)
        binding.recyclerMovies.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerMovies.adapter = adapter

        mainViewModel.movies.observe(this, {
            updateProgressBar()
            updateErrorLayout()
            adapter.setMovieList(mainViewModel.movies.value ?: listOf())
            binding.loadMovies.visibility = View.INVISIBLE
        })

        mainViewModel.exceptionHappened.observe(this, {
            if (mainViewModel.exceptionHappened.value == true)
                showErrorSnackBar()
        })
    }

    private fun updateProgressBar() {
        val progress = mainViewModel.movieProgress
        if (progress == 0 || progress == 100)
            binding.progressHorizontal.visibility = View.INVISIBLE
        else
            binding.progressHorizontal.visibility = View.VISIBLE
        binding.progressHorizontal.progress = progress
    }

    private fun updateErrorLayout() {
        if (mainViewModel.movieCount == 0) {
            binding.textError.text = "По запросу \"${mainViewModel.searchText}\" ничего не  найдено"
            binding.layoutError.visibility = View.VISIBLE
            binding.loadMovies.visibility = View.INVISIBLE
        } else
            binding.layoutError.visibility = View.GONE
    }

    private fun updateBtnClear() {
        if (mainViewModel.searchText.isNotEmpty())
            binding.btnSearchClear.visibility = View.VISIBLE
        else
            binding.btnSearchClear.visibility = View.GONE
    }

    private fun searchMovies() {
        val currentText = binding.inputSearch.text.toString()
        if (mainViewModel.searchText == currentText) return
        mainViewModel.searchText = currentText
        loadMovies()
    }

    private fun loadMovies() {
        binding.layoutError.visibility = View.GONE
        binding.loadMovies.visibility = View.VISIBLE

        if (mainViewModel.searchText.isNotEmpty())
            mainViewModel.searchMovies(mainViewModel.searchText)
        else
            mainViewModel.searchMovies()
    }

    private fun showErrorSnackBar() {
        Snackbar.make(binding.root, "Проверьте соединение с интернетом и попробуйте ещё раз",
            Snackbar.LENGTH_SHORT).show()
        binding.loadMovies.visibility = View.INVISIBLE
    }

    override fun onMovieClick(movie: Movie) {
        Toast.makeText(this, movie.title, Toast.LENGTH_SHORT).show()
    }

    override fun onAddToFavorites(movie: Movie) {
        mainViewModel.updateMovie(movie)
    }

}