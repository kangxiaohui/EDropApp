package net.edrop.edrop_user.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import net.edrop.edrop_user.MyApplication;
import net.edrop.edrop_user.R;
import net.edrop.edrop_user.activity.GrabageQuestionsActivity;
import net.edrop.edrop_user.activity.Main2Activity;
import net.edrop.edrop_user.activity.RubbishDesc01Activity;
import net.edrop.edrop_user.activity.RubbishDesc02Activity;
import net.edrop.edrop_user.activity.RubbishDesc03Activity;
import net.edrop.edrop_user.activity.RubbishDesc04Activity;
import net.edrop.edrop_user.activity.SearchRubblishActivity;
import net.edrop.edrop_user.entity.ImageInfo;
import net.edrop.edrop_user.utils.ImageCarousel;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment implements TabHost.TabContentFactory, GestureDetector.OnGestureListener {
    private Activity activity;
    private View view;
    //搜索框控件
    private Button search;
    // 图片轮播控件
    private ViewPager mViewPager;
    private TextView mTvPagerTitle;
    private LinearLayout mLineLayoutDot;
    private ImageCarousel imageCarousel;
    private List<View> dots;//小点
    // 图片数据，包括图片标题、图片链接、数据、点击要打开的网站（点击打开的网页或一些提示指令）
    private List<ImageInfo> imageInfoList;
    //定义手势检测器实例
    private GestureDetector detector;

    private Main2Activity main2Activity;
    private LinearLayout recyclable;
    private LinearLayout hazardous;
    private LinearLayout housefood;
    private LinearLayout residoual;
    private ImageView questions;
    private static final String SECTION_STRING = "fragment_string";

    public static HomePageFragment newInstance(String sectionNumber) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        //创建手势检测器
        detector = new GestureDetector(getActivity(),this);
        Main2Activity.MyOnTouchListener myOnTouchListener = new Main2Activity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = detector.onTouchEvent(ev);
                return result;
            }
        };
        main2Activity=(Main2Activity)getActivity();
        ((Main2Activity) getActivity()).registerMyOnTouchListener(myOnTouchListener);

        initView();
        initEvent();
        setListener();
        imageStart();
        return view;
    }

    @Nullable
    @Override
    //在Fragment中直接使用getContext方法容易产生空指针异常，覆写getContext方法
    public Context getContext() {
        activity = getActivity();
        if (activity == null) {
            return MyApplication.getInstance2();
        }
        return activity;
    }

    private void initView(){
        questions=view.findViewById(R.id.questions);
        //搜索框
        search=view.findViewById(R.id.search);
        //轮播图
        mViewPager =view.findViewById(R.id.viewPager);
        mTvPagerTitle = view.findViewById(R.id.tv_pager_title);
        mLineLayoutDot = view.findViewById(R.id.lineLayout_dot);
        recyclable=view.findViewById(R.id.ll_rubbish_recyclable);
        hazardous=view.findViewById(R.id.ll_rubbish_hazardous);
        housefood=view.findViewById(R.id.ll_rubbish_housefood);
        residoual =view.findViewById(R.id.ll_rubbish_residoual);
        Fresco.initialize(getContext());
    }

    private void setListener(){
        //搜索框
        search.setOnClickListener(new CustomClickListener());
        //可回收垃圾介绍
        recyclable.setOnClickListener(new CustomClickListener());
        //有害垃圾介绍
        hazardous.setOnClickListener(new CustomClickListener());
        //湿垃圾介绍
        housefood.setOnClickListener(new CustomClickListener());
        //干垃圾介绍
        residoual.setOnClickListener(new CustomClickListener());
        //答题
        questions.setOnClickListener(new CustomClickListener());
    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }

    private class CustomClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.search:
                    Intent intent = new Intent(getActivity(), SearchRubblishActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.ll_rubbish_recyclable:
                    Intent intent1 = new Intent(getActivity(),RubbishDesc01Activity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    break;
                case R.id.ll_rubbish_hazardous:
                    Intent intent2 = new Intent(getActivity(),RubbishDesc03Activity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
                case R.id.ll_rubbish_housefood:
                    Intent intent3 = new Intent(getActivity(),RubbishDesc04Activity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);
                    break;
                case R.id.ll_rubbish_residoual:
                    Intent intent4 = new Intent(getActivity(),RubbishDesc02Activity.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent4);
                    break;
                case R.id.questions:
                    Intent intent5 = new Intent(getActivity(),GrabageQuestionsActivity.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent5);
                    break;
            }
        }
    }

    /*---------------------------------------------设置图片轮播-------------------------------------------------*/
    //初始化事件
    private void initEvent() {
        imageInfoList = new ArrayList<>();
        imageInfoList.add(new ImageInfo(1, "垃圾分类，保护环境", "", getResourcesUri(R.drawable.lunbo1), "https://www.baidu.com/"));
        imageInfoList.add(new ImageInfo(2, "绿色出行", "", getResourcesUri(R.drawable.lunbo2), "https://www.baidu.com/"));
        imageInfoList.add(new ImageInfo(3, "那不该是生命中的最后一棵树", "", getResourcesUri(R.drawable.lunbo3), "https://www.baidu.com/"));
        imageInfoList.add(new ImageInfo(4, "节约能源，保护环境", "", getResourcesUri(R.drawable.lunbo4), "https://www.baidu.com/"));
        imageInfoList.add(new ImageInfo(5, "绿色家园", "", getResourcesUri(R.drawable.lunbo5), "https://www.baidu.com/"));
    }

    //获取图片资源的绝对路径
    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    //轮播
    private void imageStart() {
        int[] imgaeIds = new int[]{R.id.pager_image1, R.id.pager_image2, R.id.pager_image3, R.id.pager_image4, R.id.pager_image5, R.id.pager_image6, R.id.pager_image7, R.id.pager_image8};
        String[] titles = new String[imageInfoList.size()];
        List<SimpleDraweeView> simpleDraweeViewList = new ArrayList<>();

        for (int i = 0; i < imageInfoList.size(); i++) {
            titles[i] = imageInfoList.get(i).getTitle();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getContext());
            simpleDraweeView.setAspectRatio(1.78f);
            // 设置一张默认的图片
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(this.getResources())
                    .setPlaceholderImage(ContextCompat.getDrawable(getContext(),
                            R.drawable.defult),
                            ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            simpleDraweeView.setHierarchy(hierarchy);
            simpleDraweeView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

            //加载高分辨率图片;
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageInfoList.get(i).getImage()))
                    .setResizeOptions(new ResizeOptions(1280, 720))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
//                    .setLowResImageRequest(ImageRequest.fromUri(Uri.parse(listItemBean.test_pic_low))) //在加载高分辨率图片之前加载低分辨率图片
                    .setImageRequest(imageRequest)
                    .setOldController(simpleDraweeView.getController())
                    .build();
            simpleDraweeView.setController(controller);

            simpleDraweeView.setId(imgaeIds[i]);//给view设置id
            simpleDraweeView.setTag(imageInfoList.get(i));
            simpleDraweeView.setOnClickListener(new CustomClickListener());
            titles[i] = imageInfoList.get(i).getTitle();
            simpleDraweeViewList.add(simpleDraweeView);

        }

        dots = addDots(mLineLayoutDot,
                fromResToDrawable(getContext(), R.drawable.ic_dot_focused),
                simpleDraweeViewList.size());
        imageCarousel = new ImageCarousel(getContext(), mViewPager, mTvPagerTitle, dots, 5000);
        imageCarousel.init(simpleDraweeViewList, titles).startAutoPlay();
        imageCarousel.start();
    }

    /**
     * 动态添加一个点
     *
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount    设置
     * @return 小点的Id
     */
    private int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(getContext());
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4, 0, 4, 0);
        dot.setLayoutParams(dotParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            dot.setBackground(backgount);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dot.setId(View.generateViewId());
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.addView(dot);
            }
        });

        return dot.getId();
    }

    /**
     * 资源图片转Drawable
     *
     * @param context 上下文
     * @param resId   资源ID
     * @return 返回Drawable图像
     */
    public static Drawable fromResToDrawable(Context context, int resId) {
        return ContextCompat.getDrawable(context, resId);
        //return context.getResources().getDrawable(resId);
    }

    /**
     * 添加多个轮播小点到横向线性布局
     *
     * @param linearLayout 线性横向布局
     * @param backgount    小点资源图标
     * @param number       数量
     * @return 返回小点View集合
     */
    private List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number) {
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout, backgount);
            dots.add(view.findViewById(dotId));
        }
        return dots;
    }

    /*----------------------------手势滑动-------------------------------------*/
    public void flingLeft() {//自定义方法：处理向左滑动事件
        main2Activity.OpenLeftMenu();
    }

    public void flingRight() {//自定义方法：处理向右滑动事件

    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getX() - e2.getX() < -300) {
                flingLeft();
                return true;
            } else if (e1.getX() - e2.getX() > 300) {
                flingRight();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) { }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) { }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

}
