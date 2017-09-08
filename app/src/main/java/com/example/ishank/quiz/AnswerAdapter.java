package com.example.ishank.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AnswerAdapter  extends BaseAdapter {
    List<String> qns;
    Context context;
    List<String> ans;
    List<String> corans;
     TextView ane;
    TextView qne;
    TextView youans;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_DEFAULT = 2;
    private static  int VIEW_TYPE_COUNT ;
    private static LayoutInflater inflater = null;
    public AnswerAdapter(Context context, List<String> qns, List<String>ans, List<String> corans) {
// TODO Auto-generated constructor stub
        this.context = context;
        this.qns = qns;
        this.ans=ans;
        this.corans=corans;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    @Override
    public int getCount() {
        return qns.size();
    }

    @Override
    public Object getItem(int position) {
        return  qns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        VIEW_TYPE_COUNT=  10;
        // The total number of row types your adapter supports.
        // This should NEVER change at runtime.
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value from zero to (viewTypeCount - 1)
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (position == getCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_DEFAULT;
    }
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=convertView;

    view = inflater.inflate(R.layout.childanswerkey, null);
      ImageView imageView=(ImageView)  view.findViewById(R.id.right);
     qne = (TextView) view.findViewById(R.id.qns);
     ane = (TextView) view.findViewById(R.id.ans);
     youans = (TextView) view.findViewById(R.id.yourans);

    qne.setText( +(position+1) + "  " + qns.get(position));
    // if(ans.get(position)

        ane.setText("  " + ans.get(position));

        youans.setText("  "+corans.get(position));
if(corans.get(position).equals(ans.get(position))){
    imageView.setBackgroundResource(R.drawable.ic_right_24dp);
}
else {
    imageView.setBackgroundResource(R.drawable.ic_wrong_24dp);
}

        return view;
    }

}
