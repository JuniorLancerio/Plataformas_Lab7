package plat.lab7.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab7.ui.characters.CharacterDetailsScreen
import plat.lab7.ui.characters.CharactersScreen
import plat.lab7.ui.components.AppBottomNavigation
import plat.lab7.ui.components.BottomDestination
import plat.lab7.ui.locations.LocationDetailsScreen
import plat.lab7.ui.locations.LocationsScreen
import plat.lab7.ui.login.LoginScreen
import plat.lab7.ui.profile.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {
        composable<LoginDestination> {
            LoginScreen(
                onStart = {
                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MainDestination> {
            val mainNavController = rememberNavController()

            val currentBackStackEntry by
            mainNavController.currentBackStackEntryAsState()

            val currentDestination =
                currentBackStackEntry?.destination

            val isCharacterDetails =
                currentDestination
                    ?.hasRoute<CharacterDetailsDestination>() == true

            val isLocationDetails =
                currentDestination
                    ?.hasRoute<LocationDetailsDestination>() == true

            val showBottomBar =
                !isCharacterDetails && !isLocationDetails

            val selectedDestination = when {
                currentDestination
                    ?.hasRoute<LocationsGraph>() == true ||
                        currentDestination
                            ?.hasRoute<LocationsDestination>() == true -> {
                    BottomDestination.Locations
                }

                currentDestination
                    ?.hasRoute<ProfileDestination>() == true -> {
                    BottomDestination.Profile
                }

                else -> {
                    BottomDestination.Characters
                }
            }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        AppBottomNavigation(
                            selectedDestination = selectedDestination,
                            onCharactersClick = {
                                mainNavController.navigate(
                                    CharactersGraph
                                ) {
                                    launchSingleTop = true

                                    popUpTo(
                                        mainNavController.graph.startDestinationId
                                    )
                                }
                            },
                            onLocationsClick = {
                                mainNavController.navigate(
                                    LocationsGraph
                                ) {
                                    launchSingleTop = true

                                    popUpTo(
                                        mainNavController.graph.startDestinationId
                                    )
                                }
                            },
                            onProfileClick = {
                                mainNavController.navigate(
                                    ProfileDestination
                                ) {
                                    launchSingleTop = true

                                    popUpTo(
                                        mainNavController.graph.startDestinationId
                                    )
                                }
                            }
                        )
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = mainNavController,
                    startDestination = CharactersGraph,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    charactersGraph(
                        onCharacterClick = { id ->
                            mainNavController.navigate(
                                CharacterDetailsDestination(
                                    id = id
                                )
                            )
                        },
                        onBack = {
                            mainNavController.popBackStack()
                        }
                    )

                    locationsGraph(
                        onLocationClick = { id ->
                            mainNavController.navigate(
                                LocationDetailsDestination(
                                    id = id
                                )
                            )
                        },
                        onBack = {
                            mainNavController.popBackStack()
                        }
                    )

                    composable<ProfileDestination> {
                        ProfileScreen(
                            onLogout = {
                                navController.navigate(
                                    LoginDestination
                                ) {
                                    popUpTo(
                                        navController.graph.id
                                    ) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.charactersGraph(
    onCharacterClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {
        composable<CharactersDestination> {
            CharactersScreen(
                onCharacterClick = onCharacterClick
            )
        }

        composable<CharacterDetailsDestination> { backStackEntry ->
            val details =
                backStackEntry
                    .toRoute<CharacterDetailsDestination>()

            CharacterDetailsScreen(
                id = details.id,
                onBack = onBack
            )
        }
    }
}

fun NavGraphBuilder.locationsGraph(
    onLocationClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {
        composable<LocationsDestination> {
            LocationsScreen(
                onLocationClick = onLocationClick
            )
        }

        composable<LocationDetailsDestination> { backStackEntry ->
            val details =
                backStackEntry
                    .toRoute<LocationDetailsDestination>()

            LocationDetailsScreen(
                id = details.id,
                onBack = onBack
            )
        }
    }
}