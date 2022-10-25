package com.pharmacy.core.theme

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.pharmacy.core.component.Component

object ThemeComponent : Component {
    override fun init(application: Application) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}