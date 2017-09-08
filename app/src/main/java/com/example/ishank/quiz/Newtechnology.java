package com.example.ishank.quiz;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class Newtechnology extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> imageArray;
    ArrayList<String> discriptionlist;
    String ServerResponse = null;
    String filename;
    String pdfPath = "";
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newtechnology, container, false);
        listView = (ListView) view.findViewById(R.id.list);
        imageArray = new ArrayList<String>();
        discriptionlist = new ArrayList<String>();

arrayList= new ArrayList<>();
       /* AssetManager asset = getActivity().getAssets();
        try {
            final String[] arrdata = asset.list("");
            arrayList = new ArrayList<String>();
            int size = arrdata.length;

            for (int i = 0; i < size; i++) {

                if (arrdata[i].endsWith(".pdf")&&arrdata[i].contains("whatsnew")) {
                    Log.d("MyApp", arrdata[i]);
                    arrayList.add(arrdata[i]);
                }

            }
        }catch(Exception e){

        }use to access the pdf from assest folder
*/
        new Asynch().execute();

//        listView.setAdapter(new CustomAdapter(getContext(), arrayList, imageArray, discriptionlist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                filename=listView.getAdapter().getItem(position).toString();
                Log.d("myapp",filename);
                progressDialog = new ProgressDialog(getActivity());
               progressDialog.setTitle("Fetch Data");
               FetchDataFromServer();

           }
       });

        return view;
    }

    class Asynch extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/ctutorial.php");


            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                ServerResponse = readResponse(httpResponse);
            } catch (Exception e) {
                ServerResponse = e.toString();
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
                    String name = jsonObject.getString("name");
                    if (name.contains("technology")) {
                        arrayList.add(jsonObject.getString("name"));
                        discriptionlist.add(jsonObject.getString("discription"));
                        imageArray.add(jsonObject.getString("imgname"));
                    }

                }

                // Log.d(myapp",carrayList.toString());
            } catch (Exception e) {
                ServerResponse = e.toString();

            }
          /*  Log.d("myapp",arrayList.toString());
            discriptionlist.add("The Elixir programming language wraps functional programming with immutable state and an actor-based approach to concurrency in a tidy, modern syntax.");
            discriptionlist.add("Go is a general-purpose language designed with systems programming in mind. It was initially developed at Google in the year 2007 by Robert Griesemer, Rob Pike, and Ken Thompson.");
            discriptionlist.add("Java 8 is the most awaited and is a major feature release of Java programming language.");
            discriptionlist.add("The Julia programming language is a flexible dynamic language, appropriate for scientific and numerical computing, with performance comparable to traditional statically-typed languages.");
            discriptionlist.add("Kotlin is a great fit for developing server-side applications, allowing to write concise and expressive code while maintaining full compatibility with existing Java-based technology stacks and a smooth learning curve:.");
            discriptionlist.add("Rust is a systems programming language focused on three goals: safety, speed, and concurrency");
            discriptionlist.add("Scala is a modern multi-paradigm programming language designed to express common programming patterns in a concise, elegant, and type-safe way. Scala has been created by Martin Odersky and he released the first version in 2003.");
            discriptionlist.add("Swift is a new programming language developed by Apple Inc for iOS and OS X development. Swift adopts the best of C and Objective-C, without the constraints of C compatibility.");
            // Inflate the layout for this fragment
            imageArray.add("elixir");
            imageArray.add("go");
            imageArray.add("java8");
            imageArray.add("julia");
            imageArray.add("kotlin");
            imageArray.add("rust");
            imageArray.add("scala");
            imageArray.add("swift");*/
            listView.setAdapter(new CustomAdapter(getContext(), arrayList, imageArray, discriptionlist));
        }
    }

    public String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);


            }
            return_text = sb.toString();
        } catch (Exception e) {

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