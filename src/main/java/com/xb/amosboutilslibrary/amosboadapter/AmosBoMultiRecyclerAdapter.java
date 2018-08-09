package com.xb.amosboutilslibrary.amosboadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosboadapter
 * @Created Time: 2018/8/6 16:01
 * @Changed Time: 2018/8/6 16:01
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //TODO 添加描述
 */
public abstract class AmosBoMultiRecyclerAdapter<T> extends RecyclerView.Adapter<AmosBoViewHolder> {

    protected int mItemLayoutId;
    protected Context mContext;
    protected List<T> mList;
    protected AmosBoItemListener mOnItemListener;

    protected RecyclerView mRecyclerView;
    /**
     * item项等分个数
     */
    protected int ratio;

    /**
     * 多布局键值对-以布局Id为建和值
     */
    protected SparseArray<Integer> mLayoutTypes;

    public AmosBoMultiRecyclerAdapter(RecyclerView mRecyclerView, int[] itemLayoutId) {
        this(mRecyclerView);
        this.mLayoutTypes = new SparseArray<>();
        for (int i = 0; i < itemLayoutId.length; i++) {
            this.mLayoutTypes.put(itemLayoutId[i], itemLayoutId[i]);
        }
    }

    public AmosBoMultiRecyclerAdapter(RecyclerView mRecyclerView) {
        super();
        this.mContext = mRecyclerView.getContext();
        this.mRecyclerView = mRecyclerView;
        this.mList = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public T getItem(int position) {
        if (isSafePosition(position)) {
            return mList.get(position);
        }
        return null;
    }

    private boolean isSafePosition(int position) {

        return mList != null && position >= 0 && position < mList.size();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }


    /**
     * 等分个数
     *
     * @return
     */
    public int getRatio() {
        return ratio;
    }

    /**
     * 设置等分个数
     *
     * @return
     */
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    /**
     * 获取数据
     *
     * @return List
     */
    public List<T> getList() {
        return mList;
    }


    /**
     * 获取一个从copy的list
     *
     * @return List
     */
    public List<T> getCopyList() {

        List<T> list = new ArrayList<>();
        list.addAll(mList);
        return list;
    }

    /**
     * 添加到头部
     *
     * @param datas List
     */
    public void addNewList(List<T> datas) {
        if (datas != null && !datas.isEmpty()) {
            mList.addAll(0, datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加到末尾
     *
     * @param datas List
     */
    public void addMoreList(List<T> datas) {
        if (datas != null && !datas.isEmpty()) {
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }


    /**
     * 设置数据
     *
     * @param datas List
     */
    public void setList(List<T> datas) {
        mList.clear();
        if (datas != null && !datas.isEmpty()) {
            mList.addAll(datas);
        }
        notifyDataSetChanged();
    }


    /**
     * 删除一个列表
     *
     * @param datas List
     */
    public void removeList(List<T> datas) {
        mList.removeAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清空
     */
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    /**
     * 删除指定索引数据条目
     *
     * @param position position
     */
    public void removeItem(int position) {
        if (isSafePosition(position)) {
            mList.remove(position);
            notifyItemRangeRemoved(position, 1);
        }

    }

    /**
     * 删除指定数据条目
     *
     * @param model T
     */
    public void removeItem(T model) {

        if (mList != null && mList.contains(model)) {
            int index = mList.indexOf(model);
            if (isSafePosition(index)) {
                removeItem(index);
            }
        }


    }

    /**
     * 在指定位置添加数据条目
     *
     * @param position int
     * @param model    T
     */
    public void addItem(int position, T model) {

        if (position >= 0 && position <= mList.size()) {
            mList.add(position, model);
            notifyItemInserted(position);
        }


    }

    /**
     * 在集合头部添加数据条目
     *
     * @param model T
     */
    public void addFirstItem(T model) {
        addItem(0, model);
    }

    /**
     * 在集合末尾添加数据条目
     *
     * @param model T
     */
    public void addLastItem(T model) {
        addItem(mList.size(), model);
    }

    /**
     * 替换指定索引的数据条目
     *
     * @param location int
     * @param newModel T
     */
    public void setItem(int location, T newModel) {

        if (isSafePosition(location)) {
            mList.set(location, newModel);

            notifyItemChanged(location);
        }

    }

    /**
     * 替换指定数据条目
     *
     * @param oldModel T
     * @param newModel T
     */
    public void setItem(T oldModel, T newModel) {
        setItem(mList.indexOf(oldModel), newModel);
    }

    /**
     * 交换两个数据条目的位置
     *
     * @param fromPosition int
     * @param toPosition   int
     */
    public void moveItem(int fromPosition, int toPosition) {

        if (isSafePosition(fromPosition) && isSafePosition(toPosition)) {
            Collections.swap(mList, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }

    }

    @Override
    public AmosBoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mItemLayoutId = mLayoutTypes.get(viewType);
        View itemView = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
        return new AmosBoViewHolder(mRecyclerView, itemView, viewType, 0);
    }

    @Override
    public void onBindViewHolder(AmosBoViewHolder viewHolder, int position) {
        viewHolder.setOnItemListener(mOnItemListener);
        AmosBoHolderViewHelper mHolderHelper = viewHolder.getAmosBoHolderViewHelper();
        mHolderHelper.setPosition(position);
        mHolderHelper.setAmosBoItemListener(mOnItemListener);
        setView(mHolderHelper, position, getItem(position));
        setItemListener(mHolderHelper);
    }


    /**
     * 设置item中的子控件点击事件监听器
     *
     * @param onItemListener CanOnItemListener
     */
    public void setOnItemListener(AmosBoItemListener onItemListener) {
        mOnItemListener = onItemListener;
    }

    /**
     * @param helper
     * @param position
     * @param bean
     */
    protected abstract void setView(AmosBoHolderViewHelper helper, int position, T bean);

    /**
     * @param helper
     */
    protected abstract void setItemListener(AmosBoHolderViewHelper helper);

    /**
     * 获取指定的布局类型的抽象方法，让子类继承实现:以布局Id为Type
     *
     * @param position
     * @return
     */
    protected abstract int getItemType(int position);

}
