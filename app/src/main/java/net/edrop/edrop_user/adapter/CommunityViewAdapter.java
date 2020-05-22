package net.edrop.edrop_user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.ShowAnnimgsActivity;
import net.edrop.edrop_user.entity.Article;
import net.edrop.edrop_user.utils.CommunityGridView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CommunityViewAdapter extends RecyclerView.Adapter<CommunityViewAdapter.MyViewHolder> {
    private Activity context;
    private int item_layout;
    private SparseArray<Integer> mTextStateList;//保存文本状态集合
    private List<Article> articles;
    private List<Map<String, Object>> imagelist;
    private final int MAX_LINE_COUNT = 3;//最大显示行数
    private final int STATE_UNKNOW = -1;//未知状态
    private final int STATE_NOT_OVERFLOW = 1;//文本行数小于最大可显示行数
    private final int STATE_COLLAPSED = 2;//折叠状态
    private final int STATE_EXPANDED = 3;//展开状态

    public CommunityViewAdapter(Activity context, int item_layout, List<Article> articles,List<Map<String, Object>> imagelist) {
        this.context = context;
        this.item_layout = item_layout;
        this.articles = articles;
        this.imagelist = imagelist;
        mTextStateList = new SparseArray<>();
    }

    @Override
    public int getItemCount() {
        if (articles != null) {
            return articles.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(context.getLayoutInflater().inflate(item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int state = mTextStateList.get(articles.get(position).getId(), STATE_UNKNOW);
        //第一次初始化，未知状态
        if (state == STATE_UNKNOW) {
            holder.content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回掉会调用多次，获取完行数后记得注销监听
                    holder.content.getViewTreeObserver().removeOnPreDrawListener(this);
                    //holder.content.getViewTreeObserver().addOnPreDrawListener(null);
                    //如果内容显示的行数大于最大显示行数
                    if (holder.content.getLineCount() > MAX_LINE_COUNT) {
                        holder.content.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        holder.expandOrFold.setVisibility(View.VISIBLE);//显示“全文”
                        holder.expandOrFold.setText("全文");
                        mTextStateList.put(articles.get(position).getId(), STATE_COLLAPSED);//保存状态
                    } else {
                        holder.expandOrFold.setVisibility(View.GONE);
                        mTextStateList.put(articles.get(position).getId(), STATE_NOT_OVERFLOW);
                    }
                    return true;
                }
            });

            holder.content.setMaxLines(Integer.MAX_VALUE);//设置文本的最大行数，为整数的最大数值
            holder.content.setText(articles.get(position).getContent());
            holder.datetime.setText(articles.get(position).getDatetime());
        } else {
            //如果之前已经初始化过了，则使用保存的状态。
            switch (state) {
                case STATE_NOT_OVERFLOW:
                    holder.expandOrFold.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrFold.setVisibility(View.VISIBLE);
                    holder.expandOrFold.setText("全文");
                    break;
                case STATE_EXPANDED:
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrFold.setVisibility(View.VISIBLE);
                    holder.expandOrFold.setText("收起");
                    break;
            }
            holder.content.setText(articles.get(position).getContent());
        }

        //全文和收起的点击事件
        holder.expandOrFold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = mTextStateList.get(articles.get(position).getId(), STATE_UNKNOW);
                if (state == STATE_COLLAPSED) {
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                    holder.expandOrFold.setText("收起");
                    mTextStateList.put(articles.get(position).getId(), STATE_EXPANDED);
                } else if (state == STATE_EXPANDED) {
                    holder.content.setMaxLines(MAX_LINE_COUNT);
                    holder.expandOrFold.setText("全文");
                    mTextStateList.put(articles.get(position).getId(), STATE_COLLAPSED);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView content;
        private CommunityGridView gridView;
        private TextView datetime;
        private TextView expandOrFold;
        private ImageView praise;
        private ImageView discuss;
        private ImageView share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.list_title_text);
            datetime = itemView.findViewById(R.id.list_title_time);
            gridView = itemView.findViewById(R.id.list_item_grid_view);
            praise = itemView.findViewById(R.id.community_zan);
            discuss = itemView.findViewById(R.id.community_discuss);
            share = itemView.findViewById(R.id.community_share);
            expandOrFold = itemView.findViewById(R.id.tv_expand_or_fold);
            SimpleAdapter simpleAdapter = new SimpleAdapter(context, imagelist, R.layout.item_grid_annimgs,
                    new String[]{"image"}, new int[]{R.id.grid_item_image});
            gridView.setAdapter(simpleAdapter);      //给GridView设置适配器
            gridView.setOnItemClickListener(new GridViewItemOnClick());   //添加GridView的点击事件
        }
    }

    private class GridViewItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, ShowAnnimgsActivity.class);
            intent.putExtra("id", position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("image", (Serializable) imagelist);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
