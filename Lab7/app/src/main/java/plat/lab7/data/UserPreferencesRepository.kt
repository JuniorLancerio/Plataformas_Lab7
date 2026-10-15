
package plat.lab7.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(
    private val context: Context
) {
    private val nameKey = stringPreferencesKey("name")

    val userName: Flow<String?> = context.userDataStore.data.map { preferences ->
        preferences[nameKey]
    }

    suspend fun saveName(name: String) {
        context.userDataStore.edit { preferences ->
            preferences[nameKey] = name
        }
    }

    suspend fun clearName() {
        context.userDataStore.edit { preferences ->
            preferences.remove(nameKey)
        }
    }
}
