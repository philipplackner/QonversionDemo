package com.plcoding.qonversionprep

import android.app.Application
import com.qonversion.android.sdk.QUserProperties
import com.qonversion.android.sdk.Qonversion

class QonversionApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Qonversion.setDebugMode()
        Qonversion.launch(this, "7l19NS3boKBBeTb6LjWweXtiuZfu3BkO", false)
    }
}