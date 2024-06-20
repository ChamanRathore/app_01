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
