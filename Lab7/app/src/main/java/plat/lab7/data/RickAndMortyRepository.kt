package plat.lab7.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import plat.lab7.data.local.AppDatabase
import plat.lab7.data.mapper.toEntity
import plat.lab7.data.mapper.toModel

class RickAndMortyRepository(
    private val database: AppDatabase
) {

    private val characterDao = database.characterDao()
    private val locationDao = database.locationDao()

    fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getCharacterById(id: Int): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.toModel()
        }
    }

    fun getFavoriteCharacters(): Flow<List<Character>> {
        return characterDao.getFavoriteCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getLocationById(id: Int): Flow<Location?> {
        return locationDao.getLocationById(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }

    suspend fun synchronizeInitialData() {
        database.withTransaction {
            val characterCount = characterDao.getCount()
            val locationCount = locationDao.getCount()

            if (characterCount == 0) {
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { it.toEntity() }

                characterDao.insertAll(characters)
            }

            if (locationCount == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { it.toEntity() }

                locationDao.insertAll(locations)
            }
        }
    }

    suspend fun hasInitialData(): Boolean {
        return characterDao.getCount() > 0 &&
                locationDao.getCount() > 0
    }

    suspend fun clearAllData() {
        database.withTransaction {
            characterDao.deleteAll()
            locationDao.deleteAll()
        }
    }
}
