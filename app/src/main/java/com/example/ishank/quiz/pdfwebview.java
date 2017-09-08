package com.example.ishank.quiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class pdfwebview extends Fragment {
WebView webView;

    final   String BASEADDRESS="http://uryietcom.000webhostapp.com/WebServices/data/";
    public pdfwebview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_pdfwebview, container, false);
        webView = (WebView)view.findViewById(R.id.webpdf);
        webView.getSettings().setJavaScriptEnabled(true);
       String pdf = getArguments().getString("path");

        String path= BASEADDRESS+pdf+".pdf";

      //  String path="http://uryietcom.000webhostapp.com/WebServices/data/cbook.pdf";
        Log.d("myapp",path);


        webView.loadUrl(path);
        return view;
    }

}
