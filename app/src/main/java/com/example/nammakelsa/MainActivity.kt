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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.google.firebase.database.*
import kotlinx.coroutines.delay
import java.util.Locale

data class Worker(

    val id: String = "",

    val name: String = "",

    val skill: String = "",

    val location: String = "",

    val rate: String = "",

    val rating: Float = 0f,

    val experience: Int = 0,

    val imageUrls: List<String> = emptyList(),

    val isBlocked: Boolean = false
)

class MainActivity : ComponentActivity() {

    companion object {

        var isDarkMode by mutableStateOf(false)

        val historyList = mutableStateListOf<String>()

        val workerList = mutableStateListOf<Worker>()

        lateinit var database: DatabaseReference
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        database =
            FirebaseDatabase
                .getInstance()
                .getReference("workers")

        loadWorkersFromFirebase()

        setContent {

            NammaKelsaTheme(
                darkTheme = isDarkMode
            ) {

                AppNavigation()
            }
        }
    }

    private fun loadWorkersFromFirebase() {

        database.addValueEventListener(

            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    workerList.clear()

                    for (workerSnapshot in snapshot.children) {

                        val worker =
                            workerSnapshot.getValue(
                                Worker::class.java
                            )

                        if (worker != null) {

                            workerList.add(worker)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
        )
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

        composable("home") {
            WorkerListScreen(navController)
        }

        composable("worker_login") {
            WorkerLoginScreen(navController)
        }

        composable("worker_signup") {
            WorkerSignupScreen(navController)
        }

        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }

        composable("admin_login") {
            AdminLoginScreen(navController)
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

        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.namma_logo),
            contentDescription = null,
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Namma Kelsa",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Building Livelihoods, Empowering Lives",
            fontSize = 14.sp
        )
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

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.namma_logo
                    ),

                    contentDescription = null,

                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "Namma Kelsa",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text =
                            "Building Livelihoods, Empowering Lives",

                        fontSize = 10.sp
                    )
                }
            }

            Row {

                IconButton(
                    onClick = {
                        navController.navigate("admin_login")
                    }
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.AdminPanelSettings,

                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate("worker_login")
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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "⭐ Recommended Workers",
            fontSize = 22.sp,
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
fun AdminLoginScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Admin Login",
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
                Text("Admin Email")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Password")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                if (
                    email == "nandithacl@gmail.com"
                    &&
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
                        "Invalid Email or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {

            Text("Login")
        }
    }
}

@Composable
fun WorkerLoginScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Worker Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Password")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Worker Login Successful",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("worker_page")
            }
        ) {

            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = {
                navController.navigate("worker_signup")
            }
        ) {

            Text("Create Account")
        }

        TextButton(
            onClick = {
                navController.navigate("forgot_password")
            }
        ) {

            Text("Forgot Password?")
        }
    }
}

@Composable
fun WorkerSignupScreen(navController: NavHostController) {

    var name by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Worker Signup",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,

            onValueChange = {
                name = it
            },

            label = {
                Text("Name")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Password")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Account Created Successfully",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("worker_login")
            }
        ) {

            Text("Create Account")
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {

    var email by remember { mutableStateOf("") }

    var newPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Reset Password",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Enter Email")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newPassword,

            onValueChange = {
                newPassword = it
            },

            label = {
                Text("New Password")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                Toast.makeText(
                    context,
                    "Password Reset Successful",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate("worker_login")
            }
        ) {

            Text("Reset Password")
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

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.namma_logo
                    ),

                    contentDescription = null,

                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Worker Page",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
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
            text = "Add Worker",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Worker Name") }
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

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {

                val workerId =
                    MainActivity.database.push().key

                val worker = Worker(
                    id = workerId ?: "",
                    name = name,
                    skill = skill,
                    location = location,
                    rate = rate,
                    rating = rating.toFloatOrNull() ?: 0f,
                    experience = experience.toIntOrNull() ?: 0,
                    imageUrls = listOf(imageUrl)
                )

                if (workerId != null) {

                    MainActivity.database
                        .child(workerId)
                        .setValue(worker)
                }

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

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.namma_logo
                    ),

                    contentDescription = null,

                    modifier = Modifier.size(55.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Admin Panel",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
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

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),

            elevation =
                CardDefaults.cardElevation(6.dp)
        ) {

            Row(
                modifier = Modifier.padding(16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.mypic
                    ),

                    contentDescription = null,

                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),

                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Text(
                        text = "Nanditha",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Phone : 8105983676")

                    Spacer(modifier = Modifier.height(4.dp))

                    Text("Email : nandithacl88@gmail.com")
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

            Text("Add Worker")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate("delete_worker")
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Delete Worker")
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

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {

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
                        Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(worker.name)

                        Text(worker.skill)
                    }

                    IconButton(
                        onClick = {

                            MainActivity.database
                                .child(worker.id)
                                .removeValue()

                            MainActivity.historyList.add(
                                "Deleted Worker : ${worker.name}"
                            )

                            Toast.makeText(
                                context,
                                "Worker Deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Delete,

                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BlockWorkerScreen() {

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {

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
                        Arrangement.SpaceBetween
                ) {

                    Column {

                        Text(worker.name)

                        Text(worker.skill)

                        if (worker.isBlocked) {

                            Text("Blocked")
                        }
                    }

                    IconButton(
                        onClick = {

                            MainActivity.database
                                .child(worker.id)
                                .child("blocked")
                                .setValue(!worker.isBlocked)

                            Toast.makeText(
                                context,
                                "Worker Status Updated",
                                Toast.LENGTH_SHORT
                            ).show()
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

@Composable
fun ManageFeatureScreen() {

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {

        items(MainActivity.historyList) { item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {

                Text(
                    text = item,

                    modifier =
                        Modifier.padding(16.dp)
                )
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

            Spacer(modifier = Modifier.height(5.dp))

            Text("Skill : ${worker.skill}")

            Text("Location : ${worker.location}")

            Text("Rate : ${worker.rate}")

            Text("Rating : ⭐ ${worker.rating}")

            Text(
                "Experience : ${worker.experience} years"
            )

            Spacer(modifier = Modifier.height(12.dp))

            worker.imageUrls.forEach { image ->

                AsyncImage(
                    model = image,

                    contentDescription = null,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),

                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

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

                Spacer(modifier = Modifier.width(10.dp))

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