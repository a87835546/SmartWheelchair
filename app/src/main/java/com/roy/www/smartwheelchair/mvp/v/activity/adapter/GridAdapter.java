package com.roy.www.smartwheelchair.mvp.v.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roy.www.smartwheelchair.R;
import com.roy.www.smartwheelchair.mvp.bean.DeviceListBean;
import com.roy.www.smartwheelchair.mvp.interfaces.OnItemClickListener;

import java.util.List;


/**
 * Created by 李杨
 * On 2021/6/21
 * Email: 631934797@qq.com
 * Description:
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private Context mContext;
    private OnItemClickListener mListener;
    private List<DeviceListBean> mListBeans;
    private int mCont;

    public GridAdapter(Context context, OnItemClickListener listener, List<DeviceListBean> listBeans) {

        mContext = context;
        mListener = listener;
        mListBeans = listBeans;
    }

    public GridAdapter(Context context, OnItemClickListener listener,int cont) {

        mContext = context;
        mListener = listener;
        mCont = cont;
    }

    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GridAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, final int position) {
        holder.textView.setText("轮椅" + position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCont;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
        }
    }



}
