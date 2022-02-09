package com.imheardev.memo.ui.memo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imheardev.memo.MemoApplication
import com.imheardev.memo.databinding.FragmentMemoBinding
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.showToast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by wuto on 2021-12-18.
 */
class MemoFragment:Fragment() {
    private var _binding:FragmentMemoBinding?=null
    private val binding get() = _binding!!
    val viewModel by lazy { ViewModelProvider(this).get(MemoViewModel::class.java) }
    private lateinit var adapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        _binding = FragmentMemoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 创建并设置布局管理器
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        // 创建并设置列表适配器
        adapter = MemoAdapter(this,viewModel.memoList)
        // 2021/12/29 add
        adapter.attachiToRecyclerView(binding.recyclerView)
        // 实例化adapter中回调接口
        var itemSwipedListener:ItemSwipedListener = object :ItemSwipedListener{
            override fun onItemSwiped(index: Int, direction: Int) {
                if(direction == 32){
                    // 从左往右滑动
                    showExitDialog(index,direction)
                }
                if (direction == 16){
                    // 从右往左滑动 TODO 虽然没有物理删除，但是列表中少了
                    showExitDialog(index,direction)
                }
                val content = binding.searchMemoEdit.text.toString()
                viewModel.searchMemos(content)
            }
        }
        adapter.setItemSwipedListener(itemSwipedListener)
        binding.recyclerView.adapter = adapter
        // 查询视图更新事件监听
        binding.searchMemoEdit.addTextChangedListener{editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchMemos(content)
                binding.delSearchMemoEdit.visibility = View.VISIBLE
            }else{
                viewModel.searchMemos("")
                binding.delSearchMemoEdit.visibility = View.GONE
            }
        }
        //设置删除图标点击事件
        binding.delSearchMemoEdit.setOnClickListener {
            binding.searchMemoEdit.setText("")
        }
        // 观察memoLiveData变化，并显示到ui上
        viewModel.memoLiveData.observe(this, Observer{ result ->
            val tempMemos = result.getOrNull()
            if(tempMemos != null){
                //这里对列表进行排序:按updateTime倒序
                val memos = tempMemos.sortedByDescending { it.updateTime }
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.memoList.clear()
                viewModel.memoList.addAll(memos)
                //传递关键字，对列表中的关键字进行关键字变色高亮显示
                adapter.setKeyword(binding.searchMemoEdit.text.toString())
                adapter.notifyDataSetChanged()
//                val msg = viewModel.memoList.size.toString()+"条待办"
                val msg = adapter.itemCount.toString()+"条待办"
                binding.asearchMemoEdit.setText(msg)
            }else{
                "没有待办".showToast()
            }
        })
        // 观察deleteLiveData变化，并显示到ui上
        viewModel.memoDeleteLiveData.observe(this, Observer{ result ->
            val content = binding.searchMemoEdit.text.toString()
            if (content.isNotEmpty()){
                viewModel.searchMemos(content)
            }else{
                viewModel.searchMemos("")
            }
        })
        Log.d(TAG, "onActivityCreated")
    }

    //TODO 返回选择结果，改为公共对话框模板,单例模式?
    private fun showExitDialog(index: Int, direction: Int){
        var builder= AlertDialog.Builder(this.activity!!)
        builder.setTitle("是否删除")
        val msg = when(viewModel.memoList[index].content.length){
            in 1..15 -> viewModel.memoList[index].content
            else -> viewModel.memoList[index].content.substring(0,15)+"..."
        }
        builder.setMessage("$msg")
        builder.setPositiveButton("删除"){dialog,which ->
            val memo = viewModel.memoList[index]
            viewModel.deleteMemo(memo)
        }
        builder.setNegativeButton("不删除"){dialog,which ->

        }
        var dialog: AlertDialog = builder.create()
        if(!dialog.isShowing){
            dialog.show()
        }
    }

    companion object{
        const val TAG = "MemoFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG,"onAttach")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }
    override fun onStart() {
        super.onStart()
        // 加载待办列表
        viewModel.searchMemos(binding.searchMemoEdit.text.toString())
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }

}
