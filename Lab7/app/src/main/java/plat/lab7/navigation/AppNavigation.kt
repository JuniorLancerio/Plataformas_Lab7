package plat.lab7.navigation

import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import plat.lab7.ui.characters.CharacterDetailsScreen
import plat.lab7.ui.characters.CharactersScreen
import plat.lab7.ui.components.AppBottomNavigation
import plat.lab7.ui.components.BottomDestination
import plat.lab7.ui.locations.LocationDetailsScreen
import plat.lab7.ui.locations.LocationsScreen
import plat.lab7.ui.login.LoginScreen
import plat.lab7.ui.profile.ProfileScreen
import plat.lab7.viewmodel.SessionViewModel

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = viewModel()
) {
    val navController = rememberNavController()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = SplashDestination
    ) {
        composable<SplashDestination> {
            LaunchedEffect(sessionState.isLoading) {
                if (!sessionState.isLoading) {
                    val destination = if (sessionState.userName != null) {
                        MainDestination
                    } else {
                        LoginDestination
                    }

                    navController.navigate(destination) {
                        popUpTo<SplashDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        composable<LoginDestination> {
            LoginScreen(
                isSyncing = sessionState.isSyncing,
                hasError = sessionState.hasError,
                onLogin = { name ->
                    sessionViewModel.login(name) {
                        navController.navigate(MainDestination) {
                            popUpTo<LoginDestination> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable<MainDestination> {
            val mainNavController = rememberNavController()

            val currentBackStackEntry by
            mainNavController.currentBackStackEntryAsState()

            val currentDestination = currentBackStackEntry?.destination

            val isCharacterDetails =
                currentDestination?.hasRoute<CharacterDetailsDestination>() == true

            val isLocationDetails =
                currentDestination?.hasRoute<LocationDetailsDestination>() == true

            val showBottomBar = !isCharacterDetails && !isLocationDetails

            val selectedDestination = when {
                currentDestination?.hasRoute<LocationsDestination>() == true ||
                        currentDestination?.hierarchy?.any {
                            it.hasRoute<LocationsGraph>()
                        } == true -> BottomDestination.Locations

                currentDestination?.hasRoute<ProfileDestination>() == true ->
                    BottomDestination.Profile

                else -> BottomDestination.Characters
            }

            Scaffold(
                bottomBar = {
                    if (showBottomBar) {
                        AppBottomNavigation(
                            selectedDestination = selectedDestination,
                            onCharactersClick = {
                                mainNavController.navigate(CharactersGraph) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onLocationsClick = {
                                mainNavController.navigate(LocationsGraph) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onProfileClick = {
                                mainNavController.navigate(ProfileDestination) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
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
                                CharacterDetailsDestination(id)
                            )
                        },
                        onBack = {
                            mainNavController.popBackStack()
                        }
                    )

                    locationsGraph(
                        onLocationClick = { id ->
                            mainNavController.navigate(
                                LocationDetailsDestination(id)
                            )
                        },
                        onBack = {
                            mainNavController.popBackStack()
                        }
                    )

                    composable<ProfileDestination> {
                        ProfileScreen(
                            userName = sessionState.userName.orEmpty(),
                            onLogout = {
                                sessionViewModel.logout {
                                    navController.navigate(LoginDestination) {
                                        popUpTo<MainDestination> {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
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

        composable<CharacterDetailsDestination> {
            CharacterDetailsScreen(
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

        composable<LocationDetailsDestination> {
            LocationDetailsScreen(
                onBack = onBack
            )
        }
    }
}
