import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.progothon.Event
import com.example.progothon.Fetch_deatils

import com.example.progothon.R  // Replace with the actual package name
import com.squareup.picasso.Picasso

class Event_Adapter(private val context: Context, private val eventList: List<Event>) : RecyclerView.Adapter<Eventviewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Eventviewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.event_list, parent, false)
        return Eventviewholder(view)
    }

    override fun onBindViewHolder(holder: Eventviewholder, position: Int) {
        val event = eventList[position]

        // Set data to views
        Picasso.get().invalidate(event.imageUrl)

        Picasso.get()
            .load(event.imageUrl) // Assuming imageUrl is a valid URL or a file path
            .placeholder(R.drawable.loading1) // Placeholder image while loading
            .error(R.drawable.error) // Image to show if loading fails
            .into(holder.eventImage)

        holder.eventName.text = event.title

        // Set click listeners or any other logic as needed
       holder.selectedevent.setOnClickListener {
           val intent=Intent(holder.selectedevent.context, Fetch_deatils::class.java)
           intent.putExtra("key",eventList[position].title)
           intent.putExtra("clubName", eventList[position].clubName)
           holder.selectedevent.context.startActivity(intent)
       }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}
