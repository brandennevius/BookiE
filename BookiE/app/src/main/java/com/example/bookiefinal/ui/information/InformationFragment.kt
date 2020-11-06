package com.example.bookiefinal.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bookiefinal.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InformationFragment : Fragment() {

    private lateinit var informationViewModel: InformationViewModel

    var team: String? = null
    var league: String? = null
    var currenth2hOdds: String? = null
    var currentPointSpread: String? = null
    var apiListOdds = ArrayList<Game>()
    var apiListInformation = ArrayList<TeamInformation>()
    lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataManager = activity?.run {
            ViewModelProviders.of(this).get(DataManager::class.java)
        } ?: throw Exception("bad activity")

        team = arguments?.getString("whichTeam")
        league = arguments?.getString("whichLeague")
        informationViewModel =
            ViewModelProviders.of(this).get(InformationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_information, container, false)

        var teamName = root.findViewById<TextView>(R.id.TeamName)
        var HomeH2H = root.findViewById<TextView>(R.id.h2hBetsHome)
        var AwayH2H = root.findViewById<TextView>(R.id.h2hBetsAway)
        var HomePointSpread = root.findViewById<TextView>(R.id.pointSpreadHome)
        var AwayPointSpread = root.findViewById<TextView>(R.id.pointSpreadAway)
        var score = root.findViewById<TextView>(R.id.Score)

        when(league){
            "mlb" -> apiListOdds = dataManager.mlbGames.value!!
            "nba" -> apiListOdds = dataManager.nbaGames.value!!
            "nhl" -> apiListOdds = dataManager.nhlGames.value!!
            "nfl" -> apiListOdds = dataManager.nflGames.value!!
        }

        when(league){
            "mlb" -> apiListInformation = dataManager.baseballTeamInformation.value!!
            "nba" -> apiListInformation = dataManager.basketballTeamInformation.value!!
            "nhl" -> apiListInformation = dataManager.hockeyTeamInformation.value!!
            "nfl" -> apiListInformation = dataManager.footballTeamInformation.value!!
        }


        for( game in apiListOdds){
            if (game.home_team == team){
                currentPointSpread = game.pointSpread
                currenth2hOdds = game.odds


                var h2h = game.odds!!.split(",")
                var h2hHome = h2h[0]
                h2hHome = h2hHome.replace("[", "")
                HomeH2H.text = "HOME: " + h2hHome

                var h2hAway = h2h[1]
                h2hAway = h2hAway.replace("]", "")
                AwayH2H.text = "AWAY: " + h2hAway

                var ps = game.pointSpread!!.split(",")
                var psHome = ps[0]
                psHome = psHome.replace("\"", "")
                psHome = psHome.replace("[", "")

                HomePointSpread.text = "HOME: " + psHome

                var psAway = ps[1]
                psAway = psAway.replace("\"", "")
                psAway = psAway.replace("]", "")
                AwayPointSpread.text = "AWAY: " + psAway
            }
        }
        teamName.text = team


        /*
        Setting the data for each team
        */
        for(teamData in apiListInformation){
            //The full team data team name includes the city and we only need the mascot
//            var splitTeamName = teamData.TeamName!!.split(" ")
            var splitTeamName = team!!.split(" ")
            if(league == "Football"){
                if(splitTeamName[1] == teamData.one){
                    score.text = teamData.two.toString()
                }
            }
            if(league != "Football"){
                score.text = teamData.two.toString()
            }

        }


        return root
    }

    fun saveBet( bet: Bet){
        dataManager.saveBet(bet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var h2hButton = view.findViewById<Button>(R.id.h2hBetButton)
        var pointSpreadButton = view.findViewById<Button>(R.id.pointSpreadBetButton)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())


        pointSpreadButton.setOnClickListener{
            var currentBet = Bet()
            currentBet.homeTeam = team
            currentBet.date = currentDate
            currentBet.odd = currentPointSpread
            saveBet(currentBet)
        }
        h2hButton.setOnClickListener {
            var currentBet = Bet()
//          need to change this
            currentBet.homeTeam = team
            currentBet.date = currentDate
            currentBet.odd = currenth2hOdds
            saveBet(currentBet)
        }

    }
}