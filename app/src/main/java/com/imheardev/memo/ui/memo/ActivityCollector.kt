package com.imheardev.memo.ui.memo

import android.app.Activity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by wuto on 2021-12-20.
 */
object ActivityCollector {
    private val activities = ArrayList<Activity>()
    val swipeRefresh: SwipeRefreshLayout? = null
    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    fun finishAll(){
        for (activity in activities){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}