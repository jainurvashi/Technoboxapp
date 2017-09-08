package com.example.ishank.quiz;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class result extends Fragment {
    TextView corrct;
    int ans;
    int overall;
    TextView answer;
    String email;
    int sheet;
    int progress;
    TextView total;
    SharedPreferences share1;
    String ServerResponse=null;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Context ctx1;
   // private TextView info;
    //private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ShareButton share;
    ShareDialog shareDialog;

    ArrayList<String> answerList;
    TextView prog;
    public result() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx1=getContext();
        // Inflate the layout for this fragment
       FacebookSdk.sdkInitialize(getContext());
       callbackManager = CallbackManager.Factory.create();
        final View view = inflater.inflate(R.layout.fragment_result, container, false);
        //info = (TextView)view.findViewById(R.id.info);
     //   loginButton = (LoginButton)view.findViewById(R.id.login_button);
share=(ShareButton) view.findViewById(R.id.share);
        shareDialog = new ShareDialog(this);
        share1 = this.getActivity().getSharedPreferences("Home", MODE_PRIVATE);
        email = share1.getString("email", "email");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("result is sending to your email...");
        progressDialog.setCancelable(false);

        answerList=new ArrayList<>();
        total=(TextView) view.findViewById(R.id.total);
        corrct = (TextView) view.findViewById(R.id.correct);
        answer = (TextView) view.findViewById(R.id.answer);
        ans = getArguments().getInt("value");
        overall = getArguments().getInt("total");
        prog=(TextView) view.findViewById(R.id.progress);
        progress=(ans*100)/overall;
        prog.setText(String.valueOf(progress)+"%");
        sheet = getArguments().getInt("sheet");
        answerList= getArguments().getStringArrayList("answer");
        progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(progress);
        total.setText("Total Qns:- "+String.valueOf(overall));
        corrct.setText(String.valueOf(ans));

new  Asynch1().execute();
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new Answer();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet", sheet);
                    args.putStringArrayList("correct", answerList);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });

   /*loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        info.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().getUserId()
                                         + "Auth Token: " + loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        info.setText("Login attempt canceled.");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        info.setText("Login attempt failed.");
                    }
          });*/
share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("Technobox")
                            .setContentDescription( "Technobox App")
                            .setContentUrl(Uri.parse("https://drive.google.com/open?id=0B1op9W0ZvVooM2JjUjY0SVlFT0E")) .build();
            shareDialog.show(linkContent);

        }


}
});

        return view;


    }
    ShareLinkContent content = new ShareLinkContent.Builder()
            .setContentUrl(Uri.parse("https://developers.facebook.com"))
            .build();
    class Asynch1 extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/MailSender/result.php");
            ArrayList<NameValuePair> parameterList=new ArrayList<NameValuePair>();

            parameterList.add(new BasicNameValuePair("email",email));
            parameterList.add(new BasicNameValuePair("answer",String.valueOf(ans)));

            try
            {
                httpPost.setEntity(new UrlEncodedFormEntity(parameterList));
                HttpResponse httpResponse=httpClient.execute(httpPost);
                ServerResponse=readResponse(httpResponse);
            }
            catch (Exception e)
            {
                ServerResponse=e.toString();
            }





            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
            if(ServerResponse.contains("Technobox_Sucess")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("your result is sended sucessfully to your emailid");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();





            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("You dont have any account");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
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
}
