package plat.lab7.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import plat.lab7.data.local.entity.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getCount(): Int

    @Query("DELETE FROM locations")
    suspend fun deleteAll()
}
