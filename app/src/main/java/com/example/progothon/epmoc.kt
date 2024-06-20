package com.example.progothon
import Event_Adapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.firestore.FirebaseFirestore

class epmoc : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: Event_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epmoc)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.rvepmoc)  // Make sure to use the correct RecyclerView ID from your layout
        recyclerView.layoutManager = LinearLayoutManager(this)

        val selectedClub= intent.getStringExtra("key")
        fetchDataAndDisplay(selectedClub!!)

    }

    private fun fetchDataAndDisplay(selectedClub: String) {
        // Assume you have a collection named "clubs" in Firestore
        firestore.collection("clubs")
            .document(selectedClub)
            .collection("events")
            .get()
            .addOnSuccessListener { result ->
                val eventList = mutableListOf<Event>()
                for (document in result) {
                    // Access the data from the document
                    val title = document.getString("title").toString()
                    val imageResId = document.getString("imageUrl") ?: ""
                    val event = Event(imageResId, title,"Epmoc")
                    Log.d("Event_Adapter", "Title: $title, Image URL: $imageResId")

                    eventList.add(event)
                }

                // Initialize the Event_Adapter with the data and set it to the RecyclerView
                eventAdapter = Event_Adapter(this, eventList)
                recyclerView.adapter = eventAdapter
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Toast.makeText(this, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
