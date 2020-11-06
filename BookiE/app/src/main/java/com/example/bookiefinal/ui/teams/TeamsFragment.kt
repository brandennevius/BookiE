package com.example.bookiefinal.ui.teams

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookiefinal.DataManager
import com.example.bookiefinal.R
import com.example.bookiefinal.TeamAdapter
import java.io.BufferedReader
import com.opencsv.CSVReader
import java.io.IOException
import java.nio.Buffer

class TeamsFragment : Fragment() {

    lateinit var dataManager: DataManager
    lateinit var  customAdapter : TeamAdapter

    var league: String? = ""
    val mlbList = ArrayList<String>()
    var nflList = ArrayList<String>()
    var fileContent: BufferedReader? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*
        league decides which sports league button was clicked
        */
        league = arguments?.getString("whichLeague")

        /*
        this is going to determine which csv file to read
        */
        when(league){
            "mlb" -> fileContent = resources.openRawResource(R.raw.baseball).bufferedReader()
            "nba" -> fileContent = resources.openRawResource(R.raw.basketball).bufferedReader()
            "nhl" -> fileContent = resources.openRawResource(R.raw.hockey).bufferedReader()
            "nfl" -> fileContent = resources.openRawResource(R.raw.football).bufferedReader()
        }

        var fileReader: BufferedReader? = null
        var csvReader: CSVReader? = null

        try {
            csvReader = CSVReader(fileContent)
            var record: Array<String>?
            csvReader.readNext() // skip Header
            record = csvReader.readNext()
            while (record != null) {

                nflList.add(record[0])
                record = csvReader.readNext()
            }
            csvReader.close()

        }catch (e: IOException) {
            println("Closing fileReader/csvParser Error!")
            e.printStackTrace()
        }

        dataManager = activity?.run {
            ViewModelProviders.of(this).get(DataManager::class.java)
        } ?: throw Exception("bad activity")
        customAdapter = TeamAdapter(nflList) { team: String ->
            RecyclerViewItemsSelected(team)
        }

        var view = inflater.inflate(R.layout.fragment_teams, container, false)

        view.setBackgroundColor(Color.WHITE)

        var leagueTitle = view.findViewById<TextView>(R.id.leagueName)
        var testTextView = view.findViewById<TextView>(R.id.teamsTestTextView)



        when(league){
            "mlb" -> leagueTitle.text = league
            "nba" -> leagueTitle.text = league
            "nhl" -> leagueTitle.text = league
            "nfl" -> leagueTitle.text = league
        }
//        testTextView.text = dataManager.nflGames.value!!.size.toString()



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById(R.id.recycler_team_list) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(context)
//        viewAdapter = RecordAdapter(ArrayList<Record>(), )
        recyclerView.layoutManager = linearLayoutManager



        recyclerView.adapter = customAdapter


        customAdapter.teamData = nflList
        customAdapter.notifyDataSetChanged()


    }

    fun RecyclerViewItemsSelected(team: String){
        var bundle = Bundle()

        bundle.putString("whichTeam", team)
        bundle.putString("whichLeague", league)
        findNavController().navigate(R.id.action_nav_teams_to_nav_information, bundle)
    }
}