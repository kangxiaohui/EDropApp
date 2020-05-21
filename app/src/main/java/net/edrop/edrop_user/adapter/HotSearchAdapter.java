package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.HotItem;

import java.util.List;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/29
 * Time: 21:19
 */
public class HotSearchAdapter extends BaseAdapter {
    private Context context;
    private int item_layout;
    private List<HotItem> list;

    public HotSearchAdapter(Context context, int item_layout, List<HotItem> list) {
        this.context = context;
        this.item_layout = item_layout;
        this.list = list;
    }


    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (list != null) {
            return list.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout, null);
            viewHoder = new ViewHoder();
            viewHoder.id = convertView.findViewById(R.id.hot_id);
            viewHoder.content = convertView.findViewById(R.id.hot_content);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder) convertView.getTag();
        }
        HotItem hotItem = list.get(position);
        viewHoder.id.setText(hotItem.getId());
        viewHoder.content.setText(hotItem.getContent());
        return convertView;
    }
    

    private class ViewHoder {
        private TextView id;
        private TextView content;
    }
}
