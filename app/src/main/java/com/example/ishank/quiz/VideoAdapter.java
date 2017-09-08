package com.example.ishank.quiz;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ISHANK on 7/8/2017.
 */

public class VideoAdapter extends BaseAdapter{
    List<String> item;
    Context context;
    List<String> image;
    private static LayoutInflater inflater = null;
    public VideoAdapter(Context context, List<String> item, List<String>image) {
// TODO Auto-generated constructor stub
        this.context = context;
        this.item = item;
        this.image=image;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return  item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.child_video_list, null);

        TextView distance = (TextView) rowView.findViewById(R.id.videotext);

        distance.setText(item.get(position));
        String uri = "@drawable/"+image.get(0);
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.videoimage);
        // imageview= (ImageView)findViewById(R.id.imageview);
        Drawable res = context.getResources().getDrawable(imageResource);
        imageView.setImageDrawable(res);
        return rowView;
    }
}
