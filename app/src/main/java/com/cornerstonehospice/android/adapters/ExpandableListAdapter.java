package com.cornerstonehospice.android.adapters;

import android.content.Context;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cornerstonehospice.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ramesh on 04/08/15.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expansion_list_child_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.criterion);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expansion_list_group_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.criteria_title);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.arrow_img);
        lblListHeader.setText(headerTitle);
        if (!isExpanded) {
            Matrix matrix = new Matrix();
            imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
            matrix.postRotate((float) 0f, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
            imageView.setImageMatrix(matrix);
        } else {
            Matrix matrix = new Matrix();
            imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
            matrix.postRotate((float) 90f, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
            imageView.setImageMatrix(matrix);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
