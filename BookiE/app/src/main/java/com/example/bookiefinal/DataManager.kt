package com.example.bookiefinal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class DataManager(application: Application) : AndroidViewModel(application)  {

    private val apiURL = "https://api.the-odds-api.com"
    private val apiKey = "7788da721b00f1a483acc5d9d3b0d301"

    var nbaGames = MutableLiveData<ArrayList<Game>>()
    var nflGames = MutableLiveData<ArrayList<Game>>()
    var mlbGames = MutableLiveData<ArrayList<Game>>()
    var nhlGames = MutableLiveData<ArrayList<Game>>()
    var betHistory = MutableLiveData<ArrayList<Bet>>()
    var username = MutableLiveData<String>()
    var footballTeamInformation = MutableLiveData<ArrayList<TeamInformation>>()
    var basketballTeamInformation = MutableLiveData<ArrayList<TeamInformation>>()
    var baseballTeamInformation = MutableLiveData<ArrayList<TeamInformation>>()
    var hockeyTeamInformation = MutableLiveData<ArrayList<TeamInformation>>()
    var nbaTeamData = MutableLiveData<ArrayList<TeamInformation>>()

    init {
        betHistory.value = ArrayList()
        footballTeamInformation.value = ArrayList()
        basketballTeamInformation.value = ArrayList()
        baseballTeamInformation.value = ArrayList()
        hockeyTeamInformation.value = ArrayList()
        nbaTeamData.value = ArrayList()
    }

    interface SportService {
        @GET("/v3/sports/?")
        fun allSports(
            @Query("apiKey") appid: String
        ): Call<ResponseBody>

        @GET("/v3/odds/?sport=basketball&region=us&mkt=spreads&apiKey=7788da721b00f1a483acc5d9d3b0d301")
        fun getBasketballData(
        ): Call<ResponseBody>

        @GET("/v3/odds/?sport=baseball_mlb&region=us&mkt=spreads&apiKey=7788da721b00f1a483acc5d9d3b0d301")
        fun getBaseballData(
        ): Call<ResponseBody>

        @GET("/v3/odds/?sport=hockey&region=us&mkt=spreads&apiKey=7788da721b00f1a483acc5d9d3b0d301")
        fun getHockeyData(
        ): Call<ResponseBody>

        @GET("/v3/odds/?sport=americanfootball_nfl&region=us&mkt=spreads&apiKey=7788da721b00f1a483acc5d9d3b0d301")
        fun getFootballData(
        ): Call<ResponseBody>

        @GET("https://api.sportsdata.io/v3/nfl/scores/json/TeamSeasonStats/season=2019REG?key=c4ee7ca2608c4e849f9d64a79f6ba21f")
        fun getFootballTeamData(
        ): Call<ResponseBody>

        @GET("https://api.sportsdata.io/v3/nba/scores/json/TeamSeasonStats/season=2019REG?key=801988b71ae049359ff4441796c81a80")
        fun getBasketballTeamData(
        ): Call<ResponseBody>

        @GET("https://api.sportsdata.io/v3/mlb/scores/json/TeamSeasonStats/season=2019?key=f01a08f978764ead816428d2deb47520")
        fun getBaseballTeamData(
        ): Call<ResponseBody>

        @GET("https://api.sportsdata.io/v3/nhl/scores/json/TeamSeasonStats/season=2020?key=fdc1b15a662e45edb2a95fe13aab112d")
        fun getHockeyTeamData(
        ): Call<ResponseBody>

    }

    fun getTheOddsApiInfo() {
        val retrofit = Retrofit.Builder().baseUrl(apiURL).build()
        val service = retrofit.create(SportService::class.java)

        val callSports = service.allSports(apiKey)
        val callGetBasketballData = service.getBasketballData()
        val callGetBaseballData = service.getBaseballData()
        val callGetFootballData = service.getFootballData()
        val callGetHockeyData = service.getHockeyData()
        val callGetFootballTeamData = service.getFootballTeamData()
        val callGetBasketballTeamData = service.getBasketballTeamData()
        val callGetBaseballTeamData = service.getBaseballTeamData()
        val callGetHockeyTeamData = service.getHockeyTeamData()


//        callSports.enqueue(SportCallback())
        callGetBasketballData.enqueue((SportCallback("basketball")))
        callGetBaseballData.enqueue((SportCallback("baseball")))
        callGetFootballData.enqueue((SportCallback("football")))
        callGetHockeyData.enqueue((SportCallback("hockey")))
        callGetFootballTeamData.enqueue(SportCallback("footballTeams"))
        callGetBasketballTeamData.enqueue(SportCallback("basketballTeams"))
        callGetBaseballTeamData.enqueue(SportCallback("baseballTeams"))
        callGetHockeyTeamData.enqueue(SportCallback("hockeyTeams"))

    }

    var sportList = ArrayList<Game>()
    val basketballList = ArrayList<Game>()
    val footballList = ArrayList<Game>()
    val hockeyList = ArrayList<Game>()
    val baseballList = ArrayList<Game>()


    fun parseTeams(json: String, sport: String){
        var jsonArray = JSONArray(json)
        if(sport == "footballTeams") {
            for (i in 0 until jsonArray.length()) {
                val dataDetail = jsonArray.getJSONObject(i)
                var teamData = TeamInformation()
                teamData.one = dataDetail.getString("TeamName")
                teamData.two = dataDetail.getString("Score")
                teamData.three = dataDetail.getString("OpponentScore")
                teamData.four = dataDetail.getString("OffensiveYards")
                teamData.five = dataDetail.getString("Touchdowns")
                teamData.six = dataDetail.getString("PasserRating")
                footballTeamInformation.value!!.add(teamData)
            }
        }else if(sport == "basketballTeams"){
            for(i in 0 until jsonArray.length()){
                val dataDetail = jsonArray.getJSONObject(i)
                var teamData = TeamInformation()
                teamData.one = dataDetail.getString("Name")
                teamData.two = dataDetail.getString("Wins")
                teamData.three = dataDetail.getString("Losses")
                teamData.four = dataDetail.getString("FieldGoalsPercentage")
                teamData.five = dataDetail.getString("ThreePointersPercentage")
                teamData.six = dataDetail.getString("Turnovers")
                basketballTeamInformation.value!!.add(teamData)
            }
            var test = "yur"
        }else if(sport == "baseballTeams"){
            for(i in 0 until jsonArray.length()){
                val dataDetail = jsonArray.getJSONObject(i)
                var teamData = TeamInformation()
                teamData.one = dataDetail.getString("Name")
                teamData.two = dataDetail.getString("Wins")
                teamData.three = dataDetail.getString("Losses")
                teamData.four = dataDetail.getString("StolenBases")
                teamData.five = dataDetail.getString("HomeRuns")
                teamData.six = dataDetail.getString("PitchingGroundOuts")
                baseballTeamInformation.value!!.add(teamData)
            }
        }else if (sport == "hockeyTeams"){
            for(i in 0 until jsonArray.length()){
                val dataDetail = jsonArray.getJSONObject(i)
                var teamData = TeamInformation()
                teamData.one = dataDetail.getString("Name")
                teamData.two = dataDetail.getString("Wins")
                teamData.three = dataDetail.getString("Losses")
                teamData.four = dataDetail.getString("Goals")
                teamData.five = dataDetail.getString("PenaltyMinutes")
                teamData.six = dataDetail.getString("FaceoffsWon")
                baseballTeamInformation.value!!.add(teamData)
            }
        }

    }


    var siteKey = String()
    var odds = String()
    var pointSpread = String()
    fun addSports(json: String, sport: String){
        val data = JSONObject(json)
        val dataArray = data.getJSONArray("data")

        if (dataArray.length() == 0 ){
            return
        }

        /*
        access data objects
        */
        for ( i in 0 until dataArray.length()){
            val dataDetail = dataArray.getJSONObject(i)
            val sitesArray = dataDetail.getJSONArray("sites")
            /*
            access site objects which contains the odds
            */
            for ( j in 0 until 1){
                if (sitesArray.length() == 0 ){
                    break
                }
                val sitesDetail = sitesArray.getJSONObject(j)
                siteKey = sitesDetail.getString("site_key")
                val oddsArray = sitesDetail.getJSONObject("odds")
                /*
                access odds objects which contains the odds and point spread
                */
                val spreadsArray = oddsArray.getJSONObject("spreads")
                odds = spreadsArray.getString("odds")
                pointSpread = spreadsArray.getString("points")
            }
            val homeTeam = dataDetail.getString("home_team")

            /*
            determine which sport we are looking at and add to corresponding list
            */
            when (sport){
                "basketball" -> basketballList.add(Game("", homeTeam, "", siteKey, odds, pointSpread))
                "football" -> footballList.add(Game("", homeTeam, "", siteKey, odds, pointSpread))
                "hockey" -> hockeyList.add(Game("", homeTeam, "", siteKey, odds, pointSpread))
                "baseball" -> baseballList.add(Game("", homeTeam, "", siteKey, odds, pointSpread))
            }
        }
        nbaGames.value = basketballList
        nflGames.value = footballList
        mlbGames.value = baseballList
        nhlGames.value = hockeyList
    }

    fun saveBet ( bet: Bet){
        betHistory.value?.add(bet)
    }

    inner class SportCallback(sport: String): Callback<ResponseBody> {

        var whatSport = sport

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful){
                response.body()?.let {
                    if(whatSport == "footballTeams" || whatSport == "basketballTeams" || whatSport == "baseballTeams" || whatSport == "hockeyTeams"){
                        parseTeams(it.string(), whatSport)
                    }else {
                        addSports(it.string(), whatSport)
                    }
                }
            }
        }

    }


}