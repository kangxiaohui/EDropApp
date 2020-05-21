package net.edrop.edrop_user.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.NewsList;

import java.util.List;


/**
 * Created by 李诗凡.
 * User: sifannnn
 * Date: 2019/12/2
 * Time: 15:04
 * TODO:给拍照识别结果设置布局
 */
public class RecognitionAdapter extends BaseAdapter {
    // 原始数据
    private List<NewsList> dataSource = null;
    // 上下文环境
    private Context context;
    // item对应的布局文件
    private int item_layout_id;
    private PopupWindow popupWindow = null;
    private View popupView = null;
    private TextView tvRubbishName;
    private ImageView imageView;
    private TextView tvRubbishType;
    private TextView tvRubbishTip;
    private Activity activity;

    /**
     * 构造器，完成初始化
     *
     * @param context        上下文环境
     * @param dataSource     原始数据
     * @param item_layout_id item对应的布局文件
     */
    public RecognitionAdapter(Context context,
                              List<NewsList> dataSource,
                              Activity activity,
                              int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.activity = activity;
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
            viewHolder.text = convertView.findViewById(R.id.tv_recognition);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsList newsList = dataSource.get(position);
        viewHolder.text.setText(newsList.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null || !popupWindow.isShowing()) {
                    //显示PopupWindow
                    showPopupWindow(position);
//                    Log.e("test", dataSource.get(position).toString());
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public TextView text;
    }

    private void showPopupWindow(int position) {
        if (dataSource.get(position).getLajitype() != 4) {
            //创建popupwindow对象
//            backgroundAlpha(0.5f);
            setBackgroundAlpha(0.5f,context);
            popupWindow = new PopupWindow();
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //通过布局填充器创建View
            popupView = View.inflate(context, R.layout.item_popupwindow_recognitionresult_layout, null);
            tvRubbishName = popupView.findViewById(R.id.tv_rubbishname);
            imageView = popupView.findViewById(R.id.iv_rubbish);
            tvRubbishType = popupView.findViewById(R.id.tv_rubbish);
            tvRubbishTip = popupView.findViewById(R.id.tv_rubbishtip);
            if (dataSource.get(position).getLajitype() == 0) {
                tvRubbishName.setText(dataSource.get(position).getName());
                imageView.setImageResource(R.drawable.rubbish_recycler);
                tvRubbishType.setText("可回收物");
                tvRubbishTip.setText(dataSource.get(position).getLajitip());
            } else if (dataSource.get(position).getLajitype() == 1) {
                tvRubbishName.setText(dataSource.get(position).getName());
                imageView.setImageResource(R.drawable.rubbish_harm);
                tvRubbishType.setText("有害垃圾");
                tvRubbishTip.setText(dataSource.get(position).getLajitip());
            } else if (dataSource.get(position).getLajitype() == 2) {
                tvRubbishName.setText(dataSource.get(position).getName());
                imageView.setImageResource(R.drawable.rubbish_certain);
                tvRubbishType.setText("湿垃圾");
                tvRubbishTip.setText(dataSource.get(position).getLajitip());
            } else if (dataSource.get(position).getLajitype() == 3) {
                tvRubbishName.setText(dataSource.get(position).getName());
                imageView.setImageResource(R.drawable.rubbish_gan);
                tvRubbishType.setText("干垃圾");
                tvRubbishTip.setText(dataSource.get(position).getLajitip());
            }
            //设置Popupwindow显示的内容视图
            popupWindow.setContentView(popupView);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
            popupView.setFocusable(true);
            View view_list = View.inflate(context, item_layout_id, null);
            popupWindow.setOnDismissListener(new poponDismissListener());
            popupWindow.showAtLocation(view_list.findViewById(R.id.result_ll), Gravity.CENTER, 0, 0);
        } else {
            Toast.makeText(activity, dataSource.get(position).getLajitip(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
//            backgroundAlpha(1f);
            setBackgroundAlpha(1f,context);
        }
    }
}
