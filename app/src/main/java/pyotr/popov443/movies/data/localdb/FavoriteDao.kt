package pyotr.popov443.movies.data.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites() : List<Favorite>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun delete(id: Int)

}