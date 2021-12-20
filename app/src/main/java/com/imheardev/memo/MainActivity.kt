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
        }
    }
}