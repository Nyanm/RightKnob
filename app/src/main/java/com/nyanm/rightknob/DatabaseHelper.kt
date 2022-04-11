package com.nyanm.rightknob

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException


class DatabaseHelper(private val context: Context) {

    companion object {
        const val dbName = "music.db"
    }

    fun openDatabase(): SQLiteDatabase {

        val dbFile = context.getDatabasePath(dbName)
        Log.d("DBHelper", "dbFile=${dbFile}")

        if (!dbFile.exists()) {
            Log.i("DBHelper", "data/databases/music.db is missing.")
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
        } else Log.d("DBHelper", "Database is available.")
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }
}

// with the great help of https://www.javaer101.com/article/23538222.html