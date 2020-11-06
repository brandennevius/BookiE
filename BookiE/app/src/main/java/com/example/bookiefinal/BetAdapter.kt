package com.example.bookiefinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BetAdapter (
    var betData: ArrayList<Bet>,
    val clickListener: (Bet)->Unit ) : RecyclerView.Adapter<BetAdapter.RecyclerViewHolder>()
{

    lateinit var dataManager: DataManager


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {

//        viewModel = activity?.run {
//            ViewModelProviders.of(this).get(DrumPlayerViewModel::class.java)
//        } ?: throw Exception("bad activity")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.bet_data, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.teamName.text = betData[position].homeTeam
        holder.date.text = betData[position].date
        holder.odd.text = betData[position].odd
//        holder.bind(betData[position], clickListener)

    }

    override fun getItemCount(): Int {
        return betData.size
    }

    inner class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        internal var teamName: TextView = itemView.findViewById(R.id.betHistoryHomeTeam)
        internal var date: TextView = itemView.findViewById(R.id.betHistoryDate)
        internal var odd: TextView = itemView.findViewById(R.id.betHistoryOdd)
        fun bind(bet: Bet, clickListener: (String) -> Unit) {
            itemView.setOnClickListener {
                clickListener(bet)
            }
        }
    }

}