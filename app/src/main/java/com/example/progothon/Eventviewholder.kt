import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.progothon.R  // Replace with the actual package name

class Eventviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val eventImage: ImageView = itemView.findViewById(R.id.ivevent)
    val eventName: TextView = itemView.findViewById(R.id.tveventname)
    val selectedevent: CardView = itemView.findViewById(R.id.cvevent)

}
