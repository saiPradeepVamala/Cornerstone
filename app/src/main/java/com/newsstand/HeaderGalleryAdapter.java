package com.newsstand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cornerstonehospice.R;

/**
 * @author rameshemandi
 */
@SuppressLint("NewApi")
public class HeaderGalleryAdapter extends BaseAdapter {

    private Context mContext;
    private int mHighlightedItem;
    /**
     *
     */
    public HeaderGalleryAdapter(Context context) {
        mContext = context;
        mHighlightedItem = 0;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return new Object();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.headered_viewpager_header_gallery_view_item, null);
        ImageView navigationIcon = ((ImageView) convertView.findViewById(R.id.img_view));
        int iconDrawable;
        switch (position) {
			case 0:
                iconDrawable = position == mHighlightedItem ? R.drawable.ic_s_contact : R.drawable.ic_n_contact;
                navigationIcon.setImageResource(iconDrawable);
                break;
			case 1:
                iconDrawable = position == mHighlightedItem ? R.drawable.ic_s_refer : R.drawable.ic_n_refer;
                navigationIcon.setImageResource(iconDrawable);
				break;

			case 2:
                iconDrawable = position == mHighlightedItem ? R.drawable.ic_s_bmi : R.drawable.ic_n_bmi ;
                navigationIcon.setImageResource(iconDrawable);
				break;

            case 3:
                iconDrawable = position == mHighlightedItem ? R.drawable.ic_s_meld : R.drawable.ic_n_meld ;
                navigationIcon.setImageResource(iconDrawable);
                break;

			case 4:
                iconDrawable = position == mHighlightedItem ? R.drawable.ic_s_criteria : R.drawable.ic_n_criteria;
                navigationIcon.setImageResource(iconDrawable);
				break;
		}
        return convertView;
    }

    public void setHighlightedItem(int position) {
        mHighlightedItem = position;
        notifyDataSetChanged();
    }

}
