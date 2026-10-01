package plat.lab7.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginDestination

@Serializable
object MainDestination

@Serializable
object CharactersGraph

@Serializable
object CharactersDestination

@Serializable
data class CharacterDetailsDestination(
    val id: Int
)

@Serializable
object LocationsGraph

@Serializable
object LocationsDestination

@Serializable
data class LocationDetailsDestination(
    val id: Int
)

@Serializable
object ProfileDestination