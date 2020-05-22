package net.edrop.edrop_user.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CommunityGridView extends GridView {
    private Context context;
    public CommunityGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CommunityGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CommunityGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }


    /**
     * ListView嵌套GridView，GridView显示不全问题的解决
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}