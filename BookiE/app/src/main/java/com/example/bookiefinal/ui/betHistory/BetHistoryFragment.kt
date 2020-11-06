package com.example.bookiefinal.ui.betHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookiefinal.*

class BetHistoryFragment : Fragment() {

    lateinit var dataManager: DataManager
    lateinit var  customAdapter : BetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataManager = activity?.run {
            ViewModelProviders.of(this).get(DataManager::class.java)
        } ?: throw Exception("bad activity")
        customAdapter = BetAdapter(dataManager.betHistory.value!!) { bet: Bet ->
            RecyclerViewItemsSelected(bet)
        }
        val root = inflater.inflate(R.layout.fragment_bet_history, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.list_recyclerview) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(context)
//        viewAdapter = RecordAdapter(ArrayList<Record>(), )
        recyclerView.layoutManager = linearLayoutManager



        recyclerView.adapter = customAdapter


        customAdapter.betData = dataManager.betHistory.value!!
        customAdapter.notifyDataSetChanged()

    }

    fun RecyclerViewItemsSelected(bet: Bet){

    }
}