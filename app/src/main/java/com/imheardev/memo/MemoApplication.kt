package com.imheardev.memo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created by wuto on 2021-12-18.
 */
class MemoApplication: Application() {

    companion object{
        const val TOKEN = "oJzStw12bp92ypAE" // TODO 应放到配置文件中
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}