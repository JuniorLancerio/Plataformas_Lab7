package plat.lab7.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import plat.lab7.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCount(): Int

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}
