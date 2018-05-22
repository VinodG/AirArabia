package com.winit.airarabia.utils;
import java.lang.reflect.Field;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class AutoScrollViewPager extends ViewPager
{
	private int SCROLL_DURATION = 2000;
	private int TRANSITION_DELAY = 1500;
	private int scrolledPageCount=0;
	private int maxPagesCount = 0;
	private boolean isForward = true;

	public AutoScrollViewPager(Context context) {
		super(context);
		changePagerScroller(context);
	}

	public AutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		changePagerScroller(context);
	}

	private void changePagerScroller(Context mContext) {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			ViewPagerScroller scroller = new ViewPagerScroller(mContext);
			mScroller.set(this, scroller);
		} catch (Exception e) {
			LogUtils.infoLog("Scroller : ", "error of change scroller ");
		}
	}

	/**This method is used to start the auto scrolling of pages in the pager*/
	public void startAutoScrollPager(final AutoScrollViewPager pager)
	{
		maxPagesCount = this.getAdapter().getCount();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(scrolledPageCount < maxPagesCount  && isForward)
				{
					scrolledPageCount++;
					if(scrolledPageCount == maxPagesCount)
					{
						isForward = false;
						scrolledPageCount--;
					}
				}
				else if(!isForward)
				{
					scrolledPageCount--;
					if(scrolledPageCount == 0)
						isForward = true;
				}
				pager.setCurrentItem(scrolledPageCount, true);
				
//				pager.setPageTransformer(true, new ZoomOutPageTransformer());
				pager.setPageTransformer(false, new ViewPager.PageTransformer() {

					@Override
					public void transformPage(View page, float position) {
						final float normalizedposition = Math.abs(Math.abs(position) - 1);
						 page.setScaleX(normalizedposition / 2 + 0.5f);
						    page.setScaleY(normalizedposition / 2 + 0.5f);
					}
				});
				
				pager.setPageTransformer(true, new ZoomOutPageTransformer());
				startAutoScrollPager(pager);
			}
		}, TRANSITION_DELAY);
	}

	class ViewPagerScroller extends Scroller {

		public ViewPagerScroller(Context context) {
			super(context);
		}

		public ViewPagerScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
		}
	}
}

class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}

class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}