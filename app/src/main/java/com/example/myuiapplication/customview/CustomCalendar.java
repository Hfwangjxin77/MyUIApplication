package com.example.myuiapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.example.myuiapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomCalendar extends View {

    private String TAG = "CustomCalendar";

    /** backgrounds*/
    private int mBgMonth, mBgWeek, mBgDay, mBgPre;
    /**  colors and text sizes for title*/
    private int mTextColorMonth;
    private float mTextSizeMonth;
    private int mMonthRowL, mMonthRowR;
    private float mMonthRowSpac;
    private float mMonthSpac;
    /** color and text size for text 'week'*/
    private int mTextColorWeek, mSelectWeekTextColor;
    private float mTextSizeWeek;
    /** color and text size for text 'date'*/
    private int mTextColorDay;
    private float mTextSizeDay;
    /** color and text size for text 'tasks'*/
    private int mTextColorPreFinish, mTextColorPreUnFinish, mTextColorPreNull;
    private float mTextSizePre;
    /** selected text color*/
    private int mSelectTextColor;
    /** selected text background color*/
    private int mSelectBg, mCurrentBg;
    private float mSelectRadius, mCurrentBgStrokeWidth;
    private float[] mCurrentBgDashPath;

    /** Line space*/
    private float mLineSpac;
    /** text space*/
    private float mTextSpac;

    private Paint mPaint;
    private Paint bgPaint;

    private float titleHeight, weekHeight, dayHeight, preHeight, oneHeight;
    private int columnWidth;       //column width

    private Date month; //current month
    private boolean isCurrentMonth;       //display whether the showing month is current month.
    private int currentDay, selectDay, lastSelectDay;    //current date, selected date and last time selected day

    private int dayOfMonth;    // days of the month
    private int firstIndex;    //the index for  the first day of the month
    private int todayWeekIndex;//weekday index.
    private int firstLineNum, lastLineNum; // the number of displaying dates for first line and last line
    private int lineNum;      //the number of date lines
    private String[] WEEK_STR = new String[]{"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat", };
    private ArrayList<Date> eventDates = new ArrayList<Date>();


    public CustomCalendar(Context context) {
        this(context, null);
    }
    public CustomCalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CustomCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCalendar, defStyleAttr, 0);

        mBgMonth = a.getColor(R.styleable.CustomCalendar_mBgMonth, Color.TRANSPARENT);
        mBgWeek = a.getColor(R.styleable.CustomCalendar_mBgWeek, Color.TRANSPARENT);
        mBgDay = a.getColor(R.styleable.CustomCalendar_mBgDay, Color.TRANSPARENT);
        mBgPre = a.getColor(R.styleable.CustomCalendar_mBgPre, Color.TRANSPARENT);

        mMonthRowL = a.getResourceId(R.styleable.CustomCalendar_mMonthRowL, R.mipmap.custom_calendar_row_left);
        mMonthRowR = a.getResourceId(R.styleable.CustomCalendar_mMonthRowR, R.mipmap.custom_calendar_row_right);
        mMonthRowSpac = a.getDimension(R.styleable.CustomCalendar_mMonthRowSpac, 20);
        mTextColorMonth = a.getColor(R.styleable.CustomCalendar_mTextColorMonth, Color.BLACK);
        mTextSizeMonth = a.getDimension(R.styleable.CustomCalendar_mTextSizeMonth, 100);
        mMonthSpac = a.getDimension(R.styleable.CustomCalendar_mMonthSpac, 20);
        mTextColorWeek = a.getColor(R.styleable.CustomCalendar_mTextColorWeek, Color.BLACK);
        mSelectWeekTextColor = a.getColor(R.styleable.CustomCalendar_mSelectWeekTextColor, Color.BLACK);

        mTextSizeWeek = a.getDimension(R.styleable.CustomCalendar_mTextSizeWeek, 70);
        mTextColorDay = a.getColor(R.styleable.CustomCalendar_mTextColorDay, Color.GRAY);
        mTextSizeDay = a.getDimension(R.styleable.CustomCalendar_mTextSizeDay, 70);
        mTextColorPreFinish = a.getColor(R.styleable.CustomCalendar_mTextColorPreFinish, Color.BLUE);
        mTextColorPreUnFinish = a.getColor(R.styleable.CustomCalendar_mTextColorPreUnFinish, Color.BLUE);
        mTextColorPreNull  = a.getColor(R.styleable.CustomCalendar_mTextColorPreNull, Color.BLUE);
        mTextSizePre = a.getDimension(R.styleable.CustomCalendar_mTextSizePre, 40);
        mSelectTextColor = a.getColor(R.styleable.CustomCalendar_mSelectTextColor, Color.YELLOW);
        mCurrentBg = a.getColor(R.styleable.CustomCalendar_mCurrentBg, Color.GRAY);
        try {
            int dashPathId = a.getResourceId(R.styleable.CustomCalendar_mCurrentBgDashPath, R.array.customCalendar_currentDay_bg_DashPath);
            int[] array = getResources().getIntArray(dashPathId);
            mCurrentBgDashPath = new float[array.length];
            for(int i=0;i<array.length;i++){
                mCurrentBgDashPath[i]=array[i];
            }
        }catch (Exception e){
            e.printStackTrace();
            mCurrentBgDashPath = new float[]{2, 3, 2, 3};
        }
        mSelectBg = a.getColor(R.styleable.CustomCalendar_mSelectBg, Color.YELLOW);
        mSelectRadius = a.getDimension(R.styleable.CustomCalendar_mSelectRadius, 20);
        mCurrentBgStrokeWidth = a.getDimension(R.styleable.CustomCalendar_mCurrentBgStrokeWidth, 5);
        mLineSpac = a.getDimension(R.styleable.CustomCalendar_mLineSpac, 20);
        mTextSpac = a.getDimension(R.styleable.CustomCalendar_mTextSpac, 20);
        a.recycle();

        initCompute();

    }
    /**计算相关常量，构造方法中调用*/
    private void initCompute(){
        mPaint = new Paint();
        bgPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        bgPaint.setAntiAlias(true); //抗锯齿

        map = new HashMap<>();

        //title height
        mPaint.setTextSize(mTextSizeMonth);
        titleHeight = getFontHeight(mPaint) + 2 * mMonthSpac;
        //week height
        mPaint.setTextSize(mTextSizeWeek);
        weekHeight = getFontHeight(mPaint);
        //date height
        mPaint.setTextSize(mTextSizeDay);
        dayHeight = getFontHeight(mPaint);
        //event height
        mPaint.setTextSize(mTextSizePre);
        preHeight = getFontHeight(mPaint);

        oneHeight = mLineSpac + dayHeight + mTextSpac + preHeight;

        //defaultposter month
        String cDateStr = getMonthStr(new Date());
//
        setMonth(cDateStr);
    }

    /**month setting*/
    private void setMonth(String Month){

        month = str2Date(Month);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        //get  the date of  today.
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        todayWeekIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;

        Date cM = str2Date(getMonthStr(new Date()));

        if(cM.getTime() == month.getTime()){
            isCurrentMonth = true;
            selectDay = currentDay;//当月默认选中当前日
        }else{
            isCurrentMonth = false;
            selectDay = 0;
        }
//
        calendar.setTime(month);
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        lineNum = 1;

        firstLineNum = 7-firstIndex;
        lastLineNum = 0;
        int shengyu = dayOfMonth - firstLineNum;
        while (shengyu>7){
            lineNum ++;
            shengyu-=7;
        }
        if(shengyu>0){
            lineNum ++;
            lastLineNum = shengyu;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        columnWidth = widthSize / 7;

        float height = titleHeight + weekHeight + (lineNum * oneHeight);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                (int)height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonth(canvas);
        drawWeek(canvas);
        drawDayAndPre(canvas);
    }


    private int rowLStart, rowRStart, rowWidth;
    private void drawMonth(Canvas canvas){
        //draw background
        bgPaint.setColor(mBgMonth);
        RectF rect = new RectF(0, 0, getWidth(), titleHeight);
        canvas.drawRect(rect, bgPaint);
        //draw month
        mPaint.setTextSize(mTextSizeMonth);
        mPaint.setColor(mTextColorMonth);
        float textLen = getFontlength(mPaint, getMonthStr(month));
        float textStart = (getWidth() - textLen)/ 2;
        canvas.drawText(getMonthStr(month), textStart,
                mMonthSpac+ getFontLeading(mPaint), mPaint);
        /*draw left  and right navigation button */
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowL);
        int h = bitmap.getHeight();
        rowWidth = bitmap.getWidth();
        //float left, float top
        rowLStart = (int)(textStart-2*mMonthRowSpac-rowWidth);
        canvas.drawBitmap(bitmap, rowLStart+mMonthRowSpac , (titleHeight - h)/2, new Paint());
        bitmap = BitmapFactory.decodeResource(getResources(), mMonthRowR);
        rowRStart = (int)(textStart+textLen);
        canvas.drawBitmap(bitmap, rowRStart+mMonthRowSpac, (titleHeight - h)/2, new Paint());
    }

    private void drawWeek(Canvas canvas){

        bgPaint.setColor(mBgWeek);
        RectF rect = new RectF(0, titleHeight, getWidth(), titleHeight + weekHeight);
        canvas.drawRect(rect, bgPaint);

        mPaint.setTextSize(mTextSizeWeek);

        for(int i = 0; i < WEEK_STR.length; i++){
            if(todayWeekIndex == i && isCurrentMonth){
                mPaint.setColor(mSelectWeekTextColor);
            }else{
                mPaint.setColor(mTextColorWeek);
            }
            int len = (int) getFontlength(mPaint, WEEK_STR[i]);
            int x = i * columnWidth + (columnWidth - len)/2;
            canvas.drawText(WEEK_STR[i], x, titleHeight + getFontLeading(mPaint), mPaint);
        }
    }

    private void drawDayAndPre(Canvas canvas){

        float top = titleHeight+weekHeight;

        for(int line = 0; line < lineNum; line++){
            if(line == 0){

                drawDayAndPre(canvas, top, firstLineNum, 0, firstIndex);
            }else if(line == lineNum-1){

                top += oneHeight;
                drawDayAndPre(canvas, top, lastLineNum, firstLineNum+(line-1)*7, 0);
            }else{

                top += oneHeight;
                drawDayAndPre(canvas, top, 7, firstLineNum+(line-1)*7, 0);
            }
        }
    }

    /**
     * drawing the month for some line.
     * @param canvas
     * @param top top position
     * @param count the number of dates drawing at this line
     * @param overDay drawn dates.
     * @param startIndex the index of first date at this line
     */
    private void drawDayAndPre(Canvas canvas, float top,
                               int count, int overDay, int startIndex){
//
        //background
        float topPre = top + mLineSpac + dayHeight;
        bgPaint.setColor(mBgDay);
        RectF rect = new RectF(0, top, getWidth(), topPre);
        canvas.drawRect(rect, bgPaint);

        bgPaint.setColor(mBgPre);
        rect = new RectF(0, topPre, getWidth(), topPre + mTextSpac + dayHeight);
        canvas.drawRect(rect, bgPaint);

        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = getFontLeading(mPaint);
        mPaint.setTextSize(mTextSizePre);
        float preTextLeading = getFontLeading(mPaint);

        for(int i = 0; i<count; i++){
            int left = (startIndex + i)*columnWidth;
            int day = (overDay+i+1);

            mPaint.setTextSize(mTextSizeDay);


            if(isCurrentMonth && currentDay == day){
                mPaint.setColor(mTextColorDay);
                bgPaint.setColor(mCurrentBg);
                bgPaint.setStyle(Paint.Style.STROKE);
                PathEffect effect = new DashPathEffect(mCurrentBgDashPath, 1);
                bgPaint.setPathEffect(effect);
                bgPaint.setStrokeWidth(mCurrentBgStrokeWidth);

                canvas.drawCircle(left+columnWidth/2, top + mLineSpac +dayHeight/2,
                        mSelectRadius-mCurrentBgStrokeWidth, bgPaint);
            }

            bgPaint.setPathEffect(null);
            bgPaint.setStrokeWidth(0);
            bgPaint.setStyle(Paint.Style.FILL);


            if(selectDay == day){

                mPaint.setColor(mSelectTextColor);
                bgPaint.setColor(mSelectBg);

                canvas.drawCircle(left+columnWidth/2, top + mLineSpac +dayHeight/2, mSelectRadius, bgPaint);
            }else{
                mPaint.setColor(mTextColorDay);
            }

            int len = (int) getFontlength(mPaint, day+"");
            int x = left + (columnWidth - len)/2;
            canvas.drawText(day+"", x, top + mLineSpac + dayTextLeading, mPaint);


            mPaint.setTextSize(mTextSizePre);
            DayFinish finish = map.get(day);
            String preStr = null;
            for(Date date : eventDates){
                if(getMonth(date) == getMonth(month)  && getDay(date) == day){
                    preStr = "event";
                }
            }


            if(preStr != null){
                mPaint.setColor(Color.GREEN);
                len = (int) getFontlength(mPaint, preStr);
                x = left + (columnWidth - len)/2;
                canvas.drawText(preStr, x, topPre + mTextSpac + preTextLeading, mPaint);
            }

        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }


    private String getMonthStr(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy MMM");
        return df.format(month);
    }
    private Date str2Date(String str){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy MMM");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    float getFontLeading(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading- fm.ascent;
    }

    Integer getMonth(Date date) {
        if (date == null)
            return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    Integer getDay(Date date) {
        if (date == null)
            return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    String DateToStr(Date date,String pattern) {

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        return str;
    }

    Date StrToDate(String str,String pattern) {

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /****************************event listeners↓↓↓↓↓↓↓****************************/

    private PointF focusPoint = new PointF();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_MOVE:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                focusPoint.set(event.getX(), event.getY());
                touchFocusMove(focusPoint, true);
                break;
        }
        return true;
    }


    public void touchFocusMove(final PointF point, boolean eventEnd) {
//        Log.e(TAG, "点击坐标：("+point.x+" ，"+point.y+"),事件是否结束："+eventEnd);


        if(point.y<=titleHeight){

            if(eventEnd && listener!=null){
                if(point.x>=rowLStart && point.x<(rowLStart+2*mMonthRowSpac+rowWidth)){

                    listener.onLeftRowClick();
                }else if(point.x>rowRStart && point.x<(rowRStart + 2*mMonthRowSpac+rowWidth)){

                    listener.onRightRowClick();
                }else if(point.x>rowLStart && point.x <rowRStart){
                    listener.onTitleClick(getMonthStr(month), month);
                }
            }
        }else if(point.y<=(titleHeight+weekHeight)){

            if(eventEnd && listener!=null){

                int xIndex = (int)point.x / columnWidth;

                if((point.x / columnWidth-xIndex)>0){
                    xIndex += 1;
                }
                if(listener!=null){
                    listener.onWeekClick(xIndex-1, WEEK_STR[xIndex-1]);
                }
            }
        }else{

            touchDay(point, eventEnd);
        }
    }


    private boolean responseWhenEnd = false;

    private void touchDay(final PointF point, boolean eventEnd){

        boolean availability = false;

        float top = titleHeight+weekHeight+oneHeight;
        int foucsLine = 1;
        while(foucsLine<=lineNum){
            if(top>=point.y){
                availability = true;
                break;
            }
            top += oneHeight;
            foucsLine ++;
        }
        if(availability){

            int xIndex = (int)point.x / columnWidth;
            if((point.x / columnWidth-xIndex)>0){
                xIndex += 1;
            }

            if(xIndex<=0)
                xIndex = 1;
            if(xIndex>7)
                xIndex = 7;

            if(foucsLine == 1){

                if(xIndex<=firstIndex){

                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(xIndex-firstIndex, eventEnd);
                }
            }else if(foucsLine == lineNum){
                //最后一行
                if(xIndex>lastLineNum){

                    setSelectedDay(selectDay, true);
                }else{
                    setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
                }
            }else{
                setSelectedDay(firstLineNum + (foucsLine-2)*7+ xIndex, eventEnd);
            }
        }else{

            setSelectedDay(selectDay, true);
        }
    }

    private void setSelectedDay(int day, boolean eventEnd){

        selectDay = day;
        invalidate();
        if(listener!=null && eventEnd && responseWhenEnd && lastSelectDay!=selectDay) {
            lastSelectDay = selectDay;
            listener.onDayClick(selectDay, getMonthStr(month) + selectDay + "th", map.get(selectDay));
        }
        responseWhenEnd = !eventEnd;
    }




    @Override
    public void invalidate() {
        requestLayout();
        super.invalidate();
    }


    private float getFontHeight(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    private float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /***********************interfaces API↓↓↓↓↓↓↓**************************/
    private Map<Integer, DayFinish> map;
    public void setRenwu(String month, List<DayFinish> list){
        setMonth(month);

        if(list!=null && list.size()>0){
            map.clear();
            for(DayFinish finish : list){
                map.put(finish.day, finish);
            }
        }
        invalidate();
    }
    public void setRenwu(List<DayFinish> list){
        if(list!=null && list.size()>0){
            map.clear();
            for(DayFinish finish : list){
                map.put(finish.day, finish);
            }
        }
        invalidate();
    }

    public void monthChange(int change){
        System.out.println("monthChange");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(month);
        calendar.add(Calendar.MONTH, change);
        setMonth(getMonthStr(calendar.getTime()));
        map.clear();
        invalidate();
    }

    public void addEventDate(Date eventDate) {

        if(eventDates != null){
            this.eventDates.add(eventDate);
        }
    }

    private onClickListener listener;
    public void setOnClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{

        public abstract void onLeftRowClick();
        public abstract void onRightRowClick();
        public abstract void onTitleClick(String monthStr, Date month);
        public abstract void onWeekClick(int weekIndex, String weekStr);
        public abstract void onDayClick(int day, String dayStr, DayFinish finish);
    }

    public class DayFinish{
        int day;
        int all;
        int finish;
        public DayFinish(int day, int finish, int all) {
            this.day = day;
            this.all = all;
            this.finish = finish;
        }
    }




    /***********************interfaces API↑↑↑↑↑↑↑**************************/

}
