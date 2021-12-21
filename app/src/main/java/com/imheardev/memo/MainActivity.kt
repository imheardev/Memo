package com.imheardev.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.imheardev.memo.databinding.ActivityMainBinding
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.dao.MemoDatabase
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.ui.memo.BaseActivity
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addMemoBtn.setOnClickListener {
            "you clicked addMemoBtn".showToast()
            //TODO add data
            addData()
        }
    }

    private fun addData(){
        val memoDao = MemoDatabase.getDatabase(this).memoDao()
        val memo1 = Memo("内容1。。。。。。。。。。。。。。。。。。。。。。。。",
        0,"备注1。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。",
        "","","",0,1)
        val memo2 = Memo("内容2。。。。。。。。。。。。。。。。。。。。。。。。",
        0,"备注2。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。",
        "","","",1,0)
        thread {
            memo1.id = memoDao.insertMemo(memo1)
            memo2.id = memoDao.insertMemo(memo2)
            val msg:String = "memo1.id:${memo1.id},memo2.id:${memo2.id}"
            Log.d("MainActivity",msg)
        }
    }
}