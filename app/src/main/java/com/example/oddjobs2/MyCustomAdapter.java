package com.example.oddjobs2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyCustomAdapter extends ArrayAdapter<DataModel> implements ListAdapter {
    private ArrayList<DataModel> dataSet;
    Context mContext;
    ListView listView;

    // Declare the items in the listView
    private static class ViewHolder {
        TextView txtName, txtDes, txtPay, txtLoc;
    }

    // Initialize a new Adapter
    public MyCustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.custom_list_view, data);
        this.dataSet = data;
        this.mContext = context;
    }

    public int getCount(){
        return dataSet.size();
    }

    private int lastPosition = -1;

    // Gets the data from the DataModel to display in the custom listView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for the current position
        final DataModel dataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;

        // If the view hasn't been used before then add the data
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list_view, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.list_item_name);
            viewHolder.txtDes = (TextView) convertView.findViewById(R.id.list_item_des);
            viewHolder.txtPay = (TextView) convertView.findViewById(R.id.list_item_pay);
            viewHolder.txtLoc = (TextView) convertView.findViewById(R.id.list_item_loc);

            result=convertView;

            convertView.setTag(viewHolder);
            // Else find a new viewHolder
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        // If the user clicks the delete button,
        // the item is removed from the list and the database
        final int newPosition = position;
        Button deleteBtn = (Button)convertView.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataSet.remove(newPosition);
                notifyDataSetChanged();
                JobListActivity.checkEmpty();
            }
        });

        // Add the ID to the listView holder
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtDes.setText(dataModel.getDes());
        viewHolder.txtPay.setText(dataModel.getPay());
        viewHolder.txtLoc.setText(dataModel.getLocation());
        // Return the completed view to render on screen
        return convertView;
    }
}
