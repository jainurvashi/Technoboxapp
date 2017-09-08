package com.example.ishank.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LogIn extends AppCompatActivity {
    EditText username;
    EditText password;
    String lusername;
    String lpassword;
    Button  lognin;
    Boolean islogin;
    Context ctx;
    Context ctx1;
    EditText email;
    EditText oldpass;
    EditText newpass;
    EditText usernameid;
    String emailid;
    String usernamechange;
    String oldpasswordchange;
    String newpasswordchange;
    TextView forget;
    TextView changepass;
    String ServerResponse=null;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm box");
        alertDialogBuilder.setMessage("Are you sure You wanted to exit");
        alertDialogBuilder.setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();

                    }
                });

        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "exit", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        share =getSharedPreferences("LogIn",MODE_PRIVATE);
        islogin= share.getBoolean("isLogedIn",false);

        ctx=this;
        ctx1=this;
        if(islogin){
            Intent intent = new Intent(this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
         forget=(TextView)findViewById(R.id.forgetpassword);
        username=(EditText)findViewById(R.id.loginusername);
        password=(EditText)findViewById(R.id.loginpassword);
        changepass=(TextView)findViewById(R.id.changepassword);
       lognin=(Button)findViewById(R.id.sign);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Authenticating...");
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(ctx);
                dialog.setContentView(R.layout.activity_forget_dialog);
                dialog.setTitle("Title");
                dialog.getWindow().setLayout(650, 450);
                email= (EditText) dialog.findViewById(R.id.emailid);
                Button button = (Button) dialog.findViewById(R.id.ok);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emailid= email.getText().toString();

                        new Asynch1().execute();
                    }
                });


                dialog.show();
              /*  ForgetDialog cdd=new ForgetDialog(LogIn.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();*/
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog= new Dialog(ctx1);
                dialog.setContentView(R.layout.change_password);
              ;
                dialog.setTitle("Title");
                dialog.getWindow().setLayout(650, 800);
                oldpass=(EditText)dialog.findViewById(R.id.oldpassword);
                newpass= (EditText)dialog.findViewById(R.id.newpassword);
                usernameid=(EditText) dialog.findViewById(R.id.usernameid);
                Button button = (Button) dialog.findViewById(R.id.submit);
                     button.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                              usernamechange= usernameid.getText().toString();
                              oldpasswordchange= oldpass.getText().toString();
                              newpasswordchange= newpass.getText().toString();
                             new  Asynchchange().execute();
                         }
                     });

                dialog.show();
            }
        });
        lognin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lusername=username.getText().toString();
                lpassword=password.getText().toString();

               new Asynch().execute();
            }
        });
    }

    class Asynch extends AsyncTask<Void, Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/WebServices/login.php");
            ArrayList<NameValuePair> parameterList=new ArrayList<NameValuePair>();
            parameterList.add(new BasicNameValuePair("username",lusername));
            parameterList.add(new BasicNameValuePair("password",lpassword));

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
            if(ServerResponse.equals("1")){
                editor=share.edit();
                editor.putString("user",lusername);
                editor.putString("password",lpassword);
                editor.putBoolean("isLogedIn", true);
                editor.commit();


                Intent intent = new Intent(getApplicationContext(),Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                startActivity(intent);

            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LogIn.this);
                alertDialogBuilder.setMessage("you have not authority....");
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
    class Asynch1 extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/MailSender/mail.php");
            ArrayList<NameValuePair> parameterList=new ArrayList<NameValuePair>();

            parameterList.add(new BasicNameValuePair("email",emailid));


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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(ServerResponse.contains("Technobox_Sucess")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LogIn.this);
                alertDialogBuilder.setMessage("Password Successfully send to your email");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LogIn.this);
                 alertDialogBuilder.setMessage("You dont have any account");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
    class Asynchchange extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/WebServices/changepassword.php");
            ArrayList<NameValuePair> parameterList=new ArrayList<NameValuePair>();

            parameterList.add(new BasicNameValuePair("username",usernamechange));
            parameterList.add(new BasicNameValuePair("oldpassword",oldpasswordchange));
            parameterList.add(new BasicNameValuePair("newpassword",newpasswordchange));

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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(ServerResponse.equals("1")){  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LogIn.this);
                alertDialogBuilder.setMessage("Passworde chang Successfully");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LogIn.this);
                alertDialogBuilder.setMessage("Youu id or password is not match");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

}
