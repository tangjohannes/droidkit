package org.droidkit.widget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class HandyListView extends ListView implements StoppableScrollView {
	
	public interface OnSizeChangedListener {
		public void onSizeChanged(HandyListView listView, int w, int h, int oldw, int oldh);
	}
	
//	private View mEmptyListView = null;
//	private View mLoadingListView = null;
	private WeakReference<OnSizeChangedListener> mSizeListener = null;
	private int mFadingEdgeColor = -1;
	private boolean mIsBeingTouched = false;
	private boolean mScrollingAllowed = true;
//	private int mListViewHiddenValue = View.GONE;
	
	private ArrayList<WeakReference<StoppableScrollView>> mStoppableScrollViews = new ArrayList<WeakReference<StoppableScrollView>>();
	private int mTouchSlop;
	private float mLastMotionY;

	public HandyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHandyListView();
	}

	public HandyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHandyListView();
	}

	public HandyListView(Context context) {
		super(context);
		initHandyListView();
	}
	
	private void initHandyListView() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
	}
	
    public void addStoppableScrollView(StoppableScrollView stoppableScrollView) {
        if (stoppableScrollView != null) {
            mStoppableScrollViews.add(new WeakReference<StoppableScrollView>(stoppableScrollView));
        }
    }
    
    private void setStoppableScrollingAllowed(boolean allowed) {
        for (WeakReference<StoppableScrollView> ref : mStoppableScrollViews) {
            StoppableScrollView sv = ref.get();
            if (sv != null) {
                if (allowed)
                    sv.allowScrolling();
                else
                    sv.stopScrolling();
            }
        }
    }	
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollingAllowed)
            return false;
        
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = ev.getY();
                break;    
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                int dy = (int)Math.abs(y - mLastMotionY);
                if (dy > mTouchSlop) {
                    setStoppableScrollingAllowed(false);
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setStoppableScrollingAllowed(true);
                break;
        }
        
        return super.onInterceptTouchEvent(ev);
    }
    
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
	    if (!mScrollingAllowed)
	        return false;
	    
	    int action = ev.getAction() & MotionEvent.ACTION_MASK;
	    
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingTouched = true;
                mLastMotionY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mIsBeingTouched = true;
                float y = ev.getY();
                int dy = (int)Math.abs(y - mLastMotionY);
                if (dy > mTouchSlop) {
                    setStoppableScrollingAllowed(false);
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingTouched = false;
                setStoppableScrollingAllowed(true);
                break;
        }
        return super.onTouchEvent(ev);
    }

	public boolean isBeingTouched() {
	    return mIsBeingTouched;
	}
    /**
	 * accepts View.INVISIBLE or View.GONE, default is View.GONE
	 * might not work with empty view
	 * @param hiddenValue
	 */
//	public void setListViewHiddenValue(int hiddenValue) {
//		mListViewHiddenValue = hiddenValue;
//	}
//
//	@Override
//	public void setAdapter(ListAdapter adapter) {
//		if (mLoadingListView != null)
//			mLoadingListView.setVisibility(View.GONE);
//		setVisibility(View.VISIBLE);
//		super.setEmptyView(mEmptyListView);
//		mEmptyListView = null;
//		super.setAdapter(adapter);
//		
//	}
//
//	@Override
//	public View getEmptyView() {
//		if (mEmptyListView != null)
//			return mEmptyListView;
//		return super.getEmptyView();
//	}
//
//	@Override
//	public void setEmptyView(View emptyView) {
//		mEmptyListView = emptyView;
//		if (mEmptyListView != null)
//			mEmptyListView.setVisibility(View.GONE);
//	}
//	
//	public void setLoadingListView(View loadingView) {
//		mLoadingListView = loadingView;
//		if (mLoadingListView != null) {
//			setVisibility(mListViewHiddenValue);
//			mLoadingListView.setVisibility(View.VISIBLE);
//		}
//	}
	
	public void setOnSizeChangedListener(OnSizeChangedListener listener) {
		mSizeListener = new WeakReference<HandyListView.OnSizeChangedListener>(listener);
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
        if (mSizeListener != null) {
            OnSizeChangedListener listener = mSizeListener.get();
            if (listener != null)
                listener.onSizeChanged(this, w, h, oldw, oldh);
        }
	}
	
	public ListAdapter getBaseAdapter() {
		ListAdapter adapter = super.getAdapter();
		if (adapter instanceof HeaderViewListAdapter) 
			return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
		return adapter;
	}

	public void setFadingEdgeColor(int color) {
		mFadingEdgeColor = color;
	}
	
	@Override
	public int getSolidColor() {
		if (mFadingEdgeColor == -1)
			return super.getSolidColor();
		return mFadingEdgeColor;
	}

    @Override
    public void stopScrolling() {
       mScrollingAllowed = false;
    }

    @Override
    public void allowScrolling() {
        mScrollingAllowed = true;
    }

    @Override
    public boolean isScrollingAllowed() {
        return mScrollingAllowed;
    }


    
    
	
}
