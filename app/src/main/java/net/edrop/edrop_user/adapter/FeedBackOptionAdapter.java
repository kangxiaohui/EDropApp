package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.FeedBackActivity;

import java.util.List;


/**
 * Created by 李诗凡.
 * User: sifannnn
 * Date: 2019/12/9
 * Time: 14:34
 * TODO：反馈详细选项页面的adapter
 */
public class FeedBackOptionAdapter extends BaseAdapter {
    // 原始数据
    private List<String> dataSource = null;
    // 上下文环境
    private Context context = null;
    // item对应的布局文件
    private int item_layout_id;


    /**
     * 构造器，完成初始化
     *
     * @param context        上下文环境
     * @param dataSource     原始数据
     * @param item_layout_id item对应的布局文件
     */
    public FeedBackOptionAdapter(Context context,
                                 List dataSource,
                                 int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }

    /**
     * 返回当数据条数，AdapterView（ListView）需要通过它确定item数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return dataSource.size();
    }

    /**
     * 根据对应位置，返回item的原始数据
     *
     * @param position item在ListView中的位置
     * @return 对应位置的item的原始数据
     */
    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    /**
     * 根据item位置返回item对应的ID，ID在ListView的监听器响应方法中
     * 被使用
     *
     * @param position item在ListView中的位置，从0开始
     * @return 返回当前item的ID
     */
    @Override
    public long getItemId(int position) {
        // 为了简便起见，我们返回位置，让位置作为id
        // 实际开发中id使用数据的主键，或者唯一标识
        return position;
    }

    /**
     * 根据item位置返回item用来显示的View视图
     *
     * @param position    item位置
     * @param convertView covertView是从布局文件中inflate来的布局
     * @param parent      item所属的父容器
     * @return 相应位置用来显示的View视图对象
     */
    @Override
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        if (convertView == null) {
            //布局填充器，根据布局文件生成相应的VIew
            LayoutInflater inflater = LayoutInflater.from(context);
            // 使用布局填充器根据布局文件资源ID生成View视图对象
            convertView = inflater.inflate(item_layout_id, null);
        }
        LinearLayout linearLayout = convertView.findViewById(R.id.ll_custom);
        TextView textView = convertView.findViewById(R.id.tv_custom_item);
        final String str = dataSource.get(position);
        textView.setText(str);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("msg", str);
                /*发生intent请求*/
                context.startActivity(intent);
//                Log.e("信息", str);
            }
        });
        return convertView;
    }
}
