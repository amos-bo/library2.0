package com.xb.amosboutilslibrary.amosboadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Amos_bo
 * @package: com.xb.myapplication.amosboadapter
 * @Created Time: 2018/8/6 15:50
 * @Changed Time: 2018/8/6 15:50
 * @email: 284285624@qq.com
 * @Org: SZKT
 * @version: V1.0
 * @describe: //TODO 添加描述
 */
public class AmosBoViewHolder extends RecyclerView.ViewHolder {
    protected Context mContext;
    protected AmosBoHolderViewHelper mHolderHelper;
    protected RecyclerView mRecyclerView;
    protected AmosBoItemListener mOnItemListener;

    protected int viewType;

    public AmosBoViewHolder(RecyclerView recyclerView, View itemView) {
        this(recyclerView, itemView, 0);
    }
    public AmosBoViewHolder(RecyclerView recyclerView, View itemView ,int viewType,int ratio) {
        this(recyclerView, itemView, 0);
        this.viewType=viewType;
    }


    public AmosBoViewHolder(RecyclerView recyclerView, View itemView, int ratio) {
        super(itemView);
        if (ratio > 0) {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams() == null ? new ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .MATCH_PARENT) : itemView.getLayoutParams();
            if (recyclerView.getLayoutManager().canScrollHorizontally()) {
                lp.width = (recyclerView.getWidth() / ratio - recyclerView.getPaddingLeft() -
                        recyclerView.getPaddingRight());
            } else {
                lp.height = recyclerView.getHeight() / ratio - recyclerView.getPaddingTop() -
                        recyclerView.getPaddingBottom();
            }
            itemView.setLayoutParams(lp);
        }
        this.mRecyclerView = recyclerView;
        this.mContext = mRecyclerView.getContext();
        this.mHolderHelper = AmosBoHolderViewHelper.holderHelperFromRecyclerView(itemView);
    }

    /**
     * 设置布局类型
     *
     * @param viewType
     * @return
     */
    public AmosBoViewHolder setViewType(int viewType) {
        this.viewType = viewType;
        return this;
    }

    /**
     * 获取布局类型
     *
     * @return
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * 获取HolderViewHelper
     * @return
     */
    public AmosBoHolderViewHelper getAmosBoHolderViewHelper() {
        return mHolderHelper;
    }

    public void setOnItemListener(AmosBoItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

}
