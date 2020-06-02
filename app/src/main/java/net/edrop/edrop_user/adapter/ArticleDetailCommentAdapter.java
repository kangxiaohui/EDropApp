package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.ArticleComment;
import net.edrop.edrop_user.utils.Constant;

import java.util.List;

public class ArticleDetailCommentAdapter extends BaseAdapter {
    private Context context;
    private int item_layout;
    private List<ArticleComment> articleComments;

    public ArticleDetailCommentAdapter(Context context, int item_layout, List<ArticleComment> articleComments) {
        this.context = context;
        this.item_layout = item_layout;
        this.articleComments = articleComments;
    }

    @Override
    public int getCount() {
        if (articleComments != null) {
            return articleComments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (articleComments != null) {
            return articleComments.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout, null);
            viewHoder = new ViewHoder();
            viewHoder.head = convertView.findViewById(R.id.article_comment_head);
            viewHoder.name = convertView.findViewById(R.id.article_comment_name);
            viewHoder.content = convertView.findViewById(R.id.article_comment_content);
            viewHoder.time = convertView.findViewById(R.id.article_comment_time);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        ArticleComment articleComment = articleComments.get(position);
        Glide.with(context)
                .load(Constant.NEWS_URL + articleComment.getUser().getImgpath())
                .into(viewHoder.head);
        viewHoder.name.setText(articleComment.getUser().getUsername());
        viewHoder.time.setText(articleComment.getCommentDate());
        viewHoder.content.setText(articleComment.getCommentContent());
        return convertView;
    }

    private class ViewHoder {
        private ImageView head;
        private TextView name;
        private TextView time;
        private TextView content;
    }
}
