package com.example.mmclient.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.example.mmclient.R;
import com.example.mmclient.R.id;
import com.example.mmclient.R.layout;
import com.example.mmclient.fragment.SummaryScreenSlideFragment;

/**
 * Activity for Summary screen.
 * 
 * @author SWAPNIL
 * 
 */
public class SummaryActivity extends FragmentActivity {

	// Number of slides in this screen. One for individual and another for group
	// summary screen.
	private static final int NUM_SLIDES = 2;

	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

		viewPager = (ViewPager) findViewById(R.id.pager);
		pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						invalidateOptionsMenu();
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Screen Slide adapter with fragment which displays Individual and group
	 * summary screen.
	 * 
	 * @author SWAPNIL
	 * 
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return SummaryScreenSlideFragment.create(position);
		}

		@Override
		public int getCount() {
			return NUM_SLIDES;
		}
	}
}
