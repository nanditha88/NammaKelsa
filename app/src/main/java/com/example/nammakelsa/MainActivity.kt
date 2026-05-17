package com.example.nammakelsa

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.nammakelsa.ui.theme.NammaKelsaTheme
import kotlinx.coroutines.delay
import java.util.Locale

data class Worker(
    val name: String,
    val skill: String,
    val location: String,
    val rate: String,
    val rating: Float,
    val experience: Int,
    val imageUrls: List<String>,
    val isBlocked: Boolean = false
)

class MainActivity : ComponentActivity() {

    companion object {

        var isDarkMode by mutableStateOf(false)

        val historyList = mutableStateListOf<String>()

        val workerList = mutableStateListOf(

            Worker(
                "Ravi",
                "Painter",
                "Bangalore",
                "₹500/day",
                4.8f,
                5,
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
                4.7f,
                6,
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
                4.6f,
                4,
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
                4.9f,
                7,
                listOf(
                    "https://images.pexels.com/photos/4505161/pexels-photo-4505161.jpeg",
                    "https://images.pexels.com/photos/1301856/pexels-photo-1301856.jpeg",
                    "https://images.pexels.com/photos/2132227/pexels-photo-2132227.jpeg"
                )
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            NammaKelsaTheme(
                darkTheme = isDarkMode
            ) {

                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("signup") {
            SignupScreen(navController)
        }

        composable("home") {
            WorkerListScreen(navController)
        }

        composable("worker_page") {
            WorkerPage(navController)
        }

        composable("create_worker") {
            CreateWorkerScreen(navController)
        }

        composable("admin") {
            AdminScreen(navController)
        }

        composable("delete_worker") {
            DeleteWorkerScreen()
        }

        composable("block_worker") {
            BlockWorkerScreen()
        }

        composable("manage_feature") {
            ManageFeatureScreen()
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {

    LaunchedEffect(Unit) {

        delay(2500)

        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.namma_logo),
            contentDescription = null,
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Namma Kelsa",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Building Livelihoods, Empowering Lives",
            fontSize = 14.sp
        )
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.namma_logo),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    email == "nandithacl@gmail.com" &&
                    password == "Nandu@12"
                ) {

                    Toast.makeText(
                        context,
                        "Admin Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("admin")

                } else {

                    Toast.makeText(
                        context,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("home")
                }
            }
        ) {

            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = {
                navController.navigate("signup")
            }
        ) {

            Text("Create Account")
        }
    }
}

@Composable
fun SignupScreen(navController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.namma_logo),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Signup",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Account Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("home")
            }
        ) {

            Text("Create Account")
        }
    }
}

@Composable
fun WorkerListScreen(navController: NavHostController) {

    var searchText by remember { mutableStateOf("") }

    val filteredWorkers = MainActivity.workerList.filter {

        !it.isBlocked &&
                it.skill.contains(searchText, ignoreCase = true)
    }

    val voiceLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.StartActivityForResult()
        ) { result ->

            val spokenText =
                result.data
                    ?.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                    ?.get(0)

            if (spokenText != null) {
                searchText = spokenText
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.namma_logo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {

                    Text(
                        text = "Namma Kelsa",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Building Livelihoods, Empowering Lives",
                        fontSize = 10.sp
                    )
                }
            }

            Row {

                IconButton(
                    onClick = {
                        navController.navigate("admin")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("worker_page")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        MainActivity.isDarkMode =
                            !MainActivity.isDarkMode
                    }
                ) {

                    Icon(
                        imageVector =
                            if (MainActivity.isDarkMode)
                                Icons.Default.LightMode
                            else
                                Icons.Default.DarkMode,

                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = searchText,

                onValueChange = {
                    searchText = it
                },

                label = {
                    Text("Search workers")
                },

                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {

                    val intent = Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                    )

                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )

                    intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault()
                    )

                    voiceLauncher.launch(intent)
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "⭐ Recommended Workers",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {

            items(filteredWorkers) { worker ->

                WorkerCard(worker)
            }
        }
    }
}

@Composable
fun WorkerPage(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.namma_logo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "Workers Panel",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Namma Kelsa",
                        fontSize = 12.sp
                    )
                }
            }

            Row {

                IconButton(
                    onClick = {
                        navController.navigate("create_worker")
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        MainActivity.isDarkMode =
                            !MainActivity.isDarkMode
                    }
                ) {

                    Icon(
                        imageVector =
                            if (MainActivity.isDarkMode)
                                Icons.Default.LightMode
                            else
                                Icons.Default.DarkMode,

                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            items(
                MainActivity.workerList.filter {
                    !it.isBlocked
                }
            ) { worker ->

                WorkerCard(worker)
            }
        }
    }
}

@Composable
fun CreateWorkerScreen(navController: NavHostController) {

    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Create Worker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = skill,
            onValueChange = { skill = it },
            label = { Text("Skill") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Rate") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Photo URL") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val worker = Worker(
                    name,
                    skill,
                    location,
                    rate,
                    rating.toFloatOrNull() ?: 0f,
                    experience.toIntOrNull() ?: 0,
                    listOf(imageUrl)
                )

                MainActivity.workerList.add(worker)

                MainActivity.historyList.add(
                    "Added Worker : $name"
                )

                Toast.makeText(
                    context,
                    "Worker Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("worker_page")
            }
        ) {

            Text("Submit")
        }
    }
}

@Composable
fun AdminScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.namma_logo),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "Admin Panel",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Namma Kelsa",
                        fontSize = 12.sp
                    )
                }
            }

            IconButton(
                onClick = {
                    MainActivity.isDarkMode =
                        !MainActivity.isDarkMode
                }
            ) {

                Icon(
                    imageVector =
                        if (MainActivity.isDarkMode)
                            Icons.Default.LightMode
                        else
                            Icons.Default.DarkMode,

                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.mypic),
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Text(
                        text = "NANDITHA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Sir MVIT",
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "8105983676",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "nandithacl88@gmail.com",
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                navController.navigate("create_worker")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Add Worker Manually")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("delete_worker")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Delete Worker Permanently")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("block_worker")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Block Worker")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("manage_feature")
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Manage Features")
        }
    }
}

@Composable
fun DeleteWorkerScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Delete Workers",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            itemsIndexed(MainActivity.workerList) { index, worker ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = worker.name,
                                fontWeight = FontWeight.Bold
                            )

                            Text(worker.skill)
                        }

                        IconButton(
                            onClick = {

                                MainActivity.historyList.add(
                                    "Deleted Worker : ${worker.name}"
                                )

                                MainActivity.workerList.removeAt(index)

                                Toast.makeText(
                                    context,
                                    "Worker Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BlockWorkerScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Block / Unblock Workers",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            itemsIndexed(MainActivity.workerList) { index, worker ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = worker.name,
                                fontWeight = FontWeight.Bold
                            )

                            Text(worker.skill)

                            if (worker.isBlocked) {

                                Text("Blocked")
                            }
                        }

                        IconButton(
                            onClick = {

                                MainActivity.workerList[index] =
                                    worker.copy(
                                        isBlocked = !worker.isBlocked
                                    )

                                if (!worker.isBlocked) {

                                    MainActivity.historyList.add(
                                        "Blocked Worker : ${worker.name}"
                                    )

                                    Toast.makeText(
                                        context,
                                        "Worker Blocked",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {

                                    MainActivity.historyList.add(
                                        "Unblocked Worker : ${worker.name}"
                                    )

                                    Toast.makeText(
                                        context,
                                        "Worker Unblocked",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) {



                            Icon(
                                imageVector =
                                    if (worker.isBlocked)
                                        Icons.Default.LockOpen
                                    else
                                        Icons.Default.Lock,

                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageFeatureScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Admin History",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {

            items(MainActivity.historyList) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Text(
                        text = item,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
            .padding(vertical = 8.dp),

        elevation =
            CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = worker.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text("Skill: ${worker.skill}")
            Text("Location: ${worker.location}")
            Text("Rate: ${worker.rate}")
            Text("Rating: ⭐ ${worker.rating}")
            Text("Experience: ${worker.experience} years")

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                worker.imageUrls.forEach { image ->

                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                Button(
                    onClick = {

                        val intent =
                            Intent(Intent.ACTION_DIAL)

                        intent.data =
                            "tel:8105983676".toUri()

                        context.startActivity(intent)
                    }
                ) {

                    Text("Call")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {

                        Toast.makeText(
                            context,
                            "Booking Confirmed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {

                    Text("Book")
                }
            }
        }
    }
}

