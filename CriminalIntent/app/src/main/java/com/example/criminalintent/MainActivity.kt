package com.example.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragment = CrimeDetailFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()

        setContentView(R.layout.activity_main)
    }
}