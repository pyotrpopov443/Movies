package pyotr.popov443.movies.data.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class Favorite(@PrimaryKey @ColumnInfo(name = "id") val id: Int)