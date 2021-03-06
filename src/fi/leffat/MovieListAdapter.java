package fi.leffat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MovieListAdapter extends BaseExpandableListAdapter {
	 
	private ArrayList<String> groups;

    private ArrayList<ArrayList<ArrayList<String>>> children;

	private Context context;

	public MovieListAdapter(Context context, ArrayList<String> groups, ArrayList<ArrayList<ArrayList<String>>> children)
	{
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

	@Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }


    public ArrayList<String> getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View convertView, ViewGroup parent) {

    	String child = (String) ((ArrayList<String>)getChild(groupPosition, childPosition)).get(0);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.movielist_child, null);
        }

        TextView childtxt = (TextView) convertView.findViewById(R.id.TextViewChild01);

        childtxt.setText(child);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    public String getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

    	String group = (String) getGroup(groupPosition);

    	if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.movielist_group, null);
        }

        TextView grouptxt = (TextView) convertView.findViewById(R.id.TextViewGroup);

        grouptxt.setText(group);

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}