package com.nyanm.rightknob

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    // initialize metadata of database
    private var metaVer: Int = 0
    private var gameVer: Int = 0
    private var fixVer: Int = 0

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val dbHelper = DatabaseHelper(this, packageManager.getPackageInfo(packageName, 0).versionCode)
        val database = dbHelper.openDatabase()

        val metaQuery = "SELECT * FROM METADATA"
        val cursorMeta: Cursor = database.rawQuery(metaQuery, null)
        if (cursorMeta.moveToFirst()) {
            metaVer = cursorMeta.getInt(cursorMeta.getColumnIndex("METADATA_VER"))
            gameVer = cursorMeta.getInt(cursorMeta.getColumnIndex("SDVX_VER"))
            fixVer = cursorMeta.getInt(cursorMeta.getColumnIndex("FIX_VER"))
        }
        cursorMeta.close()

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
                    val searchQuery = "SELECT MID FROM MUSIC WHERE MEME LIKE '%${p0}%'"
                    val resList = mutableListOf<Int>()
                    val cursorMid = database.rawQuery(searchQuery, null)

                    if (cursorMid.moveToFirst()) {
                        do {
                            resList.add(cursorMid.getInt(cursorMid.getColumnIndex("MID")))
                        } while (cursorMid.moveToNext())
                    }
                    cursorMid.close()
                    Log.d("MainActivity", "Result(s): $resList")

                    // search result is empty
                    if (resList.isEmpty()) {
                        Toast.makeText(this@MainActivity, "No result found", Toast.LENGTH_SHORT)
                            .show()
                        return true
                    }

                    // construct adapter for recycle view
                    Toast.makeText(
                        this@MainActivity,
                        "${resList.size} result(s) found.",
                        Toast.LENGTH_SHORT
                    ).show()
                    var musicJKRes: Int
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

                            musicJKRes = resources.getIdentifier("s_$mid", "drawable", packageName)
                            if (musicJKRes == 0) musicJKRes = R.drawable.s_dummy

                            val curMusic = SingleMusic(
                                musicJKRes,
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
                            Log.d(
                                "MainActivity",
                                "Collecting data: '$musicName' by '$musicArtist', jacket: $musicJKRes"
                            )
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
            R.id.about -> {
                val intentAbout = Intent(".ACTION_ABOUT")

                intentAbout.putExtra("metaVer", metaVer)
                intentAbout.putExtra("gameVer", gameVer)
                intentAbout.putExtra("fixVer", fixVer)
                intentAbout.putExtra("pkgVerName", packageManager.getPackageInfo(packageName, 0).versionName)
                startActivity(intentAbout)
            }
        }
        return true
    }
}