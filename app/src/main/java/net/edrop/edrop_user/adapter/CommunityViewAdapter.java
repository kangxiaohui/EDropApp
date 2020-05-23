package net.edrop.edrop_user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.ShowAnnimgsActivity;
import net.edrop.edrop_user.entity.Article;
import net.edrop.edrop_user.utils.CommunityGridView;
import net.edrop.edrop_user.utils.ShareAppToOther;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import xyz.bboylin.universialtoast.UniversalToast;

public class CommunityViewAdapter extends RecyclerView.Adapter<CommunityViewAdapter.MyViewHolder> {
    private BottomSheetDialog dialog;
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
            praise = itemView.findViewById(R.id.community_praise);
            discuss = itemView.findViewById(R.id.community_discuss);
            share = itemView.findViewById(R.id.community_share);
            expandOrFold = itemView.findViewById(R.id.tv_expand_or_fold);
            SimpleAdapter simpleAdapter = new SimpleAdapter(context, imagelist, R.layout.item_grid_annimgs,
                    new String[]{"image"}, new int[]{R.id.grid_item_image});
            gridView.setAdapter(simpleAdapter);//给GridView设置适配器
            gridView.setOnItemClickListener(new GridViewItemOnClick());   //添加GridView的点击事件
            discuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCommentDialog();
                }
            });
            praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPraise();
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toShare();
                }
            });
        }

    }

    private void toPraise() {
        UniversalToast.makeText(context, "点赞成功", UniversalToast.LENGTH_SHORT).showSuccess();
    }

    private void toShare() {
        ShareAppToOther shareAppToOther = new ShareAppToOther(context);
        shareAppToOther.shareWeChatFriend("EDrop", "我正在EDrop玩耍，快来和我一起玩吧。\n下载地址：https://www.lanzous.com/b0aqeodib \n" +
                "密码:90rv", ShareAppToOther.TEXT, drawableToBitmap(context.getResources().getDrawable(R.drawable.logo)));
//        UniversalToast.makeText(context, "分享成功", UniversalToast.LENGTH_SHORT).showSuccess();
    }

    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
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

    private void showCommentDialog(){
        dialog = new BottomSheetDialog(context);
        View commentView = LayoutInflater.from(context).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
//                    CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent,"刚刚");
//                    adapter.addTheCommentData(detailBean);
                    UniversalToast.makeText(context, "评论成功", UniversalToast.LENGTH_SHORT).showSuccess();
                }else {
                    UniversalToast.makeText(context, "评论内容不能为空！", UniversalToast.LENGTH_SHORT).showWarning();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }
}
