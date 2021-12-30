package com.imheardev.memo.ui.memo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imheardev.memo.MemoApplication.Companion.context
import com.imheardev.memo.databinding.MemoItemBinding
import com.imheardev.memo.logic.Repository
import com.imheardev.memo.logic.model.Memo

/**
 * RecyclerView 适配器
 * Created by wuto on 2021-12-18.
 */
class MemoAdapter(private val fragment: Fragment,private val memoList:List<Memo>):
RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

    // 删除回调接口
    private lateinit var callBack:CallBack

    //定义 setCallBack() 方法
    fun setCallBack(callBack: CallBack?) {
        this.callBack = callBack!!
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
        // 2021/12/29 add
        var mShadow:View = binding.itemShadow
//        constructor(itemView: View) : super(itemView) {
//            mShadow = itemView.findViewById(R.id.itemShadow)
//        }
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
            MemoActivity.actionStart(context,memo.content,memo.id)
        }
        return holder
    }

    /**
     * 设置每个列表项的显示内容
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memoList[position]
        holder.content.text = memo.content
        holder.remark.text = memo.updateTime?:"12月18日"
    }

    /**
     * 获取列表项个数
     */
    override fun getItemCount() = memoList.size

    /**
     * 删除列表项
     */
    fun itemDelete(index:Int){
        val memo = memoList[index]
        // 使用回调方法
        callBack.onRemove(memo)
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
                    //TODO  在adapter中实现itemMove
//                    memoList.removeAt(fromPos)
//                    memoList.add(toPos,from)
//                    notifyItemMoved(fromPos,toPos)
//                    return true
                    itemMove(fromPos,toPos)
                }
            }
            //默认不让拖动
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //用于执行滑动删除
            itemDelete(viewHolder.absoluteAdapterPosition)
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

//添加一个回调接口 CallBack
interface CallBack {
    fun onRemove(memo: Memo)
}
