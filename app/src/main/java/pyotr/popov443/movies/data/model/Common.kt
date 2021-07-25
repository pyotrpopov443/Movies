package pyotr.popov443.movies.data.model

object Common {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}