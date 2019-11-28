package net.edrop.edrop_user.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.adapter.ServiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/25
 * Time: 16:39
 */
public class ServicePageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private List<Map<String, Object>> dataSource = null;
    private ListView listView;
    private View view;

    public static ServicePageFragment newInstance(String sectionNumber) {
        ServicePageFragment fragment = new ServicePageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_page, container, false);

        InitData();
        findView();
        setAdapter();
        return view;
    }

    //找到控件对象
    private void findView() {
        listView = view.findViewById(R.id.lv_service);

    }

    //将数据传入adapter
    private void setAdapter() {
        ServiceAdapter adapter = new ServiceAdapter(
                getActivity(),
                dataSource,
                R.layout.fragment_service_page_item
        );
        listView.setAdapter(adapter);
    }


    //初始化数据
    private void InitData() {
        String[] arr = {"今日预约", "我的预约", "社区", "我的钱包"};
        dataSource = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            Map map = new HashMap<String, Object>();
            map.put("text", arr[i]);
            map.put("img", R.drawable.right);
            dataSource.add(map);
        }

    }
}
