# IIITU Events App

## Project Description

The IIITU Events App is designed to keep students and faculty informed about the various events happening at the Indian Institute of Information Technology Una (IIITU). This Android application retrieves event details from a Firestore database and displays them to the users. Users can see event titles, descriptions, venues, dates, times, and contact information, along with an image related to the event.

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
3. [Features](#features)
4. [Code Snippet](#code-snippet)
5. [Contributing](#contributing)


## Installation

To set up the project locally, follow these steps:

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/iiitu-events-app.git
    cd iiitu-events-app
    ```

2. Open the project in Android Studio.

3. Add the necessary dependencies in your `build.gradle` file:
    ```gradle
    implementation 'com.google.firebase:firebase-firestore:24.0.0'
    implementation 'com.squareup.picasso:picasso:2.71828'
    ```

4. Sync your project with Gradle files.

5. Set up Firebase Firestore:
   - Go to the Firebase Console.
   - Create a new project or use an existing one.
   - Add an Android app to your Firebase project.
   - Register the app with the package name `com.example.progothon`.
   - Download the `google-services.json` file and place it in the `app` directory of your Android Studio project.

6. Enable Firestore in the Firebase Console.

## Usage

To use the app:

1. Launch the application on your Android device or emulator.
2. The app will display a list of clubs.
3. Select a club to view its events.
4. Click on an event to view its details, including the title, description, venue, date, time, contact information, and an image.

## Features

- Retrieve and display event details from Firestore.
- Display event images using Picasso.
- Real-time updates of event details.
- User-friendly interface.

## Code Snippet

Here's a crucial code snippet for fetching and displaying event details:

```kotlin
package com.example.progothon

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class Fetch_deatils : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var title1: TextView
    private lateinit var description1: TextView
    private lateinit var venue1: TextView
    private lateinit var date1: TextView
    private lateinit var time1: TextView
    private lateinit var image: ImageView
    private lateinit var contact1: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fetch_details)
        image = findViewById(R.id.imvevent)
        title1 = findViewById(R.id.evtnam)
        description1 = findViewById(R.id.des)
        venue1 = findViewById(R.id.etven)
        date1 = findViewById(R.id.etDate)
        time1 = findViewById(R.id.ettim)
        contact1 = findViewById(R.id.etph)

        val selectedClub = intent.getStringExtra("clubName")
        val eventname = intent.getStringExtra("key")

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Pass the event name to fetch data
        title1.text = eventname
        fetchDataAndDisplay(selectedClub!!, eventname!!)
    }

    private fun fetchDataAndDisplay(selectedClub: String, eventName: String) {
        // Assume you have a collection named "clubs" in Firestore
        firestore.collection("clubs")
            .document(selectedClub)
            .collection("events")
            .document(eventName)  // Use the event name as the document ID
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Access the data from the document
                    val title = document.getString("title")
                    val description = document.getString("description")
                    val venue = document.getString("venue")
                    val date = document.getString("date")
                    val time = document.getString("time")
                    val contact = document.getString("contact")
                    val imageUrl = document.getString("imageUrl")

                    // Update the EditText fields with the fetched data
                    title1.text = title
                    description1.text = description
                    venue1.text = venue
                    date1.text = date
                    time1.text = time
                    contact1.text = contact

                    // Load and display the image using Picasso
                    if (imageUrl != null && imageUrl.isNotEmpty()) {
                        Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.loading) // Placeholder image while loading
                            .error(R.drawable.error) // Image to show if loading fails
                            .into(image)
                    }
                } else {
                    Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Toast.makeText(this, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}


Contributing

Contributions are welcome! If you would like to contribute to this project, please follow these steps:

    Fork the repository.
    Create a new branch (git checkout -b feature-branch).
    Make your changes.
    Commit your changes (git commit -m 'Add some feature').
    Push to the branch (git push origin feature-branch).
    Open a pull request.
