package com.imheardev.memo.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.imheardev.memo.MemoApplication
import com.imheardev.memo.logic.dao.MemoDatabase
import com.imheardev.memo.logic.model.Memo
import kotlinx.coroutines.Dispatchers

/**
 * Created by wuto on 2021-12-18.
 */
object Repository {

    val memoDao = MemoDatabase.getDatabase(MemoApplication.context).memoDao()

    fun searchMemos(query:String) = liveData(Dispatchers.IO) {
        val result = try {
            Result.success(memoDao.SearchMemosByContext(query))
        }catch (e:Exception){
            Result.failure<List<Memo>>(e)
        }
        emit(result)
    }

    fun deleteMemo(memo: Memo) = liveData(Dispatchers.IO) {
        val result = try {
            Result.success(memoDao.deleteMemo(memo))
        }catch (e:Exception){
            Result.failure<List<Memo>>(e)
        }
        emit(result)
    }

    fun deleteMemo1(memo: Memo) {
        val result = try {
            Thread {
                memoDao.deleteMemo(memo)
            }.start()
        }catch (e:Exception){
            Result.failure<List<Memo>>(e)
        }
    }
}