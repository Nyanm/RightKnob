package com.nyanm.rightknob

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val dbHelper = DatabaseHelper(this)
        val database = dbHelper.openDatabase()
        recycle.layoutManager = LinearLayoutManager(this)

        search_bar.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d("MainActivity", "Searching: $p0")

                    // empty input
                    if (p0.isNullOrBlank()) return false

                    // uncanny input
                    if (p0.uppercase(Locale.getDefault()) == "NYANM") {
                        startActivity(Intent(".ACTION_UNCANNY"))
                        return true
                    }

                    // search for mid
                    val sqlQuery = "SELECT MID FROM MUSIC WHERE MEME LIKE '%${p0}%'"
                    val resList = mutableListOf<Int>()
                    val cursorMid = database.rawQuery(sqlQuery, null)

                    if (cursorMid.moveToFirst()) {
                        do {
                            resList.add(cursorMid.getInt(cursorMid.getColumnIndex("MID")))
                        } while (cursorMid.moveToNext())
                    }
                    cursorMid.close()
                    Log.d("MainActivity", "Result(s): $resList")

                    // search result is empty
                    if (resList.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Search failed.", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }

                    // construct adapter for recycle view
                    Toast.makeText(
                        this@MainActivity,
                        "${resList.size} result(s) found.",
                        Toast.LENGTH_SHORT
                    ).show()
                    var musicName: String
                    var musicArtist: String
                    var musicYomigana: String
                    var musicDate: Int
                    var musicVersion: Int
                    var musicInfVer: Int
                    var musicNOV: Int
                    var musicADV: Int
                    var musicEXH: Int
                    var musicINF: Int
                    var musicMXM: Int

                    val dataList = ArrayList<SingleMusic>()

                    for (mid in resList) {
                        val dataQuery = "SELECT * FROM MUSIC WHERE MID = $mid"
                        val curSG = database.rawQuery(dataQuery, null)
                        if (curSG.moveToFirst()) {
                            musicName = curSG.getString(curSG.getColumnIndex("NAME"))
                            musicArtist = curSG.getString(curSG.getColumnIndex("ARTIST"))
                            musicYomigana = curSG.getString(curSG.getColumnIndex("NAME_YO"))
                            musicDate = curSG.getInt(curSG.getColumnIndex("DATE"))
                            musicVersion = curSG.getInt(curSG.getColumnIndex("VERSION"))
                            musicInfVer = curSG.getInt(curSG.getColumnIndex("INF_VER"))
                            musicNOV = curSG.getInt(curSG.getColumnIndex("NOV_LV"))
                            musicADV = curSG.getInt(curSG.getColumnIndex("ADV_LV"))
                            musicEXH = curSG.getInt(curSG.getColumnIndex("EXH_LV"))
                            musicINF = curSG.getInt(curSG.getColumnIndex("INF_LV"))
                            musicMXM = curSG.getInt(curSG.getColumnIndex("MXM_LV"))

                            val curMusic = SingleMusic(
                                mid,
                                musicName,
                                musicArtist,
                                musicYomigana,
                                musicDate,
                                musicVersion,
                                musicInfVer,
                                musicNOV,
                                musicADV,
                                musicEXH,
                                musicINF,
                                musicMXM
                            )
                            Log.d("MainActivity", "Collecting data: '$musicName' by '$musicArtist'")
                            dataList.add(curMusic)
                        }
                        curSG.close()
                    }
                    val adapter = MusicBoxAdapter(dataList)
                    recycle.adapter = adapter
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("MainActivity", "Menu ${item.itemId} selected.")
        when (item.itemId) {
            R.id.about -> startActivity(Intent(".ACTION_ABOUT"))
        }
        return true
    }
}