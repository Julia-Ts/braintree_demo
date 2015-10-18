package com.yalantis.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.yalantis.R;

/**
 * Draws a line for each page. The current page line is colored differently
 * than the unselected page lines.
 */
abstract class BasePageIndicator extends View implements ViewPager.OnPageChangeListener {
    protected static final int INVALID_POINTER = -1;

    protected final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected ViewPager mViewPager;
    protected ViewPager.OnPageChangeListener mListener;
    protected int mCurrentPage;
    protected boolean mCentered;
    protected float mRadius;
    protected float mGapSize;

    protected int mTouchSlop;
    protected int mActivePointerId = INVALID_POINTER;
    protected boolean mIsDragging;

    public BasePageIndicator(Context context) {
        super(context);
        initIndicator(context, null, 0);
    }

    public BasePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIndicator(context, attrs, 0);
    }

    public BasePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initIndicator(context, attrs, defStyle);
    }

    private void initIndicator(Context context, AttributeSet attrs, int defStyle) {
        if (isInEditMode()) return;

        final Resources resources = getResources();

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator, defStyle, 0);

        mCentered = a.getBoolean(R.styleable.PageIndicator_centered, true);
        mRadius = a.getDimension(R.styleable.PageIndicator_radius, resources.getDimension(R.dimen.default_page_indicator_dot_radius));
        mGapSize = a.getDimension(R.styleable.PageIndicator_gapSize, resources.getDimension(R.dimen.default_page_indicator_gap_width));
        setStrokeWidth(a.getDimension(R.styleable.PageIndicator_strokeWidth, resources.getDimension(R.dimen.default_page_indicator_stroke_width)));
        mPaintUnselected.setColor(a.getColor(R.styleable.PageIndicator_unselectedColor, resources.getColor(R.color.colorPrimary)));
        // TODO: change to FILL if you need filled unselected indicators
        mPaintUnselected.setStyle(Paint.Style.STROKE);
        mPaintSelected.setColor(a.getColor(R.styleable.PageIndicator_selectedColor, Color.WHITE));
        // TODO: change to STROKE if you need not filled unselected indicator
        mPaintSelected.setStyle(Paint.Style.FILL);

        Drawable background = a.getDrawable(R.styleable.PageIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }

        a.recycle();

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }

    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }

    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public void setSelectedColor(int selectedColor) {
        mPaintSelected.setColor(selectedColor);
        invalidate();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    public void setStrokeWidth(float lineWidth) {
        mPaintSelected.setStrokeWidth(lineWidth);
        mPaintUnselected.setStrokeWidth(lineWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return mPaintSelected.getStrokeWidth();
    }

    public void setGapWidth(float gapWidth) {
        mGapSize = gapWidth;
        invalidate();
    }

    public float getGapWidth() {
        return mGapSize;
    }

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param viewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (mViewPager != null) {
            //Clear us from the old pager.
            mViewPager.setOnPageChangeListener(null);
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view
     * @param initialPosition
     */
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    /**
     * Set the current page of both the ViewPager and indicator.</p>
     * <p/>
     * This must be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item
     */
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    /**
     * Notify the indicator that the fragment list has changed.
     */
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    /**
     * Set a page change listener which will receive forwarded events.
     *
     * @param listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    protected int measureWidth(int measureSpec) {
        return 0;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    protected int measureHeight(int measureSpec) {
        return 0;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;

                    if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                        return true;
                    } else if ((mCurrentPage < count - 1) && (ev.getX() > halfWidth + sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        return true;
                    }
                }

                mIsDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
                return true;

            default:
                super.onTouchEvent(ev);
                return false;
        }
    }
}