package com.cornerstonehospice.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.utils.NavItem;

import java.util.ArrayList;

/**
 * Created by ramesh on 21/08/15.
 */
public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> mNavItems;
    int mSelectedItem = 0;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_item, null);
        }
        else {
            view = convertView;
        }
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView subtitleView = (TextView) view.findViewById(R.id.subTitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText( mNavItems.get(position).mTitle );
        subtitleView.setText(mNavItems.get(position).mSubtitle);
        iconView.setImageResource(mNavItems.get(position).mIcon);


        switch (position) {
            case 0:
                view.setBackgroundColor(mContext.getResources().getColor(R.color.cornerstone_refer_yellow));
                break;
            case 1:
                view.setBackgroundColor(mContext.getResources().getColor(R.color.cornerstone_tools_green));
                break;
            case 2:
                view.setBackgroundColor(mContext.getResources().getColor(R.color.cornerstone_criteria_pink));
                break;
            case 3:
                view.setBackgroundColor(mContext.getResources().getColor(R.color.cornerstone_about_blue));
                break;
            case 4:
                view.setBackgroundColor(mContext.getResources().getColor(R.color.cornerstone_contact_blue));
                break;
            default:
                view.setBackgroundColor(mSelectedItem == position ? mContext.getResources().getColor(R.color.cornerstone_blue2_selected) : mContext.getResources().getColor(R.color.cornerstone_blue4));
        }

        return view;
    }

    public void setSelectedItem(int position) {
        mSelectedItem = position;
        notifyDataSetChanged();
    }

 }
