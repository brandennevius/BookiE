package com.example.bookiefinal.ui.leagues

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.bookiefinal.R

class LeaguesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_leagues, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val intent = Intent(getActivity(), LeaguesActivity::class.java)
//        getActivity()!!.startActivity(intent)



        var nflButton = view.findViewById<ImageView>(R.id.NFLButton)
        var mlbButton = view.findViewById<ImageView>(R.id.MLBButton)
        var nbaButton = view.findViewById<ImageView>(R.id.NBAButton)
        var nhlButton = view.findViewById<ImageView>(R.id.NHLButton)

        var bundle = Bundle()

        nflButton.setOnClickListener{
            bundle.putString("whichLeague", "nfl")
            findNavController().navigate(R.id.action_nav_leagues_to_nav_teams, bundle)
        }
        mlbButton.setOnClickListener{
            bundle.putString("whichLeague", "mlb")
            findNavController().navigate(R.id.action_nav_leagues_to_nav_teams, bundle)
        }
        nbaButton.setOnClickListener{
            bundle.putString("whichLeague", "nba")
            findNavController().navigate(R.id.action_nav_leagues_to_nav_teams, bundle)
        }
        nhlButton.setOnClickListener{
            bundle.putString("whichLeague", "nhl")
            findNavController().navigate(R.id.action_nav_leagues_to_nav_teams, bundle)
        }
    }

}