package com.example.myuiapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

public class MyHorizontalScrollView extends HorizontalScrollView {

    public static final float DEFAULT_LEFT_SLIDE_VIEW_PADDING = 50;


    // main container
    private LinearLayout mLinearLayout;
    private ViewGroup mLeftSlideView;
    private ViewGroup mMainView;
    private int mScreenWidth;
    private float mLeftSlideViewPadding;


    public MyHorizontalScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        /*搞清楚 dip,px,pt,sp的区别*/
        mLeftSlideViewPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,DEFAULT_LEFT_SLIDE_VIEW_PADDING,
                context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }
}
