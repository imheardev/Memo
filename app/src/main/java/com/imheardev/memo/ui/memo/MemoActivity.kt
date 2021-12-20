package com.imheardev.memo.ui.memo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.imheardev.memo.R
import com.imheardev.memo.databinding.ActivityMemoBinding
import com.imheardev.memo.showToast

class MemoActivity : BaseActivity() {

    private lateinit var binding: ActivityMemoBinding
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
        val content = intent.getStringExtra(MEMO_CONTEXT)?:""
        val remark = intent.getStringExtra(MEMO_REMARK)?:""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = id.toString()
        Glide.with(this).load(R.drawable.orange).into(binding.memoImageView)
        binding.memoContentEdit.setText(content)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onPause() {
//        Log.d("MemoActivity","onPause")
//        showAlertDialog()
//        super.onPause()
//    }
//    override fun onStop() {
//        Log.d("MemoActivity","onStop")
//        showAlertDialog()
//        super.onStop()
//    }
//
//    override fun onDestroy() {
//        Log.d("MemoActivity","onDestroy")
//        showAlertDialog()
//        super.onDestroy()
//    }

    override fun onBackPressed() {
        Log.d("MemoActivity","onBackPressed")
        showAlertDialog()
//        TODO 根据返回选择结果，进行下一步操作
//        super.onBackPressed()
    }

    private fun showAlertDialog(){
        var builder=AlertDialog.Builder(this)
        builder.setTitle("提示")
        builder.setMessage("是否保存更改?")
        builder.setPositiveButton("保存"){dialog,which ->
            "你选择了保存".showToast()
        }
        builder.setNegativeButton("不保存"){dialog,which ->
            "你选择了取消".showToast()
        }
        var dialog:AlertDialog = builder.create()
        if(!dialog.isShowing){
            dialog.show()
        }
//        TODO 返回选择结果，改为公共对话框模板
    }

}