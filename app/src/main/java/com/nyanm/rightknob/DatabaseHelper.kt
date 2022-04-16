package com.nyanm.rightknob

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException


class DatabaseHelper(private val context: Context, private val versionCode: Int) {

    companion object {
        const val dbName = "music.db"
        const val spsName = "config"
        const val cfgVer = 1
    }

    private val dbFile = context.getDatabasePath(dbName)

    @SuppressLint("Range")
    fun openDatabase(): SQLiteDatabase {
        Log.d("DBHelper", "dbFile=${dbFile}")

        if (!dbFile.exists()) return update("data/databases/music.db is missing.")
        else {
            val sps: SharedPreferences = context.getSharedPreferences(spsName, Context.MODE_PRIVATE)
            val cfgVerTemp = sps.getInt("configVer", 0)
            Log.v("DBHelper", "configVer: $cfgVerTemp")

            return if (cfgVerTemp == 0 || cfgVerTemp < cfgVer) update("Config is outdated.")
            else {
                val versionCodeTemp = sps.getInt("versionCode", 0)
                Log.v("DBHelper", "versionCodeTemp: $versionCodeTemp")

                if (versionCodeTemp == 0 || versionCodeTemp < versionCode) update("Database is outdated.")
                else {
                    Log.d("DBHelper", "Database load successfully.")
                    SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY)
                }
            }
        }
    }

    private fun update(reason: String): SQLiteDatabase {
        Log.i("DBHelper", reason)

        try {
            val `is` = context.resources.openRawResource(R.raw.music)
            val os = FileOutputStream(dbFile)
            val buffer = ByteArray(8192)

            while (`is`.read(buffer) > 0) os.write(buffer)

            os.flush()
            os.close()
            `is`.close()
            Log.d("DBHelper", "Database copied successfully.")

        } catch (e: IOException) {
            throw RuntimeException("Raw database is missing", e)
        }

        val sps: SharedPreferences = context.getSharedPreferences(spsName, Context.MODE_PRIVATE)
        val editor = sps.edit()
        editor.clear()

        when (cfgVer) {
            1 -> {
                editor.putInt("configVer", cfgVer)
                editor.putInt("versionCode", versionCode)
            }
            else -> throw RuntimeException("Unsupported config version.")
        }

        editor.apply()
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY)
    }

}

// with the great help of https://www.javaer101.com/article/23538222.html