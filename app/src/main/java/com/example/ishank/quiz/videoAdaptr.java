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
 * Created by ISHANK on 7/9/2017.
 */

public class videoAdaptr extends BaseAdapter {
    List<String> videoname;
    Context context;
    List<String> videoid;
    List<String> imagelist;
    private static LayoutInflater inflater = null;

    public videoAdaptr(Context context, List<String> videoname, List<String> videoid,List<String> imagelist) {
// TODO Auto-generated constructor stub
        this.context = context;
        this.videoname = videoname;
        this.videoid = videoid;
        this.imagelist=imagelist;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return videoid.size();

    }

    @Override
    public Object getItem(int position) {
        return videoid.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.childvideocalling, null);
        TextView distance = (TextView) rowView.findViewById(R.id.videoname);

        distance.setText(videoname.get(position));
        String uri = "@drawable/"+imagelist.get(0);  // where myresource (without the extension) is the file

        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imagevideo);
        // imageview= (ImageView)findViewById(R.id.imageview);
        Drawable res = context.getResources().getDrawable(imageResource);
        imageView.setImageDrawable(res);

        return rowView;
    }
}