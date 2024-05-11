package com.example.mycontacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Contact> {
    private final Context context;
    private final ArrayList<Contact> values;

    public CustomListAdapter(@NonNull Context context, @NonNull ArrayList<Contact> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_contact_item, parent, false);

        TextView tvContactName = rowView.findViewById(R.id.textView2);
        TextView tvContactEmail = rowView.findViewById(R.id.textView3);

        Contact e = values.get(position);
        tvContactName.setText(e.Name);
        tvContactEmail.setText(e.Email);

        return rowView;
    }

}
