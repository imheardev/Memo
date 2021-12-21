package com.imheardev.memo.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by wuto on 2021-12-18.
 * content 内容
 * done 是否完成
 * remark 备注
 * alertTime 提醒时间
 * createTime 创建时间
 * updateTime 更新时间
 * important 是否重要
 * urgent 是否紧急
 */
@Entity
data class Memo(var content:String,var done:Int=0,
                var remark:String="",var alertTime:String="",
                var createTime:String="",var updateTime:String="",
                var important:Int=0,var urgent:Int=0){
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}
