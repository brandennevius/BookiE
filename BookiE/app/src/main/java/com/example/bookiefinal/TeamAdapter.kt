package com.example.bookiefinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamAdapter (
    var teamData: ArrayList<String>,
    val clickListener: (String)->Unit ) : RecyclerView.Adapter<TeamAdapter.RecyclerViewHolder>()
{

    lateinit var dataManager: DataManager


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {

//        viewModel = activity?.run {
//            ViewModelProviders.of(this).get(DrumPlayerViewModel::class.java)
//        } ?: throw Exception("bad activity")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.teams_data, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.teamName.text = teamData[position]
        holder.bind(teamData[position], clickListener)

    }

    override fun getItemCount(): Int {
        return teamData.size
    }

    inner class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        internal var teamName: TextView = itemView.findViewById(R.id.teamsRecyclerViewData)
        fun bind(team: String, clickListener: (String) -> Unit) {
            itemView.setOnClickListener {
                clickListener(team)
            }
        }
    }

}