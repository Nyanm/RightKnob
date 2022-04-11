package com.nyanm.rightknob

import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val dbHelper = DatabaseHelper(this)
        val database = dbHelper.openDatabase()

        search_bar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d("MainActivity", "Searching: $p0")
                    val cursor =
                        database.query("MUSIC", arrayOf("MID"), "MEME", null, null, null, "MID")
                    cursor.close()
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            }
        )
    }
}