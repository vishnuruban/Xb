package in.net.maitri.xb.reports;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.net.maitri.xb.R;
import in.net.maitri.xb.db.ReportData;
import in.net.maitri.xb.util.CheckDeviceType;

class TotalSalesAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<ReportData> _listDataHeader;
    private HashMap<String, List<ReportData>> _listDataChild;

    TotalSalesAdapter(Context context, List<ReportData> listDataHeader,
                          HashMap<String, List<ReportData>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(String.valueOf(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_child, parent, false);
        LinearLayout linear1 = (LinearLayout) convertView.findViewById(R.id.linear2);
        Resources r = _context.getResources();
        float px;
        if (new CheckDeviceType(_context).isTablet()) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, r.getDisplayMetrics());
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, r.getDisplayMetrics());
        }
        for (int i = 0; i < _listDataChild.size(); i++) {
            if (groupPosition == i) {
                List<ReportData> list = _listDataChild.get(String.valueOf(i));
                for (int j = 0; j < list.size(); j++) {
                    if (childPosition == j) {
                        ReportData reportData = list.get(j);
                        TextView textView = new TextView(_context);

                        String text = reportData.getrDescription();
                        Log.d("ITMDes", text);
                        textView.setText(text);
                        textView.setGravity(Gravity.CENTER);
                        textView.setPadding(64, 0, 4, 0);
                        textView.setWidth((int) px);
                        textView.setTextColor(ContextCompat.getColor(_context, R.color.colorBlack));
                        linear1.addView(textView);

                        textView = new TextView(_context);
                        textView.setText(reportData.getrMrp());
                        textView.setGravity(Gravity.END);
                        textView.setPadding(4, 0, 4, 0);
                        textView.setWidth((int) px);
                        textView.setTextColor(ContextCompat.getColor(_context, R.color.colorBlack));
                        linear1.addView(textView);

                        textView = new TextView(_context);
                        textView.setText(reportData.getrQty());
                        textView.setGravity(Gravity.END);
                        textView.setPadding(4, 0, 4, 0);
                        textView.setWidth((int) px);
                        textView.setTextColor(ContextCompat.getColor(_context, R.color.colorBlack));
                        linear1.addView(textView);
                    }
                }
            }
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(String.valueOf(groupPosition)).size();
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
        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_group, parent, false);
        LinearLayout linear1 = (LinearLayout) convertView.findViewById(R.id.linear1);
        Resources r = _context.getResources();
        float px;
        if (new CheckDeviceType(_context).isTablet()) {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 175, r.getDisplayMetrics());
        } else {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, r.getDisplayMetrics());
        }
        for (int j = 0; j < _listDataHeader.size(); j++) {
            if (groupPosition == j) {
                ReportData reportData = _listDataHeader.get(j);
                TextView textView = new TextView(_context);

                String text = reportData.getrDescription();
                Log.d("CatDes", text);
                textView.setText(text);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(64, 0, 4, 0);
                textView.setWidth((int) px);
                textView.setTextColor(ContextCompat.getColor(_context, R.color.colorWhite));
                linear1.addView(textView);

                textView = new TextView(_context);
                textView.setText(reportData.getrMrp());
                textView.setGravity(Gravity.END);
                textView.setPadding(4, 0, 4, 0);
                textView.setWidth((int) px);
                textView.setTextColor(ContextCompat.getColor(_context, R.color.colorWhite));
                linear1.addView(textView);

                textView = new TextView(_context);
                textView.setText(reportData.getrQty());
                textView.setGravity(Gravity.END);
                textView.setPadding(4, 0, 4, 0);
                textView.setWidth((int) px);
                textView.setTextColor(ContextCompat.getColor(_context, R.color.colorWhite));
                linear1.addView(textView);
            }
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}