package com.rd.views.filter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rd.views.R;
import com.rd.views.filter.adapter.MenuAdapter;
import com.rd.views.filter.util.UIUtil;

import java.util.Arrays;
import java.util.List;

public class FixedTabIndicator extends LinearLayout {
    private Context context;
    /** 分割线 */
    private Paint   mDividerPaint;
    /** 分割线颜色 */
    private int mDividerColor   = Color.WHITE;
    /** 分割线距离上下padding */
    private int mDividerPadding = 13;
    /** 上下两条线 */
    private Paint mLinePaint;
    private float mLineHeight       = 1;
    private int   mLineColor        = Color.WHITE;
    /** 指针文字的大小,sp */
    private int   mTabTextSize      = 13;
    /** 未选中默认颜色 */
    private int   mTabDefaultColor  = Color.WHITE;
    /** 指针选中颜色 */
    private int   mTabSelectedColor = Color.WHITE;
    private int   drawableRight     = 10;
    private int                 measureHeight;
    private int                 measuredWidth;
    /** 设置的条目数量 */
    private int                 mTabCount;
    /** 上一个指针选中条目 */
    private int                 mCurrentIndicatorPosition;
    /** 上一个指针选中条目 */
    private int                 mLastIndicatorPosition;
    private OnItemClickListener mOnItemClickListener;

    public FixedTabIndicator(Context context) {
        this(context, null);
    }

    public FixedTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FixedTabIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 条目点击事件
     */
    public interface OnItemClickListener {
        /**
         * 回调方法
         *
         * @param v
         *         当前点击的view
         * @param position
         *         当前点击的position
         * @param open
         *         当前的状态，蓝色为open,筛选器为打开状态
         */
        void onItemClick(View v, int position, boolean open);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    private void init(Context context) {
        this.context = context;
        mDividerColor = ContextCompat.getColor(getContext(), R.color.filter_tab_line);
        mLineColor = ContextCompat.getColor(getContext(), R.color.filter_tab_line);
        mTabDefaultColor = ContextCompat.getColor(getContext(), R.color.filter_tab_text_normal);
        mTabSelectedColor = ContextCompat.getColor(getContext(), R.color.filter_tab_text_selected);

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.filter_tab_background));
        setWillNotDraw(false);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setColor(mDividerColor);

        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);

        mDividerPadding = UIUtil.dp(context, mDividerPadding);
        drawableRight = UIUtil.dp(context, drawableRight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 分割线的个数比tab的个数少一个
        for (int i = 0; i < mTabCount - 1; ++i) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }

            canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), measureHeight - mDividerPadding, mDividerPaint);
        }

        // 上边黑线
        canvas.drawRect(0, 0, measuredWidth, mLineHeight, mLinePaint);

        // 下边黑线
        canvas.drawRect(0, measureHeight - mLineHeight, measuredWidth, measureHeight, mLinePaint);
    }

    /**
     * 添加相应的布局进此容器
     */
    public void setTitles(List<String> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("条目数量为空");
        }
        this.removeAllViews();

        mTabCount = list.size();
        for (int pos = 0; pos < mTabCount; ++pos) {
            addView(generateTextView(list.get(pos), pos));
        }

        postInvalidate();
    }

    public void setTitles(String[] list) {
        setTitles(Arrays.asList(list));
    }

    public void setTitles(MenuAdapter menuAdapter) {
        if (menuAdapter == null) {
            return;
        }
        this.removeAllViews();

        mTabCount = menuAdapter.getMenuCount();
        for (int pos = 0; pos < mTabCount; ++pos) {
            addView(generateTextView(menuAdapter.getMenuTitle(pos), pos));
        }
        postInvalidate();
    }

    private void switchTab(int pos) {
        TextView tv = getChildAtCurPos(pos);

        Drawable drawable = tv.getCompoundDrawables()[2];
        int      level    = drawable.getLevel();

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(tv, pos, level == 1);
        }

        if (mLastIndicatorPosition == pos) {
            // 点击同一个条目时
            tv.setTextColor(level == 0 ? mTabSelectedColor : mTabDefaultColor);
            drawable.setLevel(1 - level);

            return;
        }

        mCurrentIndicatorPosition = pos;
        resetPos(mLastIndicatorPosition);

        //highLightPos(pos);
        tv.setTextColor(mTabSelectedColor);
        tv.getCompoundDrawables()[2].setLevel(1);

        mLastIndicatorPosition = pos;
    }

    /**
     * 重置字体颜色
     */
    public void resetPos(int pos) {
        TextView tv = getChildAtCurPos(pos);
        tv.setTextColor(mTabDefaultColor);
        tv.getCompoundDrawables()[2].setLevel(0);
    }

    /**
     * 重置当前字体颜色
     */
    public void resetCurrentPos() {
        resetPos(mCurrentIndicatorPosition);
    }

    /**
     * 获取当前pos内的TextView
     */
    public TextView getChildAtCurPos(int pos) {
        return (TextView) ((ViewGroup) getChildAt(pos)).getChildAt(0);
    }

    /**
     * 直接用TextView使用weight不能控制图片，需要用用父控件包裹
     */
    private View generateTextView(String title, int pos) {
        // 子空间TextView
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabTextSize);
        tv.setTextColor(mTabDefaultColor);
        tv.setSingleLine();
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMaxEms(6);//限制4个字符
        // Drawable drawable = getResources().getDrawable(R.drawable.level_filter);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.level_filter);
        tv.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        tv.setCompoundDrawablePadding(drawableRight);

        // 将TextView添加到父控件RelativeLayout
        RelativeLayout              rl       = new RelativeLayout(context);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(-2, -2);
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl.addView(tv, rlParams);
        rl.setId(pos);

        // 再将RelativeLayout添加到LinearLayout中
        LayoutParams params = new LayoutParams(-1, -1);
        params.weight = 1;
        params.gravity = Gravity.CENTER;
        rl.setLayoutParams(params);

        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置点击事件
                switchTab(v.getId());
            }
        });

        return rl;
    }

    /**
     * 高亮字体颜色
     */
    public void highLightPos(int pos) {
        TextView tv = getChildAtCurPos(pos);
        tv.setTextColor(mTabSelectedColor);
        tv.getCompoundDrawables()[2].setLevel(1);
    }

    public int getCurrentIndicatorPosition() {
        return mCurrentIndicatorPosition;
    }

    public void setCurrentText(String text) {
        setPositionText(mCurrentIndicatorPosition, text);
    }

    public void setPositionText(int position, String text) {
        if (position < 0 || position > mTabCount - 1) {
            throw new IllegalArgumentException("position 越界");
        }
        TextView tv = getChildAtCurPos(position);
        tv.setTextColor(mTabDefaultColor);
        tv.setText(text);
        tv.getCompoundDrawables()[2].setLevel(0);
    }

    public int getLastIndicatorPosition() {
        return mLastIndicatorPosition;
    }
}