package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.Order;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.ORDER_STATE_FINISH;
import static net.edrop.edrop_user.utils.Constant.ORDER_STATE_NO_FINISH;
import static net.edrop.edrop_user.utils.Constant.ORDER_STATE_NO_RECEIVE;

/**
 * Created by Android Studio.
 * User: zhanghaoyu
 * Date: 2019/12/10
 * Time: 15:05
 */
public class ShowOrderAdapter extends BaseAdapter {
    private int employeeId;
    private OkHttpClient okHttpClient;
    private List<Order> dataSource = null;
    private Context context;
    private int item_layout_id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(context, msg.obj+"请到消息列表查看", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public ShowOrderAdapter(List<Order> dataSource, Context context, int item_layout_id) {
        this.dataSource = dataSource;
        this.context = context;
        this.item_layout_id = item_layout_id;
    }


    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder.tvName = convertView.findViewById(R.id.tv_showorder_name);
            viewHolder.tvPhone = convertView.findViewById(R.id.tv_showorder_phone);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_showorder_address);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_showorder_time);
            viewHolder.tvNumber = convertView.findViewById(R.id.tv_showorder_number);
            viewHolder.tvStatus = convertView.findViewById(R.id.tv_showorder_status);
            viewHolder.ivStatus = convertView.findViewById(R.id.iv_status);
            viewHolder.worker = convertView.findViewById(R.id.ll_worker);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Order order = dataSource.get(position);
        viewHolder.tvName.setText(order.getOuname());

        Timestamp timestamp = order.getReserveTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = dateFormat.format(timestamp);
        viewHolder.tvTime.setText(date);
        viewHolder.tvPhone.setText(order.getOutelephone());
        viewHolder.tvAddress.setText(order.getOrderAddress());
        viewHolder.tvNumber.setText(order.getNumber());
        if (order.getState() == ORDER_STATE_NO_RECEIVE) {
            //订单待确认
            viewHolder.tvStatus.setText("待确认");
            viewHolder.ivStatus.setImageResource(R.drawable.waitlook);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_blue));
        } else if (order.getState() == ORDER_STATE_NO_FINISH) {
            //订单待服务
            viewHolder.tvStatus.setText("待服务");
            viewHolder.ivStatus.setImageResource(R.drawable.waitservice);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_yellow));
        } else if (order.getState() == ORDER_STATE_FINISH) {
            //订单已完成
            viewHolder.tvStatus.setText("已完成");
            viewHolder.ivStatus.setImageResource(R.drawable.complete);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        viewHolder.worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeeId = order.getEmployeeId();
                postFormData();//添加消息列表
            }
        });
        return convertView;
    }

    private void postFormData() {
        okHttpClient = new OkHttpClient();
        SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(context, "loginInfo");
        int userId = loginInfo.getInt("userId");
        FormBody formBody = new FormBody.Builder()
                .add("employeeId", employeeId + "")
                .add("userId", userId + "")
                .build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "addContact")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = string;
                handler.sendMessage(message);
            }
        });
    }


    private class ViewHolder {
        private TextView tvName;
        private TextView tvPhone;
        private TextView tvAddress;
        private TextView tvTime;
        private TextView tvNumber;
        private TextView tvStatus;
        private ImageView ivStatus;
        private LinearLayout worker;
    }
}
