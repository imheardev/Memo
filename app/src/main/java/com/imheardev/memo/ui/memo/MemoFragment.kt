package com.imheardev.memo.ui.memo

import android.content.Context
import android.os.Bundle
import android.util.Log
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
        Log.d(TAG, "onCreateView")
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
        Log.d(TAG, "onActivityCreated")
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
        viewModel.searchMemos("")
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
