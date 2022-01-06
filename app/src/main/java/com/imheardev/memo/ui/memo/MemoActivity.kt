package com.imheardev.memo.ui.memo

import android.content.Context
import android.content.Intent
import android.icu.text.RelativeDateTimeFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View.FOCUSABLE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.imheardev.memo.MainActivity
import com.imheardev.memo.MemoApplication
import com.imheardev.memo.R
import com.imheardev.memo.databinding.ActivityMemoBinding
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.dao.MemoDatabase
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.showToast
import com.imheardev.memo.util.KeywordUtil
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

class MemoActivity : BaseActivity() {

    private lateinit var binding: ActivityMemoBinding
    private lateinit var originMemoContext:String
    private lateinit var operatorType:String
    val viewModel by lazy { ViewModelProvider(this).get(SingleMemoViewModel::class.java) }

    companion object{
        const val MEMO_ID = "id"
        const val MEMO_CONTEXT = "context"
        const val OPERATOR_TYPE = "OPERATOR_TYPE"
        const val OPERATOR_ADD = "OPERATOR_ADD"
        const val OPERATOR_EDIT = "OPERATOR_EDIT"
        const val KEY_WORD = "KEY_WORD"
        const val MEMO_REMARK = "remark"

        // mainActivity中点击新增按钮
        fun actionStart(context: MainActivity) {
            val intent = Intent(context,MemoActivity::class.java)
            intent.putExtra(OPERATOR_TYPE,OPERATOR_ADD)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(intent)
        }
        // memoFragment中点击列表项
        fun actionStart(context:Context,content:String,id:Long,keyWord:String){
            val intent = Intent(context,MemoActivity::class.java)
            intent.putExtra(OPERATOR_TYPE,OPERATOR_EDIT)
            intent.putExtra(MEMO_CONTEXT,content)
            intent.putExtra(MEMO_ID,id)
            intent.putExtra(KEY_WORD,keyWord)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        operatorType = intent.getStringExtra(OPERATOR_TYPE)?:""
        originMemoContext = intent.getStringExtra(MEMO_CONTEXT)?:""
        if(OPERATOR_EDIT == operatorType){
            // 编辑模式
            val id = intent.getLongExtra(MEMO_ID,0)
            val keyword = intent.getStringExtra(KEY_WORD)?:""
            //关键字变色高亮显示
            var spanable = KeywordUtil.matcherSearchTitle(resources.getColor(R.color.blue_policy),originMemoContext,keyword)
            binding.collapsingToolbar.title = id.toString()
            binding.memoContentEdit.setText(spanable)
        }
        if(OPERATOR_ADD == operatorType){
            // 文本框默认焦点设置并弹出软键盘
            binding.memoContentEdit.postDelayed({
                binding.memoContentEdit.requestFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(binding.memoContentEdit, 0)
            }, 100)
        }
        // TODO，当点击文本框空白处时，文本框默认焦点设置并弹出软键盘
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Glide.with(this).load(R.drawable.orange).into(binding.memoImageView)
//        // TODO,观察insertLiveData变化，并显示到ui上,并将新增模式改为编辑模式
//        viewModel.memoInsertLiveData.observe(this, Observer{result ->
//            Log.d("MemoActivity","onBackPressed")
//        })

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
        if(OPERATOR_ADD == operatorType && originMemoContext != memoContent){
            val content:String = binding.memoContentEdit.text.toString()
            //修改时间 TODO need debug
            // 自定义格式化:
            val myDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val createTime = myDateTimeFormatter.format(LocalDateTime.now())
            val newMemo = Memo(content=content,createTime=createTime,updateTime = createTime)
//            viewModel.insertMemo(newMemo)
            super.onBackPressed()
            thread {
              val memoDao = MemoDatabase.getDatabase(this).memoDao()
              newMemo.id = memoDao.insertMemo(newMemo)
            val msg:String = "OPERATOR_ADD,newMemo.id:${newMemo.id}"
            Log.d("MainActivity",msg)
            }//.start()
//            finish()
        }else if(OPERATOR_EDIT == operatorType && originMemoContext != memoContent){
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