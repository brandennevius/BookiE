package com.example.bookiefinal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import com.example.bookiefinal.DataManager
import com.example.bookiefinal.R
import kotlinx.android.synthetic.main.nav_header_main.*

class HomeFragment : Fragment() {

        private lateinit var appBarConfiguration: AppBarConfiguration
        lateinit var dataManager: DataManager



        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_home, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
//            username_button.text = dataManager.username.value
        }
}