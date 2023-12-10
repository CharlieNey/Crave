package hu.ait.crave.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.crave.R
import hu.ait.crave.ui.theme.Yellow80
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginScreenViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("peter@ait.hu") }
    var password by rememberSaveable { mutableStateOf("123456") }

    val coroutineScope = rememberCoroutineScope()
    val eggyolkColor = Color(254, 236, 153)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(eggyolkColor)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.cravecircle),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(350.dp) // Adjust the size as needed
            )

            Text(
                text = "Please login to continue.",
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.opensans))
            )
            
            Spacer(modifier = Modifier.size(40.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f),

                label = {
                    Text(text = "E-mail",
                        fontFamily = FontFamily(Font(R.font.opensans)))
                },
                value = email,
                onValueChange = {
                    email = it
                },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Email, null)
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 25.dp),
                label = {
                    Text(text = "Password",
                        fontFamily = FontFamily(Font(R.font.opensans)))
                },
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(Icons.Default.Password, null)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        if (showPassword) {
                            Icon(Icons.Default.Visibility, null)
                        } else {
                            Icon(Icons.Default.VisibilityOff, null)
                        }
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                    coroutineScope.launch {
                        val result = loginViewModel.loginUser(email,password)
                        if (result?.user != null) {
                            onLoginSuccess()
                        }
                    }
                }) {
                    Text(
                        text = "Login",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.opensans))
                    )
                }
                OutlinedButton(onClick = {
                    loginViewModel.registerUser(email,password)
                }) {
                    Text(
                        text = "Register",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.opensans))
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (loginViewModel.loginUiState) {
                is LoginUiState.Loading -> CircularProgressIndicator()
                is LoginUiState.RegisterSuccess -> Text(text = "Registration OK", fontFamily = FontFamily(Font(R.font.opensans)))
                is LoginUiState.Error -> Text(text = "Error: ${
                    (loginViewModel.loginUiState as LoginUiState.Error).error
                }", fontFamily = FontFamily(Font(R.font.opensans)))
                is LoginUiState.LoginSuccess -> Text(text = "Login OK", fontFamily = FontFamily(Font(R.font.opensans)))
                LoginUiState.Init -> {}
            }

        }
    }
}