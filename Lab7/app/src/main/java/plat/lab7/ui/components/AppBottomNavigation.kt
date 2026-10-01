package plat.lab7.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import plat.lab7.ui.theme.Lab7Theme

enum class BottomDestination {
    Characters,
    Locations,
    Profile
}

@Composable
fun AppBottomNavigation(
    selectedDestination: BottomDestination,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = selectedDestination == BottomDestination.Characters,
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Characters"
                )
            },
            label = {
                Text("Characters")
            },
            colors = navigationItemColors()
        )

        NavigationBarItem(
            selected = selectedDestination == BottomDestination.Locations,
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = {
                Text("Locations")
            },
            colors = navigationItemColors()
        )

        NavigationBarItem(
            selected = selectedDestination == BottomDestination.Profile,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            },
            colors = navigationItemColors()
        )
    }
}

@Composable
private fun navigationItemColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary
    )

@Preview(showBackground = true)
@Composable
fun AppBottomNavigationPreview() {
    Lab7Theme {
        AppBottomNavigation(
            selectedDestination = BottomDestination.Characters,
            onCharactersClick = {},
            onLocationsClick = {},
            onProfileClick = {}
        )
    }
}