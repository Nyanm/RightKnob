package com.nyanm.rightknob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.about_activity.*

class AboutActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)

        // val metaVer = intent.getIntExtra("metaVer", 0)
        val gameVer = intent.getIntExtra("gameVer", 0)
        val fixVer = intent.getIntExtra("fixVer", 0)
        val pkgVerName = intent.getStringExtra("pkgVerName")

        about_ver.text = String.format(getString(R.string.about_ver), pkgVerName)
        about_db_ver.text = String.format(getString(R.string.about_db_ver), gameVer, fixVer)
    }
}