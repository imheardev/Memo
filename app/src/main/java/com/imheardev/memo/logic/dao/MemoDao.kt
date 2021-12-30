package com.imheardev.memo.logic.dao

import androidx.room.*
import com.imheardev.memo.logic.model.Memo

/**
 * Created by wuto on 2021-12-18.
 */
@Dao
interface MemoDao {
    @Insert
    fun insertMemo(memo:Memo):Long

    @Delete
    fun deleteMemo(memo: Memo)

    @Update
    fun updateMemo(memo:Memo)

    @Query("select * from Memo")
    fun loadAllMemos():List<Memo>

    @Query("select * from Memo where content like '%'||:value||'%'")
    fun SearchMemosByContext(value:String):List<Memo>
}