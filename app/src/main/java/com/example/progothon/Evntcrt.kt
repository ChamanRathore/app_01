package com.example.progothon

import android.app.Activity
import com.google.firebase.appcheck.FirebaseAppCheck
//import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.HashMap

class Evntcrt : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var spinner: Spinner
    private lateinit var uploadImage: ImageView
    private lateinit var uploadPdfButton: ImageView
    private var selectedImageUri: Uri? = null
    private var selectedPdfUri: Uri? = null
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedUri: Uri? = data?.data
                if (selectedUri.toString().endsWith(".pdf")) {
                    // Handle PDF selection
                    selectedPdfUri = selectedUri
                    // You can set a label or show a message to indicate PDF selection
                } else {
                    // Handle image selection
                    selectedImageUri = selectedUri
                    uploadImage.setImageURI(selectedImageUri)
                }
            }
        }

    private val pdfPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedPdfUri = data?.data
                // Handle the selected PDF file as needed
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evntcrt)
        uploadImage = findViewById(R.id.atchmnt)
        uploadPdfButton = findViewById(R.id.uploadPdfButton)

        // Initialize Firestore
        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
//        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance())
        firestore = FirebaseFirestore.getInstance()

        // Initialize the spinner and its adapter
        spinner = findViewById(R.id.clubSpinner)
        val clubs = resources.getStringArray(R.array.clubs)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clubs)
        spinner.adapter = adapter

        uploadImage.setOnClickListener {
            openGallery()
        }

        // Add a button to upload PDF

        uploadPdfButton.setOnClickListener {
            openPdfPicker()
        }

        val uploadButton: Button = findViewById(R.id.uploadButton)
        uploadButton.setOnClickListener {
            uploadDataToFirestore()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openPdfPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Check if there is an app available to handle the PDF picker intent
        if (intent.resolveActivity(packageManager) != null) {
            pdfPickerLauncher.launch(intent)
        } else {
            // No app available to handle the PDF picker intent
            Toast.makeText(this, "No app found to handle PDF files", Toast.LENGTH_SHORT).show()
        }
    }


    private fun uploadDataToFirestore() {
        val selectedClub = spinner.selectedItem.toString()
        val title = findViewById<EditText>(R.id.evnttitl).text.toString()
        val description = findViewById<EditText>(R.id.dscrptn).text.toString()
        val venue = findViewById<EditText>(R.id.vnue).text.toString()
        val date = findViewById<EditText>(R.id.date).text.toString()
        val time = findViewById<EditText>(R.id.tim).text.toString()
        val contact = findViewById<EditText>(R.id.contact).text.toString()

        // Use the selected club as part of the collection path
        val collectionPath = "clubs/$selectedClub/events"

        val eventMap = HashMap<String, Any>()
        eventMap["title"] = title
        eventMap["description"] = description
        eventMap["venue"] = venue
        eventMap["date"] = date
        eventMap["time"] = time
        eventMap["contact"] = contact

        // Upload image to Firestore Storage
        if (selectedImageUri != null) {
            val imageRef = storageRef.child(selectedClub.toString()).child(title.toString())
                .child("images/$title.jpg")
            val imageUploadTask = imageRef.putFile(selectedImageUri!!)

            imageUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    // Add the image URL to the eventMap
                    eventMap["imageUrl"] = downloadUri.toString()

                    // Upload PDF to Firestore Storage
                    if (selectedPdfUri != null) {
                        val pdfRef =
                            storageRef.child(selectedClub.toString()).child(title.toString())
                                .child("pdfs/$title.pdf")
                        val pdfUploadTask = pdfRef.putFile(selectedPdfUri!!)

                        pdfUploadTask.continueWithTask { pdfTask ->
                            if (!pdfTask.isSuccessful) {
                                pdfTask.exception?.let {
                                    throw it
                                }
                            }
                            return@continueWithTask pdfRef.downloadUrl
                        }.addOnCompleteListener { pdfTask ->
                            if (pdfTask.isSuccessful) {
                                val pdfDownloadUri = pdfTask.result

                                // Add the PDF URL to the eventMap
                                eventMap["pdfUrl"] = pdfDownloadUri.toString()

                                // Use the title as the document ID
                                firestore.collection(collectionPath)
                                    .document(title)  // Set the document ID to the title
                                    .set(eventMap)
                                    .addOnSuccessListener {
                                        // Document added successfully
                                        Toast.makeText(
                                            this,
                                            "Event added to Firestore",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navigate to the 'done' activity
                                        val intent = Intent(this, done::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener {
                                        // Handle errors
                                        Toast.makeText(
                                            this,
                                            "Error adding event to Firestore",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                // Handle PDF upload failure
                                Toast.makeText(this, "Error uploading PDF", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        // No PDF selected, proceed with image upload
                        // Use the title as the document ID
                        firestore.collection(collectionPath)
                            .document(title)  // Set the document ID to the title
                            .set(eventMap)
                            .addOnSuccessListener {
                                // Document added successfully
                                Toast.makeText(this, "Event added to Firestore", Toast.LENGTH_SHORT)
                                    .show()

                                // Navigate to the 'done' activity
                                val intent = Intent(this, done::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener {
                                // Handle errors
                                Toast.makeText(
                                    this,
                                    "Error adding event to Firestore",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    // Handle image upload failure
                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // No image selected, proceed without image upload
            // Upload PDF to Firestore Storage
            if (selectedPdfUri != null) {
                val pdfRef = storageRef.child(selectedClub.toString()).child(title.toString())
                    .child("pdfs/$title.pdf")
                val pdfUploadTask = pdfRef.putFile(selectedPdfUri!!)

                pdfUploadTask.continueWithTask { pdfTask ->
                    if (!pdfTask.isSuccessful) {
                        pdfTask.exception?.let {
                            throw it
                        }
                    }
                    return@continueWithTask pdfRef.downloadUrl
                }
            }
        }
    }
}
