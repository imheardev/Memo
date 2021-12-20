package com.imheardev.memo.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imheardev.memo.logic.model.Memo

/**
 * Created by wuto on 2021-12-18.
 */
@Dao
interface MemoDao {
    @Insert
    fun insertMemo(memo:Memo):Long

    @Query("select * from Memo")
    fun loadAllMemos():List<Memo>

    @Query("select * from Memo where content like '%'||:value||'%'")
    fun SearchMemosByContext(value:String):List<Memo>
}