package net.edrop.edrop_user.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import net.edrop.edrop_user.R;

public class MessageFragment extends DialogFragment {

    public static final String TAG = "MessageDialog";
    private OnMsgResultListener mOnMsgResultListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //message_dialog是提示框的布局
        View show_view = inflater.inflate(R.layout.message_dialog, null);

        TextView mTxtContent = (TextView) show_view.findViewById(R.id.tv_content);
        TextView mTxtTitle = (TextView) show_view.findViewById(R.id.tv_title);
        Button btnSubmit = (Button) show_view.findViewById(R.id.btn_submit);
        Button btnCancel = (Button) show_view.findViewById(R.id.btn_cancel);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            String title = bundle.getString("title");
            if(title!=null){
                mTxtTitle.setText(bundle.getString("title"));
            }
            mTxtContent.setText(bundle.getString("content"));
        }
        btnSubmit.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);

        setCancelable(false);
        return show_view;
    }

    public void setOnResultListener(OnMsgResultListener listener) {
        mOnMsgResultListener = listener;
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_submit:
                    mOnMsgResultListener.onResultFun(Activity.RESULT_OK);
                    dismiss();
                    break;

                case R.id.btn_cancel:
                    mOnMsgResultListener.onResultFun(Activity.RESULT_CANCELED);
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public interface OnMsgResultListener {
        void onResultFun(int resultCode);
    }
}