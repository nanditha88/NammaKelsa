 
Android App Development using GenAI – Namma-Kelsa
________________________________________

1.	Problem Statement
   
Many skilled local workers such as painters, electricians, plumbers, drivers, nurses, chefs, and housekeepers struggle to find regular job opportunities. At the same time, people often face difficulty finding trusted workers quickly in their local area.
Namma Kelsa solves this problem by providing a digital platform where users can easily search, view, and contact workers based on their skills. The application also provides an admin management system for adding, deleting, and blocking workers using Firebase Realtime Database.
________________________________________

Table of Contents

•	Problem Statement 
•	Description 
•	Features 
•	Technologies Used 
•	Getting Started
•	Installation
•	Usage 
•	Firebase Setup 
•	Project Structure 
•	Contributing
•	License 
•	Contact & Acknowledgments 
•	Future Enhancements
•	Conclusion
________________________________________

2.	Description
   
Namma Kelsa is an Android application built using Kotlin and Jetpack Compose. The application helps connect local workers with users who are searching for services.
The platform includes:
•	Worker listing system 
•	Search and voice search feature 
•	Worker login and signup 
•	Admin login system 
•	Worker management panel 
•	Firebase Realtime Database integration 
•	Dark and light mode support 
•	Booking and calling functionality 
•	Worker categories like Painter, Driver, Nurse, Chef, Electrician, etc. 
The application is designed to support local employment and make worker hiring faster and easier.
________________________________________

3.	Main Features
   
•	Splash Screen 
•	Home Page 
•	Voice Search 
•	Worker Login 
•	Worker Signup 
•	Forgot Password 
•	Worker List 
•	Worker Images 
•	Call Feature 
•	Booking Feature 
•	Dark/Light Mode 
•	Admin Login 
•	Admin Panel 
•	Add Worker 
•	Delete Worker 
•	Block Worker 
•	Admin History 
•	Navigation System 
•	Worker Page 
•	Add Worker Form 
•	Firebase Realtime Database 
•	Category Buttons 
•	Search Filtering 
________________________________________

4.	Technologies Used

•	Kotlin 
•	Jetpack Compose 
•	Firebase Realtime Database 
•	Android Studio 
•	Material 3 
•	Coil Image Library 
•	Navigation Compose
________________________________________
5.	Getting Started / Prerequisites
Before running this project, make sure you have:
•	Android Studio installed 
•	Kotlin support enabled 
•	Firebase account 
•	Android device or emulator 
•	Internet connection 
Required Dependencies
Add these dependencies in your build.gradle file:
implementation("androidx.navigation:navigation-compose:2.7.7")

implementation("io.coil-kt:coil-compose:2.5.0")

implementation("com.google.firebase:firebase-database-ktx:20.3.0")

implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

implementation("com.google.firebase:firebase-analytics")
________________________________________

6.	Installation

Step 1: Clone the Repository
git clone https://github.com/yourusername/namma-kelsa.git
Step 2: Open in Android Studio
•	Open Android Studio 
•	Click Open Project 
•	Select the cloned folder 
Step 3: Connect Firebase
1.	Open Firebase Console 
2.	Create a new Firebase project 
3.	Add Android App 
4.	Download google-services.json 
5.	Paste it inside: 
                app/google-services.json
 Step 4: Enable Firebase Realtime Database
•	Open Firebase Console 
•	Go to Realtime Database 
•	Create Database 
•	Start in test mode 
Step 5: Sync Project
•	Click:
Sync Now
Step 6: Run the Application
•	Connect Android device or emulator 
•	Click Run ▶️ 
________________________________________

7.	Usage

User Features
•	Search workers by skill 
•	Create an account 
•	Login securely 
•	Use voice search 
•	View worker details 
•	Call workers 
•	Book workers 
•	Switch dark/light mode 
________________________________________
Worker Features
•	Register worker details 
•	Add skills and experience 
•	Upload profile image 
________________________________________
Admin Features
•	Secure admin login 
•	Add workers 
•	Delete workers 
•	Block/unblock workers 
•	View admin history
•	Monitor all request 
________________________________________
Example Worker Data
Name: Ravi
Skill: Painter
Location: Bangalore
Rate: ₹500/day
Rating: 4.8
Experience: 5 years
________________________________________

8.	Firebase Database Structure

workers
       workerId
                 id
                name
                skill
                location
                 rate
                 rating
                experience
                imageUrls
                isBlocked
________________________________________
Admin Login Credentials
Email: nandithacl@gmail.com
Password: Nandu@12
________________________________________

9.	Project Structure

NammaKelsa/
│
├── app/
├── screens/
├── navigation/
├── components/
├── firebase/
├── model/
├── utils/
├── res/
└── MainActivity.kt
________________________________________

10.	Contributing

Contributions are welcome.
Steps to Contribute
1.	Fork the repository 
2.	Create a new branch 
git checkout -b feature-name
3.	Commit changes 
git commit -m "Added new feature"
4.	Push to GitHub 
git push origin feature-name
5.	Create Pull Request
________________________________________

11.	License

This project is licensed under the MIT License.
Example:
MIT License
Copyright (c) 2026
________________________________________

12.	Contact

Developer
Nanditha CL

College
MVJ Institute of Technology (MVIT)

Email
nandithacl88@gmail.com 

GitHub
https://github.com/nanditha88/
________________________________________

13.	Acknowledgments

Special thanks to:
•	Firebase 
•	Android Developers 
•	Jetpack Compose Documentation 
•	Open Source Community
________________________________________

14.	Future Improvements

•	Firebase Authentication 
•	Real-time booking system 
•	Location tracking 
•	Online payment integration 
•	Chat system 
•	Push notifications 
________________________________________

15.	Conclusion

Namma Kelsa is designed to simplify local job hiring and worker management by providing a smart, reliable, and scalable digital platform for both workers and users.
The application improves accessibility, transparency, and employment opportunities through modern Android technologies and cloud-based services.
________________________________________

Author
Developed by: Nanditha CL GitHub: @nanditha88

