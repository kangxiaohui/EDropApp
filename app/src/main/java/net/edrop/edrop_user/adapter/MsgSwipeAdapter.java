package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.ChatViewActivity;
import net.edrop.edrop_user.entity.Contacts;
import net.edrop.edrop_user.entity.MsgItemBean;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.edrop.edrop_user.utils.Constant.BASE_URL;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/28
 * Time: 8:43
 */
public class MsgSwipeAdapter extends BaseSwipeAdapter {
    private OkHttpClient okHttpClient=new OkHttpClient();
    private Context context;
    private List<MsgItemBean> list;
    private int item_swipe_msg;
    private SwipeLayout swipeLayout;
    private int userId;
    private List<Contacts> listContacts;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                list.clear();
                notifyDataSetChanged();
                String json = (String) msg.obj;
                listContacts= new Gson().fromJson(json, new TypeToken<List<Contacts>>() {}.getType());
                for (int i = 0; i < listContacts.size(); i++) {
                    String imgname = listContacts.get(i).getEmployee().getImgname();
                    String imgname1 = listContacts.get(i).getUser().getImgname();
                    String imgpath = listContacts.get(i).getEmployee().getImgpath();
                    String imgpath1 = listContacts.get(i).getUser().getImgpath();
                    MsgItemBean itemBean = new MsgItemBean();
                    itemBean.setNickName(listContacts.get(i).getEmployee().getUsername());
                    itemBean.setUserId(listContacts.get(i).getUser().getId());
                    itemBean.setEmployeeId(listContacts.get(i).getEmployee().getId());
                    itemBean.setMsg("一起来交流吧");
                    itemBean.setEmployeeHeadImg(BASE_URL.substring(0,BASE_URL.length()-1)+imgpath +"/"+ imgname);
                    itemBean.setUserHeadImg(BASE_URL.substring(0,BASE_URL.length()-1)+imgpath1 +"/"+ imgname1);
                    itemBean.setDate(getDate());
                    list.add(itemBean);
                    notifyDataSetChanged();
                }
            }
        }
    };

    public MsgSwipeAdapter(Context context, int item_swipe_msg, List<MsgItemBean> list) {
        this.item_swipe_msg = item_swipe_msg;
        this.context = context;
        this.list = list;
    }

    private String getDate() {
        String hour;
        String minute;
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        if (c.get(Calendar.HOUR_OF_DAY)<10){
            hour="0"+c.get(Calendar.HOUR_OF_DAY);
        }else {
            hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        }
        if (c.get(Calendar.MINUTE)<10){
            minute="0"+c.get(Calendar.MINUTE);
        }else {
            minute = String.valueOf(c.get(Calendar.MINUTE));
        }
        int second = c.get(Calendar.SECOND);
        return hour+":"+minute;
    }

    public void updataData(){
        SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(context, "loginInfo");
        userId = loginInfo.getInt("userId");
        //刷新信息栏
        FormBody formBody = new FormBody.Builder()
                .add("employeeId", "")
                .add("userId", userId + "")
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "getContactsById")
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

    /**
     * 返回SwipeLayout的ID，而不是布局的ID
     *
     * @param position
     * @return
     */
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    /**
     * 绑定布局
     * 和item布局进行关联的，并在这里设置swipeLayout的相关属性。
     *
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View generateView(final int position, ViewGroup parent) {
        View itemView = View.inflate(context, item_swipe_msg, null);
        swipeLayout = (SwipeLayout) itemView.findViewById(getSwipeLayoutResourceId(position));

        // 设置滑动是否可用,默认为true
        swipeLayout.setSwipeEnabled(true);

        /**
         * 打开时调用：循环调用onStartOpen,onUpdate,onHandRelease,onUpdate,onOpen,
         * 关闭时调用：onStartClose,onUpdate,onHandRelease,onHandRelease,onUpdate,onClose
         */
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
//                Log.e(TAG, "onStartOpen: ");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
//                Log.e(TAG, "onOpen: ");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
//                Log.e(TAG, "onStartClose: ");
            }

            @Override
            public void onClose(SwipeLayout layout) {
//                Log.e(TAG, "onClose: ");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//                Log.e(TAG, "onUpdate: ");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//                Log.e(TAG, "onHandRelease: ");
            }
        });

        // 设置为true,在当前一条item(除侧滑以外部分)点击时,可收回侧滑出来部分,默认为false
        swipeLayout.setClickToClose(true);

        // SwipeLayout单击事件,可替代ListView的OnItemClickListener事件.
        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, ChatViewActivity.class);
                intent.putExtra("employeeId",list.get(position).getEmployeeId());
                intent.putExtra("employeeName",list.get(position).getNickName());
                intent.putExtra("employeeHeadImg",list.get(position).getEmployeeHeadImg());
                intent.putExtra("userHeadImg",list.get(position).getUserHeadImg());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                return false;
            }
        });

        return itemView;
    }

    /**
     * 绑定数据
     * 给item中的控件绑定数据，并根据需要设置事件等操作。
     *
     * @param position
     * @param convertView
     */
    @Override
    public void fillValues(final int position, View convertView) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.nickName = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.tv_msg);
            viewHolder.headImg = (ImageView) convertView.findViewById(R.id.head_default_img);
            viewHolder.talkDate = (TextView) convertView.findViewById(R.id.tv_talk_date);
            viewHolder.swipeOpen = (TextView) convertView.findViewById(R.id.swipe_open);
            viewHolder.swipeDelete = (TextView) convertView.findViewById(R.id.swipe_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nickName.setText(list.get(position).getNickName());
        viewHolder.msg.setText(list.get(position).getMsg());
        viewHolder.talkDate.setText(list.get(position).getDate());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(context)
                .load(list.get(position).getEmployeeHeadImg())
                .apply(options)
                .into(viewHolder.headImg);

        viewHolder.swipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 关闭所有打开的Swipe的item
                closeAllItems();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        viewHolder.swipeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatViewActivity.class);
                intent.putExtra("employeeId",list.get(position).getEmployeeId());
                intent.putExtra("employeeName",list.get(position).getNickName());
                intent.putExtra("employeeHeadImg",list.get(position).getEmployeeHeadImg());
                intent.putExtra("userHeadImg",list.get(position).getUserHeadImg());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MsgItemBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private TextView nickName;
        private TextView msg;
        private ImageView headImg;
        private TextView talkDate;
        private TextView swipeDelete;
        private TextView swipeOpen;
    }

}
