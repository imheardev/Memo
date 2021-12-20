package com.imheardev.memo

import android.widget.Toast

/**
 * Created by wuto on 2021-12-18.
 */
fun String.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(MemoApplication.context,this,duration).show()
}
fun Int.showToast(duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(MemoApplication.context,this,duration).show()
}