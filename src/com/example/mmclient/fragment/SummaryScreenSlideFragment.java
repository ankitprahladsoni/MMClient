package com.example.mmclient.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mmclient.R;
import com.example.mmclient.domain.Transaction;

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

	private static List<Transaction> individualTransactions = new ArrayList<Transaction>();

	static {
		Transaction transaction1 = new Transaction();
		transaction1.setName("One Individual");
		transaction1.setTotalAmount(BigDecimal.TEN);
		Transaction transaction2 = new Transaction();
		transaction2.setName("Two Individual");
		transaction2.setTotalAmount(BigDecimal.TEN);
		Transaction transaction3 = new Transaction();
		transaction3.setName("Three Individual");
		transaction3.setTotalAmount(BigDecimal.TEN);
		individualTransactions.add(transaction1);
		individualTransactions.add(transaction2);
		individualTransactions.add(transaction3);
	}

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
		final ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_screen_slide_page, container, false);

		// First screen is for Individual Summary screen.
		if (0 == pageNumber) {
			((TextView) rootView.findViewById(R.id.slideTitleId))
					.setText("Individual Summary");

			for (final Transaction transaction : individualTransactions) {
				LinearLayout linearLayout = (LinearLayout) rootView
						.findViewById(R.id.transactionListId);
				TextView textView = new TextView(rootView.getContext());
				textView.setText(transaction.getName()
						+ transaction.getTotalAmount());
				textView.setTextSize(19);
				textView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Toast.makeText(
								rootView.getContext(),
								transaction.getName()
										+ " "
										+ transaction.getTotalAmount()
												.toString(), Toast.LENGTH_LONG)
								.show();
					}
				});
				linearLayout.addView(textView);
			}
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
