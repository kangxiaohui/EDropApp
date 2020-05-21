package net.edrop.edrop_user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.entity.Rubbish;
import net.edrop.edrop_user.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/29
 * Time: 17:13
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<String> list;
    private View inflate;
    private Context context;
    private View popupView = null;
    private PopupWindow popupWindow = null;
    private Activity activity;
    private List<Rubbish> dataSource = new ArrayList<>();
    private TextView tvRubbishName;
    private ImageView imageView;
    private TextView tvRubbishType;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    public SearchAdapter(List<String> list, Context context, Activity activity, List<Rubbish> dataSource) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.dataSource = dataSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.content.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if (popupWindow == null || !popupWindow.isShowing()) {
                    SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(context,"searchHistory");
                    SharedPreferences.Editor editor = sharedPreferences.getEditor();
                    String history = sharedPreferences.getString("history", "");
                    if (history.equals("")){
                        editor.putString("history",list.get(position));
                    }else {
                        editor.putString("history",history+","+list.get(position));
                    }
                    editor.commit();
                    //显示PopupWindow
                    showPopupWindow(position);
//                    Log.e("test", dataSource.get(position).toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showPopupWindow(int position) {
        //创建popupwindow对象
        backgroundAlpha(0.5f);
        popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //通过布局填充器创建View
        popupView = View.inflate(context, R.layout.item_popupwindow_searchrubbish_layout, null);
        tvRubbishName = popupView.findViewById(R.id.tv_rubbishname);
        imageView = popupView.findViewById(R.id.iv_rubbish);
        tvRubbishType = popupView.findViewById(R.id.tv_rubbish);
        if (dataSource.get(position).getTypeId() == 1) {
            tvRubbishName.setText(dataSource.get(position).getName());
            imageView.setImageResource(R.drawable.rubbish_recycler);
            tvRubbishType.setText("可回收物");
        }else if (dataSource.get(position).getTypeId() == 2){
            tvRubbishName.setText(dataSource.get(position).getName());
            imageView.setImageResource(R.drawable.rubbish_harm);
            tvRubbishType.setText("有害垃圾");
        }else if (dataSource.get(position).getTypeId() == 3){
            tvRubbishName.setText(dataSource.get(position).getName());
            imageView.setImageResource(R.drawable.rubbish_certain);
            tvRubbishType.setText("湿垃圾");
        }else if (dataSource.get(position).getTypeId() == 4){
            tvRubbishName.setText(dataSource.get(position).getName());
            imageView.setImageResource(R.drawable.rubbish_gan);
            tvRubbishType.setText("干垃圾");
        }
        //设置Popupwindow显示的内容视图
        popupWindow.setContentView(popupView);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupView.setFocusable(true);
        View view_list = View.inflate(context, R.layout.item_search_result, null);
        popupWindow.setOnDismissListener(new poponDismissListener());
        popupWindow.showAtLocation(view_list.findViewById(R.id.ll), Gravity.CENTER, 0, 0);
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /***
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
