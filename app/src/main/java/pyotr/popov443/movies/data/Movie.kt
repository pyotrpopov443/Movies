package pyotr.popov443.movies.data

data class Movie(val id: Int, val poster: String?, val title: String?, val overview: String?,
                 val date: String?, var favorite: Boolean)
