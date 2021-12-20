package com.imheardev.memo.ui.memo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imheardev.memo.databinding.FragmentMemoBinding
import com.imheardev.memo.showToast

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
        _binding = FragmentMemoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MemoAdapter(this,viewModel.memoList)
        binding.recyclerView.adapter = adapter
        binding.searchMemoEdit.addTextChangedListener{editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchMemos(content)
            }else{
                viewModel.searchMemos("")
            }
        }
        viewModel.memoLiveData.observe(this, Observer{ result ->
            val memos = result.getOrNull()
            if(memos != null){
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.memoList.clear()
                viewModel.memoList.addAll(memos)
                adapter.notifyDataSetChanged()
            }else{
                "没有待办".showToast()
            }
        })
        // 初始化待办列表
        viewModel.searchMemos("")
    }
}
