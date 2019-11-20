package com.example.myuiapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myuiapplication.R;

import org.w3c.dom.Text;

/**
 *  自定义布局文件
 *  1.多个控件组合在一个layout中形成一个整体的新组件
 *  2.自定义属性 （在attrs.xml中添加属性，绑定相关控件，在layout布局中使用）
 *  3.自定义方法和事件
 */
public class CustomItemView extends LinearLayout {

    TextView tvLeft;
    TextView tvRight;
    ImageView arrow;
    RelativeLayout rl;

    public CustomItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_item_view,this);
        rl = (RelativeLayout) findViewById(R.id.rl);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);
        arrow = (ImageView) findViewById(R.id.ivArrow);

        //自定义属性与控件绑定
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CustomItemView);
        String leftText = ta.getString(R.styleable.CustomItemView_tvLeft_text);
        String rightText = ta.getString(R.styleable.CustomItemView_tvRight_text);
        boolean showArrow = ta.getBoolean(R.styleable.CustomItemView_arrow_Visible,true);
        this.setView(leftText,rightText,showArrow);
    }

    /**
     * set contents for item view
     * @param tvLeftText
     * @param tvRightText
     * @param showArrow
     */
    public void setView(String tvLeftText,String tvRightText,boolean showArrow){

        if(this.tvLeft != null){

            this.tvLeft.setText(tvLeftText);
        }
        if(this.tvRight != null){

            this.tvRight.setText(tvRightText);
        }
        if(this.arrow != null){

            int Visibility = showArrow ? View.VISIBLE : View.GONE;
            this.arrow.setVisibility(Visibility);
        }
    }

    //1.设置点击事件

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.rl.setOnClickListener(l);
    }


    //2.获取左边内容
    public String getLeftContentText(){

        if(tvLeft!=null){
            return tvLeft.getText().toString();
        }else{
            return null;
        }

    }

    public void setLeftContentText(String leftContent){

        if(tvLeft!=null){
            tvLeft.setText(leftContent);
        }
    }

    //3.获取右边内容

    public String getRightContentText(){

        if(tvLeft!=null){
            return tvLeft.getText().toString();
        }else{
            return null;
        }

    }

    public void setRightContentText(String rightContent){

        if(tvLeft!=null){
            tvRight.setText(rightContent);
        }
    }

}
