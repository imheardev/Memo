package com.imheardev.memo.ui.memo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.model.Memo

/**
 * Created by wuto on 2021-12-31.
 */
class SingleMemoViewModel: ViewModel() {    // 新增一个列表项
    private val insertLiveData = MutableLiveData<Memo>()

    val memoInsertLiveData = Transformations.switchMap(insertLiveData){ memo ->
        Log.d("SingleMemoViewModel","memoInsertLiveData")
        Repository.insertMemo(memo)
    }

    fun insertMemo(memo: Memo){
        Log.d("SingleMemoViewModel","insertMemo")
        insertLiveData.value = memo
    }
}