package com.imheardev.memo.ui.memo

import android.content.Context
import android.content.Intent
import android.icu.text.RelativeDateTimeFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.imheardev.memo.MemoApplication
import com.imheardev.memo.R
import com.imheardev.memo.databinding.ActivityMemoBinding
import com.imheardev.memo.logic.dao.MemoDatabase
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.showToast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

class MemoActivity : BaseActivity() {

    private lateinit var binding: ActivityMemoBinding
    private lateinit var originMemoContext:String

    companion object{
        const val MEMO_ID = "id"
        const val MEMO_CONTEXT = "context"
        const val MEMO_REMARK = "remark"

        fun actionStart(context:Context,content:String,id:Long){
            val intent = Intent(context,MemoActivity::class.java)
            intent.putExtra(MEMO_CONTEXT,content)
            intent.putExtra(MEMO_ID,id)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getLongExtra(MEMO_ID,0)
        originMemoContext = intent.getStringExtra(MEMO_CONTEXT)?:""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = id.toString()
        Glide.with(this).load(R.drawable.orange).into(binding.memoImageView)
        binding.memoContentEdit.setText(originMemoContext)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                //拦截点击返回箭头事件
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        Log.d("MemoActivity","onBackPressed")
        //如果已经修改，则提示是否保存更改
        val memoContent = binding.memoContentEdit.text.toString()
        if(originMemoContext != memoContent){
            showExitDialog()
        }else{
            super.onBackPressed()
        }
    }

    //        TODO 返回选择结果，改为公共对话框模板,单例模式?
    private fun showExitDialog(){
        var builder=AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("是否保存更改?")
        builder.setPositiveButton("保存"){dialog,which ->
            val id:Long = intent.getLongExtra(MEMO_ID,0)
            val content:String = binding.memoContentEdit.text.toString()
            //修改时间 TODO need debug
            // 自定义格式化:
            val myDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val updateTime = myDateTimeFormatter.format(LocalDateTime.now())
            val updateMemo = Memo(content=content,updateTime=updateTime)
            updateMemo.id = id
            updateData(updateMemo)
            ActivityCollector.removeActivity(this)
            finish()
        }
        builder.setNegativeButton("不保存"){dialog,which ->
            ActivityCollector.removeActivity(this)
            finish()
        }
        var dialog:AlertDialog = builder.create()
        if(!dialog.isShowing){
            dialog.show()
        }
    }

    private fun updateData(memo:Memo){
        val memoDao = MemoDatabase.getDatabase(this).memoDao()
        thread {
            memoDao.updateMemo(memo)
            val msg:String = "updateData:memo1.id:${memo.id}"
            Log.d("MemoActivity",msg)
        }
    }


}