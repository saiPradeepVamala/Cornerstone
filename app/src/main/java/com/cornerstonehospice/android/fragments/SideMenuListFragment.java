package com.cornerstonehospice.android.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.activities.MainDashboardActivity;
import com.newsstand.SlidingViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramesh on 05/08/15.
 */
public class SideMenuListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    String[] menutitles;
    TypedArray menuIcons;
    TypedArray menuSelectedIcons;

    SideMenuAdapter adapter;
    private List<RowItem> rowItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.side_menu_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        //initSideMenuAdapter();
        //setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);
        ((MainDashboardActivity)getActivity()).navigateToFragment(AboutUsFragment.newInstance()); // TODO: need to move the constant
     //   ((MainDashboardActivity)getActivity()).navigateToFragment(ReferFragment.newInstance(0));
    }

    private void initSideMenuAdapter() {
        menutitles = getResources().getStringArray(R.array.titles);
        menuIcons = getResources().obtainTypedArray(R.array.icons);
        menuSelectedIcons = getResources().obtainTypedArray(R.array.selected_icons);
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < menutitles.length; i++) {
            RowItem item = new RowItem(menutitles[i], menuIcons.getResourceId(i, -1), menuSelectedIcons.getResourceId(i, -1));
            rowItems.add(item);
        }
        adapter = new SideMenuAdapter(getActivity(), rowItems);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Fragment frag = new ReferFragment();
        switch (position) {
            case 0: frag = ReferFragment.newInstance(0);
                break;
            case 1: frag = CriteriaListFragment.newInstance(3);
                break;
            case 2: frag = SlidingViewPagerFragment.newInstance(1);
                break;
            case 3: frag = AboutUsFragment.newInstance();
                break;
            case 4: frag = ContactUsFragment.newInstance(4);
                break;
        }
        ((MainDashboardActivity)getActivity()).dismissKeyboard();
        adapter.setSelectedItem(position);
        ((MainDashboardActivity)getActivity()).navigateToFragment(frag);
    }

    public class SideMenuAdapter extends BaseAdapter {

        Context context;
        List<RowItem> listItems;
        int mSelectedItem = 0;

        SideMenuAdapter(Context context, List<RowItem> rowItem) {
            this.context = context;
            this.listItems = rowItem;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listItems.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.side_menu_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

            RowItem row_pos = listItems.get(position);
            // setting the image resource and title
            imgIcon.setImageResource(mSelectedItem == position ? row_pos.getSelected_icon() : row_pos.getIcon());
//            convertView.setBackgroundColor(mSelectedItem == position ? context.getResources().getColor(R.color.cornerstone_blue2_selected) : context.getResources().getColor(R.color.cornerstone_blue4));
            txtTitle.setText(row_pos.getTitle());
            switch (position) {
                case 0:
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.cornerstone_refer_yellow));
                    break;
                case 1:
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.cornerstone_criteria_pink));
                    break;
                case 2:
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.cornerstone_tools_green));
                    break;
                case 3:
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.cornerstone_about_blue));
                    break;
                case 4:
                    convertView.setBackgroundColor(context.getResources().getColor(R.color.cornerstone_contact_blue));
                    break;
                default:
//                view.setBackgroundColor(mSelectedItem == position ? mContext.getResources().getColor(R.color.cornerstone_blue2_selected) : mContext.getResources().getColor(R.color.cornerstone_blue4));
            }
            return convertView;
        }

        public void setSelectedItem(int position) {
            mSelectedItem = position;
            notifyDataSetChanged();
        }

    }

    public class RowItem {

        private String title;
        private int icon;
        private int selected_icon;


        public RowItem(String title, int icon, int selected_icon) {
            this.title = title;
            this.icon = icon;
            this.selected_icon = selected_icon;

        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public int getSelected_icon() { return selected_icon; }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

}