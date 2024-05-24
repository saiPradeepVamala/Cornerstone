package com.cornerstonehospice.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cornerstonehospice.R;
import com.cornerstonehospice.android.api.results.Criteria;

import java.util.List;
import java.util.Objects;

/**
 * Created by ramesh on 30/06/15.
 */
public class CriteriaListAdapter extends ArrayAdapter<Criteria> {

    LayoutInflater inflater;
    private List<Criteria> mListItems;

    public static class ViewHolder {
        private TextView mCriteriaTitleTV;
    }



    public CriteriaListAdapter(Context context) {
        super(context, R.layout.criteria_list_item);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.criteria_list_item, parent, false);
            holder.mCriteriaTitleTV = (TextView) convertView.findViewById(R.id.criteria_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mCriteriaTitleTV.setText(Objects.requireNonNull(getItem(position)).criterionTitle);
        return convertView;
    }

}
