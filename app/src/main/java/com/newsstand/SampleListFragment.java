package com.newsstand;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.cornerstonehospice.R;

import java.util.ArrayList;

public class SampleListFragment extends ScrollTabHolderFragment implements OnScrollListener {

	private static final String ARG_POSITION = "position";

	private ListView mListView;
	private ArrayList<String> mListItems;

	private int mPosition;

	public static Fragment newInstance(int position) {
		SampleListFragment f = new SampleListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);

		mListItems = new ArrayList<String>();

		for (int i = 1; i <= 100; i++) {
			mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.headered_viewpager_fragment_list, null);

		mListView = (ListView) v.findViewById(R.id.listView);

		View placeHolderView = inflater.inflate(R.layout.headered_viewpager_header_placeholder, mListView, false);
		mListView.addHeaderView(placeHolderView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView.setOnScrollListener(this);
//	 	mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.headered_viewpager_list_item, android.R.id.text1, mListItems));
		mListView.setAdapter(new listAdapter(getActivity()));
	}

	@Override
		public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}

		mListView.setSelectionFromTop(1, scrollHeight);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// nothing
	}

	public class listAdapter extends BaseAdapter{

		LayoutInflater inflater=null;

		public listAdapter(Activity a){
			inflater = ( LayoutInflater )a.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;
//			ViewHolder holder;

			if(convertView==null){

				/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
				vi = inflater.inflate(R.layout.activity_bmi, null);

				/****** View Holder Object to contain tabitem.xml file elements ******/

//				holder = new ViewHolder();
//				holder.text = (TextView) vi.findViewById(R.id.text);
//				holder.text1=(TextView)vi.findViewById(R.id.text1);
//				holder.image=(ImageView)vi.findViewById(R.id.image);

				/************  Set holder with LayoutInflater ************/
//				vi.setTag( holder );
			}
			return vi;
		}

		private class ViewHolder {

		}
	}

}