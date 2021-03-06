package com.imheardev.memo.ui.memo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.model.Memo

/**
 * Created by wuto on 2021-12-18.
 */
class MemoViewModel:ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val memoList = ArrayList<Memo>()

    val memoLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchMemos(query)
    }

    fun searchMemos(query:String){
        searchLiveData.value = query
    }

    // 删除一个列表项
    private val deleteLiveData = MutableLiveData<Memo>()

    val memoDeleteLiveData = Transformations.switchMap(deleteLiveData){memo ->
        Repository.deleteMemo(memo)
    }

    fun deleteMemo(memo:Memo){
        deleteLiveData.value = memo
    }
}