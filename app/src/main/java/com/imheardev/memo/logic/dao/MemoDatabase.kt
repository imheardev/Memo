package com.imheardev.memo.logic.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.imheardev.memo.logic.model.Memo

/**
 * Created by wuto on 2021-12-18.
 */
@Database(version = 3,entities = [Memo::class])
abstract class MemoDatabase:RoomDatabase(){

    abstract fun memoDao():MemoDao

    companion object{
        val MIGRATION_1_2 = object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                //SQLite不支持一次性添加多个字段，所以需要多次执行添加单个字段语句。
                val sqlV2 = arrayOf("alter table Memo add column createTime text not null default 'unknow'",
                    "alter table Memo add column alertTime text not null default 'unknow'",
                    "alter table Memo add column important INTEGER not null default 0",
                    "alter table Memo add column urgent INTEGER not null default 0")
                sqlV2.forEach {
                    database.execSQL(it)
                }
            }
        }
        var MIGRATION_2_3 = object:Migration(2,3){
            //Memo表增加字段：updateTime text 最后更新时间
            override fun migrate(database: SupportSQLiteDatabase) {
                var sqlV3 = arrayOf("alter table Memo add column updateTime text not null default 'unknow'")
                sqlV3.forEach {
                    database.execSQL(it)
                }
            }
        }

        private var instance:MemoDatabase?=null

        @Synchronized
        fun getDatabase(context:Context):MemoDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
            MemoDatabase::class.java,"memo_database")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build().apply{
                    instance = this
                }
        }
    }
}