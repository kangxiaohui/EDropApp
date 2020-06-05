package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error);
        Glide.with(context)
                .load(Constant.BASE_URL.substring(0,Constant.BASE_URL.length()-1) + articleComment.getUser().getImgpath()+ "/" +articleComment.getUser().getImgname())
                .apply(options)
                .into(viewHoder.head);
        viewHoder.name.setText(articleComment.getUser().getUsername());
        viewHoder.time.setText(TimeFormatConversion(articleComment.getCommentDate()));
        viewHoder.content.setText(articleComment.getCommentContent());
        return convertView;
    }

    private String TimeFormatConversion(String time) {
        String[] times = time.split(" ");
        String month = FormatMonth(times[0]);
        String day = times[1].substring(0,times[1].length()-1);
        String year = times[2];
        String date = times[3];
        Log.e("date",year+month+day+date);
        return year+"-"+month+"-"+day+" "+date;
    }

    private String FormatMonth(String time) {
        if (time.equals("Jan")){
            return "1";
        }else if (time.equals("Feb")){
            return "2";
        }else if (time.equals("Mar")){
            return "3";
        }else if (time.equals("Apr")){
            return "4";
        }else if (time.equals("May")){
            return "5";
        }else if (time.equals("Jun")){
            return "6";
        }else if (time.equals("Jul")){
            return "7";
        }else if (time.equals("Aug")){
            return "8";
        }else if (time.equals("Sep")){
            return "9";
        }else if (time.equals("Oct")){
            return "10";
        }else if (time.equals("Nov")){
            return "11";
        }else if (time.equals("De")){
            return "12";
        }else {
            return null;
        }
    }

    private class ViewHoder {
        private ImageView head;
        private TextView name;
        private TextView time;
        private TextView content;
    }
}
