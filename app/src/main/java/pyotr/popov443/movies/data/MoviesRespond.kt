package pyotr.popov443.movies.data

data class MoviesRespond(val page: Int, val results: List<MovieResult>,
                         val total_results: Int, val total_pages: Int)