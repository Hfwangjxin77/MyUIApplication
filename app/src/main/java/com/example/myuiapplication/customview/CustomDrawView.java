package com.example.myuiapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.myuiapplication.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * TODO: document your custom view class.
 */
public class CustomDrawView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private Paint mDialPlatePaint;
    private float mTextWidth;
    private float mTextHeight;

    public CustomDrawView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomDrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomDrawView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CustomDrawView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CustomDrawView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CustomDrawView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CustomDrawView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CustomDrawView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        //init mDialPlatePaint
        mDialPlatePaint = new Paint();
        mDialPlatePaint.setColor(Color.BLUE);
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ViewHelper.getAlpha(this);
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

//        // Draw the example drawable on top of the text.
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }
        drawDialPlate(canvas);
    }


    private void drawDialPlate(Canvas canvas){

        //draw half circle
        float x = (getWidth() - getHeight() / 2) / 2;
        float y = getHeight() / 4;

        RectF oval = new RectF( x, y,
                getWidth() - x, getHeight() - y);
//        mDialPlatePaint.setColor(Color.BLACK);
//        canvas.drawRect(oval,mDialPlatePaint);

        mDialPlatePaint.setColor(Color.BLUE);
        mDialPlatePaint.setStrokeWidth(10);
        mDialPlatePaint.setAntiAlias(true);
        mDialPlatePaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval,-180,180,false,mDialPlatePaint);
        float lineY = (oval.bottom-oval.top)/2 + oval.top;
        canvas.drawLine(oval.left,lineY,oval.right,lineY,mDialPlatePaint);
        drawDial(canvas,179,oval);
    }

    private void drawDial(Canvas canvas,int dialNum,RectF oval){

        mDialPlatePaint.setColor(Color.BLACK);
        mDialPlatePaint.setStrokeWidth(5);
        int degree = 180/(dialNum+1);
        float lineY = (oval.bottom-oval.top)/2 + oval.top;
        float centerX = oval.left + (oval.right-oval.left)/2;
        float centerY = oval.top + (oval.bottom-oval.top)/2;

        for(int i = 0 ; i < dialNum ; i ++){
            canvas.rotate(degree,centerX,centerY);
            canvas.drawLine(oval.left,lineY,oval.left+10,lineY,mDialPlatePaint);
        }

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
