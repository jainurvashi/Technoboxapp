package com.example.ishank.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BookList extends Fragment {
     ArrayList<String> cBookList;
    List<String> javaBookList;
    List<String> hmlBookList;
    List<String> cppBookList;
    List<String> csharpBookList;
    List<String> androidBookList;
    String pdfPath = "";
    String filename;
    List<String> path;
    List<String> header;


    ProgressDialog progressDialog;
    final   String BASEADDRESS="http://uryietcom.000webhostapp.com/WebServices/data/";
     HashMap<String,List<String>> bookChild;
    String ServerResponse=null;
     public BookList() {
        // Required empty public constructor
    }


    ExpandableListView expandableListView;
    Context contxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_book_list, container, false);


        expandableListView=(ExpandableListView)v.findViewById(R.id.expand);
        contxt=getActivity();

        HashMap<String,String> pathMap=new HashMap<>();


      header= new ArrayList<>();

        header.add("c");
        header.add("java");
        header.add("C++");
         header.add("CSharp");
        header.add("Html");
        header.add("Android");

       bookChild = new HashMap<>();
       cBookList= new ArrayList<>();
        cppBookList= new ArrayList<>();
        hmlBookList= new ArrayList<>();
        javaBookList= new ArrayList<>();
       androidBookList= new ArrayList<>();
        csharpBookList= new ArrayList<>();
        path= new ArrayList<>();
        bookChild.put(header.get(0),cBookList);
        bookChild.put(header.get(1),javaBookList);
        bookChild.put(header.get(2),cppBookList);
        bookChild.put(header.get(3),csharpBookList);
        bookChild.put(header.get(4),hmlBookList);
        bookChild.put(header.get(5),androidBookList);

    new Asynch().execute();



        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                filename=bookChild.get(header.get(groupPosition)).get(childPosition);
Log.d("aa",filename);

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Fetch Data");
                    FetchDataFromServer();


                return false;
            }
        });



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity(),
                        header.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });



        // Inflate the layout for this fragment
        return v;
    }
    class Asynch extends AsyncTask<Void, Void,String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/WebServices/ctutorial.php");



            try
            {
                HttpResponse httpResponse=httpClient.execute(httpPost);
                ServerResponse=readResponse(httpResponse);
            }
            catch (Exception e)
            {
                ServerResponse=e.toString();
            }
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
                     String  name = jsonObject.getString("name");
                    if(name.contains("cbook")) {
                        cBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }
                    else if(name.contains("cppbook")){
                        cppBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }
                    else if(name.contains("htmlbook")){
                        hmlBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }

                    else if(name.contains("csharpbook")){
                        csharpBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }
                    else if(name.contains("androidbook")){
                        androidBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }
                    else if(name.contains("javabook")){
                        javaBookList.add(jsonObject.getString("name"));
                        path.add(jsonObject.getString("path"));
                    }
                }
                // Log.d(myapp",carrayList.toString());
            }catch (Exception e){
                ServerResponse = e.toString();
            }
            bookChild.put(header.get(0),cBookList);


            expandableListView.setAdapter(new CustomExapandAdaptor(contxt,header,bookChild));



                          /*  Fragment fragment = null;
                            fragment = new pdfwebview();
                            Bundle args = new Bundle();
                            args.putString("path", carrayList.get(position).toString());
                            fragment.setArguments(args);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.contentFrame, fragment);
                            ft.commit();
                        }
                    });*/
                }






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
    public void FetchDataFromServer() {
        File file = new File( Constants.MAIN_DIERECTORY_PATH+ "/Books"+ filename+".pdf" );
        if (file.exists()) {
            Fragment fragment = null;
            fragment = new ReadPdf();
            Bundle args = new Bundle();
            args.putString("path", filename);
            args.putString("pdfpath",Constants.MAIN_DIERECTORY_PATH+ "/Books");
            fragment.setArguments(args);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFrame, fragment);
            ft.commit();
        }
        else {
            new DownloadFileFromURL().execute("http://uryietcom.000webhostapp.com/WebServices/data/" + filename + ".pdf");
        }

    }



    public void createPdfDirectory() {



        File folder = new File(Constants.MAIN_DIERECTORY_PATH+ "/Books");
      //  File folder = new File(Technobox + "/QuestionPaper");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();

            //  Log.d("MyApp", "Question directory Created" + folder.getPath());
        }
        if (success) {

            pdfPath= folder.getPath();


            //    Log.d("MyApp", "directory Exist");
        } else {
            // Do something else on failure
        }
    }







    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog.show();
            createPdfDirectory();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            //    Log.d("MyApp", "in do");
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(pdfPath+"/"+filename+".pdf");


                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.d("MyApp: ", e.toString());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */


        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {

            progressDialog.cancel();
            Fragment fragment = null;
            fragment = new ReadPdf();
            Bundle args = new Bundle();
            args.putString("path", filename);
            args.putString("pdfpath",pdfPath);
            fragment.setArguments(args);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFrame, fragment);
            ft.commit();
            // dismiss the dialog after the file was downloaded


        }

    }
}
