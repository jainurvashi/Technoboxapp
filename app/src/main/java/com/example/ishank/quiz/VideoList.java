package com.example.ishank.quiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoList extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> imageArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_video_list, container, false);
        listView=(ListView)view.findViewById(R.id.videolist);
        imageArray=new ArrayList<String>();
        arrayList=new ArrayList<String>();
        arrayList.add("C Video");
        arrayList.add("java video");
        arrayList.add("C++ Video");
        arrayList.add("Android Video");
        arrayList.add("Html Video");
        arrayList.add("C# Video");
        imageArray.add("vtut");


        listView.setAdapter(new VideoAdapter(getActivity(),arrayList,imageArray));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = null;
                    fragment = new VideoCalling();
                    Bundle args = new Bundle();
                    args.putString("name", listView.getAdapter().getItem(position).toString());
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
            }
        });

        return view;
    }

}
