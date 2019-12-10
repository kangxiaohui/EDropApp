package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.callBack.MyItemCallback;
import net.edrop.edrop_user.entity.Problems;

import java.util.List;

public class MyDiffAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private AsyncListDiffer<Problems> mTextDiffl;
    private DiffUtil.ItemCallback<Problems> diffCallback = new MyItemCallback();

    public MyDiffAdapter(Context mContext) {
        this.mContext = mContext;
        mTextDiffl = new AsyncListDiffer<>(this, diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_view, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Log.e("ref", "position: "+position );
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        Problems bean  = getItem(position);
        myViewHolder.tv.setText(bean.getQuestion() + "." + bean.getId());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void submitList(List<Problems> data) {
        Log.e("ljc ",data.toString());
        mTextDiffl.submitList(data);
    }
    public Problems getItem(int position) {
        return mTextDiffl.getCurrentList().get(position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }
}
