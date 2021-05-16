package com.skyletto.startappfrontend.ui.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skyletto.startappfrontend.R
import com.skyletto.startappfrontend.ui.project.fragments.CreateProjectFragment

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        supportFragmentManager.beginTransaction().add(R.id.project_frame, CreateProjectFragment.newInstance()).commit()
    }
}