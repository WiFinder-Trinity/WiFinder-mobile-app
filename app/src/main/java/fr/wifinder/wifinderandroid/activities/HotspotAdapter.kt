package fr.wifinder.wifinderandroid.activities

import android.net.wifi.ScanResult
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.wifinder.wifinderandroid.R

class HotspotAdapter (private val hotspots: List<ScanResult>) : RecyclerView.Adapter<HotspotAdapter.MyViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HotspotAdapter.MyViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_hotspots_list, parent, false)
        return MyViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pair = Pair(hotspots[position].SSID, hotspots[position].BSSID)
        holder.display(pair)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = hotspots.size
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.name)
        private val description: TextView = itemView.findViewById(R.id.description)
        private var currentPair: Pair<String, String>? = null

        init {
            itemView.setOnClickListener {
                AlertDialog.Builder(itemView.getContext())
                        .setTitle(currentPair!!.first)
                        .setMessage(currentPair!!.second)
                        .show()
            }
        }

        fun display(pair: Pair<String, String>) {
            currentPair = pair
            name.text = pair.first
            description.text = pair.second
        }
    }
}