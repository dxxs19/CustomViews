package com.wei.customviews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wei.customviews.R;

import java.util.List;

/**
 * author: WEI
 * date: 2017/2/20
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private List<String> mData;

    public RecyclerAdapter(List<String> data)
    {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 将布局转化为View并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 建立起ViewHolder中视图与数据的关联
        holder.mTextView.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private OnItemClickListener mItemClickListener;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
              if (mItemClickListener != null)
              {
                  mItemClickListener.onItemClick(v, getPosition());
              }
        }
    }
}
