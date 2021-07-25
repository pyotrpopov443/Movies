package pyotr.popov443.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pyotr.popov443.movies.data.Movie
import pyotr.popov443.movies.databinding.ItemMovieBinding

class RecyclerMoviesAdapter(private var list: List<Movie>,
                            private val callback: MovieAdapterCallback) :
    RecyclerView.Adapter<RecyclerMoviesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load("https://image.tmdb.org/t/p/w500${list[position].poster}").into(holder.binding.imageMovie)
        holder.binding.textMovieName.text = list[position].title
        holder.binding.textMovieDescription.text = list[position].overview
        holder.binding.textDate.text = list[position].date
        holder.setLike(list[position].favorite)

        holder.binding.btnLikeMovie.setOnClickListener {
            list[position].favorite = !list[position].favorite
            holder.setLike(list[position].favorite)

            callback.onAddToFavorites(list[position])
        }

        holder.binding.root.setOnClickListener {
            callback.onMovieClick(list[position])
        }
    }

    fun setMovieList(movieList: List<Movie>) {
        list = movieList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setLike(favorite: Boolean) {
            if (favorite)
                binding.btnLikeMovie.setImageResource(R.drawable.ic_favorite)
            else
                binding.btnLikeMovie.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    interface MovieAdapterCallback {
        fun onMovieClick(movie: Movie)
        fun onAddToFavorites(movie: Movie)
    }

}