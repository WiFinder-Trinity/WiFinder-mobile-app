package fr.wifinder.wifinderandroid.activities

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import fr.wifinder_trinity.wifinderandroid.R

class HotspotAdapter : RecyclerView.Adapter<HotspotAdapter.MyViewHolder>() {

    private val characters = arrayOf(
            Pair("Lyra Belacqua", "Brave, curious, and crafty, she has been prophesied by the witches to help the balance of life"),
            Pair("Pantalaimon", "Lyra's daemon, nicknamed Pan."),
            Pair("Roger Parslow", "Lyra's friends"),
            Pair("Lord Asriel", "Lyra's uncle"),
            Pair("Marisa Coulter", "Intelligent and beautiful, but extremely ruthless and callous."),
            Pair("Iorek Byrnison", "Armoured bear, Rightful king of the panserbjørne. Reduced to a slave of the human village Trollesund."),
            Pair("Serafina Pekkala", "Witch who closely follows Lyra on her travels."),
            Pair("Lee Scoresby", "Texan aeronaut who transports Lyra in his balloon. Good friend with Iorek Byrnison."),
            Pair("Ma Costa", "Gyptian woman whose son, Billy Costa is abducted by the \"Gobblers\"."),
            Pair("John Faa", "The King of all gyptian people.")
    )

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
        val pair = characters.get(position)
        holder.display(pair)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = characters.size

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