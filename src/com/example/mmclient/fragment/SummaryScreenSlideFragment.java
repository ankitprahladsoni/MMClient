package com.example.mmclient.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mmclient.R;

/**
 * Fragment for Individual and Group Summary screens. First screen will be for
 * Individual Summary screen and second will be for Group Summary screen.
 * 
 * @author SWAPNIL
 * 
 */
public class SummaryScreenSlideFragment extends Fragment {

	public static final String ARG_PAGE = "page";
	private int pageNumber;

	public static SummaryScreenSlideFragment create(int pageNumber) {
		SummaryScreenSlideFragment fragment = new SummaryScreenSlideFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SummaryScreenSlideFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNumber = getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);

		// First screen is for Individual Summary screen.
		if (0 == pageNumber) {
			((TextView) rootView.findViewById(R.id.slideTitleId))
					.setText("Individual Summary");
		}
		// Second screen is for Group Summary screen.
		else if (1 == pageNumber) {
			((TextView) rootView.findViewById(R.id.slideTitleId))
					.setText("Group Summary");
		}
		return rootView;
	}

	public int getPageNumber() {
		return pageNumber;
	}
}
