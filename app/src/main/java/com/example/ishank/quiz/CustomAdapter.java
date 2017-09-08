package com.example.ishank.quiz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Abhishek kumar on 9/28/2016.
 */
class CustomAdapter extends BaseAdapter {


    List<String> item;
    Context context;
    List<String> image;
    List<String> dicsription;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, List<String> item, List<String>image, List<String>dicsription) {
// TODO Auto-generated constructor stub
        this.context = context;
        this.item = item;
        this.image=image;
        this.dicsription=dicsription;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
// TODO Auto-generated method stub
        return item.size();
    }

    @Override
    public Object getItem(int position) {
// TODO Auto-generated method stub
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
// TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.new_child_view, null);

        TextView distance = (TextView) rowView.findViewById(R.id.textview);

        TextView disc = (TextView) rowView.findViewById(R.id.discription);
        disc.setText(dicsription.get(position));
       distance.setText(item.get(position));
       // String uri = "@drawable/"+arr[0];
      //  String uri = "@drawable/"+image.get(position);
      //  int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageview);
       // imageview= (ImageView)findViewById(R.id.imageview);
       // Drawable res = context.getResources().getDrawable(imageResource);
        Picasso.with(context).load("http://uryietcom.000webhostapp.com/WebServices/data/"+image.get(position)+".png").into(imageView);
Log.d("myapp",image.get(position));
        Log.d("myapp",item.get(position));
       // imageView.setImageDrawable(res);
        return rowView;
    }

}


