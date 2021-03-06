package net.edrop.edrop_user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.model.bean.IMUserInfo;
import net.edrop.edrop_user.model.bean.InvitationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2020/4/30
 * Time: 19:12
 */
//邀请信息列表页面的适配器
public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();
    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context, OnInviteListener OnInviteListener) {
        mContext = context;
        mOnInviteListener = OnInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> invitationInfos) {

        if (invitationInfos != null && invitationInfos.size() >= 0) {

            mInvitationInfos.clear();

            mInvitationInfos.addAll(invitationInfos);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //1.获取或创建ViewHolder
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_invite, null);

            holder.name = convertView.findViewById(R.id.tv_invite_name);
            holder.reason = convertView.findViewById(R.id.tv_invite_reason);
            holder.accept = convertView.findViewById(R.id.bt_invite_accept);
            holder.reject = convertView.findViewById(R.id.bt_invite_reject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //2.获取当前item数据
        final InvitationInfo invitationInfo = mInvitationInfos.get(position);

        //3.显示当前item数据
        IMUserInfo user = invitationInfo.getUser();
        if (user != null) {//联系人
            //名称的展示
            holder.name.setText(invitationInfo.getUser().getName());
            holder.accept.setVisibility(View.GONE);
            holder.reject.setVisibility(View.GONE);

            //原因
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE) {//新的邀请
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("请求添加好友");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
                holder.accept.setVisibility(View.VISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
            } else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT) {//接受邀请
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("接受邀请");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            } else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {//邀请被接受
                if (invitationInfo.getReason() == null) {
                    holder.reason.setText("邀请被接受");
                } else {
                    holder.reason.setText(invitationInfo.getReason());
                }
            }

            //按钮的处理
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invitationInfo);
                }
            });

            //拒绝按钮的点击事件处理
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invitationInfo);
                }
            });
        }

        //4.返回view
        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private TextView reason;
        private Button accept;
        private Button reject;
    }

    public interface OnInviteListener {
        //联系人接受按钮的点击事件
        void onAccept(InvitationInfo invitationInfo);

        //联系人拒绝按钮的点击事件
        void onReject(InvitationInfo invitationInfo);

        //接受邀请按钮处理
        void onInviteAccept(InvitationInfo invitationInfo);

        //拒绝邀请按钮处理
        void onInviteReject(InvitationInfo invitationInfo);

        //接受申请按钮处理
        void onApplicationAccept(InvitationInfo invitationInfo);

        //拒绝申请按钮处理
        void onApplicationReject(InvitationInfo invitationInfo);
    }
}