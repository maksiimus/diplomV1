package com.example.diplomv1

import android.app.Application
import com.example.diplomv1.lms.LMSRepository

class App : Application() {
    override fun onTerminate() {
        super.onTerminate()
        LMSRepository.clear()
    }
}
