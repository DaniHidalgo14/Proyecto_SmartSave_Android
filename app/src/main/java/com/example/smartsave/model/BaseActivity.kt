package com.example.smartsave.model

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.smartsave.controller.LocaleHelper

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase))
    }
}