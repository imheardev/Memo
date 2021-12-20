package com.imheardev.memo.ui.memo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.imheardev.memo.MemoApplication.Companion.context
import com.imheardev.memo.databinding.FragmentMemoBinding
import com.imheardev.memo.databinding.MemoItemBinding
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.showToast

/**
 * Created by wuto on 2021-12-18.
 */
class MemoAdapter(private val fragment: Fragment,private val memoList:List<Memo>):
RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

    inner class ViewHolder(binding: MemoItemBinding):RecyclerView.ViewHolder(binding.root){
        val content:TextView = binding.content
        val remark:TextView = binding.remark
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MemoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        var holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val memo = memoList[position]
            MemoActivity.actionStart(context,memo.content,memo.id)
//            val intent = Intent(context,MemoActivity::class.java).apply {
//                putExtra(MemoActivity.MEMO_ID,memo.id)
//                putExtra(MemoActivity.MEMO_CONTEXT,memo.content)
//                putExtra(MemoActivity.MEMO_REMARK,memo.remark)
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memoList[position]
        holder.content.text = memo.content
        holder.remark.text = memo.remark
    }

    override fun getItemCount() = memoList.size
}