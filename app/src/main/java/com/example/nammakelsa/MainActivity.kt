package com.example.nammakelsa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.nammakelsa.ui.theme.NammaKelsaTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    companion object {
        var isDarkMode by mutableStateOf(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            NammaKelsaTheme(
                darkTheme = isDarkMode
            ) {

                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeChange = {
                        isDarkMode = it
                    }
                )
            }
        }
    }
}

data class Worker(
    val name: String,
    val skill: String,
    val location: String,
    val rate: String,
    val imageUrls: List<String>
)

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    var currentScreen by remember {
        mutableStateOf("splash")
    }

    when (currentScreen) {

        "splash" -> SplashScreen(
            onSplashFinished = {
                currentScreen = "login"
            }
        )

        "login" -> LoginScreen(
            onLoginSuccess = {
                currentScreen = "home"
            },
            onSignupClick = {
                currentScreen = "signup"
            }
        )

        "signup" -> SignupScreen(
            onSignupSuccess = {
                currentScreen = "home"
            },
            onBackToLogin = {
                currentScreen = "login"
            }
        )

        "home" -> WorkerListScreen(
            isDarkMode = isDarkMode,
            onThemeChange = onThemeChange
        )
    }
}

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {

    LaunchedEffect(true) {
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFE3F2FD)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.namma_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(220.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Namma Kelsa",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Building Livelihoods, Empowering Lives",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            CircularProgressIndicator()
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.namma_logo),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {

                Text(
                    text = "Namma Kelsa",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                Text(
                    text = "Building Livelihoods, Empowering Lives",
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Welcome Back!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            context,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        onLoginSuccess()

                    } else {

                        Toast.makeText(
                            context,
                            "Wrong Email or Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {

            Text("Login")
        }

        Spacer(modifier = Modifier.height(15.dp))

        TextButton(
            onClick = {
                onSignupClick()
            }
        ) {

            Text("Don't have an account? Sign Up")
        }
    }
}

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Full Name")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                auth.createUserWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val userId = auth.currentUser?.uid

                        val userData = mapOf(
                            "name" to name,
                            "email" to email
                        )

                        if (userId != null) {

                            database.child("users")
                                .child(userId)
                                .setValue(userData)
                        }

                        Toast.makeText(
                            context,
                            "Signup Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        onSignupSuccess()

                    } else {

                        Toast.makeText(
                            context,
                            "Signup Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {

            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(15.dp))

        TextButton(
            onClick = {
                onBackToLogin()
            }
        ) {

            Text("Already have an account? Login")
        }
    }
}

@Composable
fun WorkerListScreen(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val workers = listOf(

        Worker(
            "Ravi",
            "Painter",
            "Bangalore",
            "₹500/day",
            listOf(
                "https://images.pexels.com/photos/5691624/pexels-photo-5691624.jpeg",
                "https://images.pexels.com/photos/6474129/pexels-photo-6474129.jpeg",
                "https://images.pexels.com/photos/7218525/pexels-photo-7218525.jpeg"
            )
        ),

        Worker(
            "Suresh",
            "Electrician",
            "Mysore",
            "₹600/day",
            listOf(
                "https://images.pexels.com/photos/257736/pexels-photo-257736.jpeg",
                "https://images.pexels.com/photos/8005397/pexels-photo-8005397.jpeg",
                "https://images.pexels.com/photos/442154/pexels-photo-442154.jpeg"
            )
        ),

        Worker(
            "Manju",
            "Plumber",
            "Tumkur",
            "₹550/day",
            listOf(
                "https://images.pexels.com/photos/8486972/pexels-photo-8486972.jpeg",
                "https://images.pexels.com/photos/5691613/pexels-photo-5691613.jpeg",
                "https://images.pexels.com/photos/8292794/pexels-photo-8292794.jpeg"
            )
        ),

        Worker(
            "Kiran",
            "Gardener",
            "Hassan",
            "₹400/day",
            listOf(
                "https://images.pexels.com/photos/4505161/pexels-photo-4505161.jpeg",
                "https://images.pexels.com/photos/1301856/pexels-photo-1301856.jpeg",
                "https://images.pexels.com/photos/2132227/pexels-photo-2132227.jpeg"
            )
        )
    )

    val filteredWorkers = workers.filter {

        it.skill.contains(
            searchText,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.namma_logo),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "Namma Kelsa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Text(
                        text = "Building Livelihoods, Empowering Lives",
                        fontSize = 11.sp
                    )
                }
            }

            Switch(
                checked = isDarkMode,
                onCheckedChange = {
                    onThemeChange(it)
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AsyncImage(
            model = "https://img.freepik.com/free-vector/construction-workers-concept-illustration_114360-1396.jpg",
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            label = {
                Text("Search skills...")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.horizontalScroll(
                rememberScrollState()
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(
                selected = false,
                onClick = {
                    searchText = "Painter"
                },
                label = {
                    Text("Painter")
                }
            )

            FilterChip(
                selected = false,
                onClick = {
                    searchText = "Plumber"
                },
                label = {
                    Text("Plumber")
                }
            )

            FilterChip(
                selected = false,
                onClick = {
                    searchText = "Gardener"
                },
                label = {
                    Text("Gardener")
                }
            )

            FilterChip(
                selected = false,
                onClick = {
                    searchText = "Electrician"
                },
                label = {
                    Text("Electrician")
                }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Workers Near You",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {

            items(filteredWorkers) { worker ->

                WorkerCard(worker)
            }
        }
    }
}

@Composable
fun WorkerCard(worker: Worker) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Text(
                text = worker.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = worker.skill,
                color = Color.Blue,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text("📍 ${worker.location}")

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = worker.rate,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                worker.imageUrls.forEach { image ->

                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.size(110.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {

                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = "tel:8105983676".toUri()

                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Call")
            }
        }
    }
}