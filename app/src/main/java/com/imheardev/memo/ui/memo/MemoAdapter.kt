package com.imheardev.memo.ui.memo

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import com.imheardev.memo.MemoApplication.Companion.context
import com.imheardev.memo.R
import com.imheardev.memo.databinding.MemoItemBinding
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.model.Memo
import com.imheardev.memo.util.KeywordUtil

/**
 * RecyclerView 适配器
 * Created by wuto on 2021-12-18.
 */
class MemoAdapter(private val fragment: Fragment,private val memoList:List<Memo>):
RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

    private lateinit var _keyword:String

    // 左右滑动列表项回调接口
    private lateinit var itemSwipedListener:ItemSwipedListener

    //定义setItemSwipedListener()方法
    fun setItemSwipedListener(itemSwipedListener:ItemSwipedListener) {
        this.itemSwipedListener = itemSwipedListener!!
    }

    //拖拽回调接口
    private val mItemDragHelperCallback = ItemDragHelperCallback()
    //触摸辅助类 可以用于主动调用删除
    private val mItemTouchHelper = ItemTouchHelper(mItemDragHelperCallback)
    //生明常量
    companion object {
        open val TYPE_TITLE: Int = 1
        open val TYPE_SUB_IMG: Int = 2
    }

    /**
     * 将recyclerView依附到触摸辅助类
     * 2021/12/29 add
     * */
    open fun attachiToRecyclerView(recyclerView: RecyclerView) {
        if (mItemTouchHelper != null && recyclerView != null) {
            mItemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    /**
     * 布局容器
     */
    inner class ViewHolder(binding: MemoItemBinding):RecyclerView.ViewHolder(binding.root){
        val content:TextView = binding.content
        val remark:TextView = binding.remark
        // 列表项遮罩
        var mShadow:View = binding.itemShadow
    }

    /**
     * 初始化布局文件
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MemoItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        var holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            //TODO 避免多次点击，databingding方法
            val position = holder.absoluteAdapterPosition
            val memo = memoList[position]
            //这里将fragment中的查询关键字传入actionStart，用作子画面关键字变色高亮显示
            MemoActivity.actionStart(context,memo.content,memo.id,_keyword)
        }
        return holder
    }

    /**
     * 设置每个列表项的显示内容
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memoList[position]
        //关键字变色高亮显示
        var spanable = KeywordUtil.matcherSearchTitle(context.getResources().getColor(R.color.blue_policy),memo.content,_keyword)
        holder.content.setText(spanable)
        //TODO这里对日期进行自定义格式化
        holder.remark.text = memo.updateTime?:"12月18日"
    }

    /**
     * 获取列表项个数
     */
    override fun getItemCount() = memoList.size

    /**
     *  左右滑动列表项
     */
    fun onItemSwiped(index:Int,direction:Int,){
        // 使用回调方法
        itemSwipedListener.onItemSwiped(index,direction)
    }

    /**
     * 传递关键字
     */
    fun setKeyword(value:String) {
        // 使用回调方法
        _keyword = value
    }

    /**
     * 交换列表项
     */
    fun itemMove(oldIndex:Int,newIndex:Int){
        notifyItemMoved(oldIndex,newIndex)
    }

    inner class ItemDragHelperCallback:ItemTouchHelper.Callback(){
        /**
         * 获取可以拖动的方向标志
         * */
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            //此处返回可以拖动的方向值
            var swipe = 0
            var move = 0
            //此处为 假设recyclerview 不为空
            recyclerView.let {
                if (recyclerView.layoutManager is GridLayoutManager){
                    //如果是网格型 则可以左右上下都可以拖动
                    move = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                }else if (recyclerView.layoutManager is LinearLayoutManager){
                    //支持上下拖动
                    move = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    //左右滑动删除
                    swipe = ItemTouchHelper.START or ItemTouchHelper.END
                }
            }
            return ItemTouchHelper.Callback.makeMovementFlags(move,swipe)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if(viewHolder != null && target != null){
                var fromPos = viewHolder.absoluteAdapterPosition
                var toPos = target.absoluteAdapterPosition
                //此处为memoList不为空时
                memoList.let{
                    var from = memoList[fromPos]
                    itemMove(fromPos,toPos)
                }
            }
            //默认不让拖动
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //用于执行滑动删除
            onItemSwiped(viewHolder.absoluteAdapterPosition,direction)
        }

        //不重写默认是返回true的 如果返回false的话 需要使用ItemTouchHelper的实例方法
        //使用 startDrag 来执行拖拽
        //使用 startSwipe 来执行滑动删除
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        //是否支持滑动功能
        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        /**
         * 此处用于状态变化时 更换图片状态
         * */
        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                //显示遮罩
                viewHolder.let {
                    if (viewHolder is ViewHolder){
                        viewHolder.mShadow.visibility = View.VISIBLE
                    }
                }
            }
        }

        /**
         * 此处当拖拽完成
         * */
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.let {
                if(viewHolder is ViewHolder){
                    viewHolder.mShadow.visibility = View.GONE
                }
            }
        }
    }
}

//添加一个回调接口 ItemSwipedListener
interface ItemSwipedListener {
    fun onItemSwiped(index:Int,direction: Int)
}
