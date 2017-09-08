package com.example.ishank.quiz;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoCalling extends Fragment {

    ListView listView;
    String ServerResponse=null;
    String name;
    String id;
    ArrayList<String> arrayListvideoid;
    ArrayList<String> arrayListvideoname;
    ArrayList<String> imageArray;
    public VideoCalling() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_calling, container, false);

          listView = (ListView) view.findViewById(R.id.video);
        arrayListvideoid = new ArrayList();
        arrayListvideoname = new ArrayList();
        imageArray= new ArrayList<>();
        new Asynch1().execute();

        return view;
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);


            }
            return_text=sb.toString();
        } catch (Exception e)
        {

        }
        return return_text;


    }
    class Asynch1 extends AsyncTask<Void, Void,String> {
        HttpPost httpPost;
        HttpClient httpClient;
        @Override
        protected String doInBackground(Void... params) {
            if(getArguments().getString("name").equals("C Video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/view.php");
          imageArray.add("c");
            }
            else if(getArguments().getString("name").equals("C++ Video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/viewcpp.php");
                imageArray.add("cpp");
            }
            else if(getArguments().getString("name").equals("java video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/viewjava.php");
                imageArray.add("java");
            }
            else if(getArguments().getString("name").equals("Android Video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/viewandroid.php");
                imageArray.add("android");
            }
            else if(getArguments().getString("name").equals("C# Video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/viewcsharp.php");
                imageArray.add("csharp");
            }
            else if(getArguments().getString("name").equals("Html Video"))
            {

                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/viewhtml.php");
                imageArray.add("html");
            }
            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                ServerResponse = readResponse(httpResponse);
            } catch (Exception e) {
                ServerResponse = e.toString();
            }
Log.d("myapp",ServerResponse);

            return ServerResponse;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject1 = new JSONObject(s);
                JSONArray jsonArray = jsonObject1.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString("videoname");
                    id = jsonObject.getString("videoid");
                    arrayListvideoname.add(name);
                    arrayListvideoid.add(id);

                }
        }catch (Exception e){
                ServerResponse = e.toString();
        }
            listView.setAdapter(new videoAdaptr(getActivity(),arrayListvideoname,arrayListvideoid, imageArray));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(),youtube.class);
                    intent.putExtra("videoid",arrayListvideoid.get(position).toString());
                    Log.d("my",arrayListvideoid.get(position).toString());
                    startActivity(intent);
                }
            });
        }

    }


}
