
package plat.lab7.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plat.lab7.R
import plat.lab7.ui.theme.Lab7Theme

@Composable
fun LoginScreen(
    onLogin: (String) -> Unit
) {
    var name by rememberSaveable {
        mutableStateOf("")
    }

    LoginContent(
        name = name,
        onNameChange = { name = it },
        onLogin = {
            if (name.isNotBlank()) {
                onLogin(name.trim())
            }
        }
    )
}

@Composable
fun LoginContent(
    name: String,
    onNameChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.rick_morty_logo
            ),
            contentDescription = "Rick and Morty logo",
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 260.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = {
                Text("Nombre")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onLogin,
            enabled = name.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    Lab7Theme {
        LoginContent(
            name = "",
            onNameChange = {},
            onLogin = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenFilledPreview() {
    Lab7Theme {
        LoginContent(
            name = "Junior Lancerio",
            onNameChange = {},
            onLogin = {}
        )
    }
}
