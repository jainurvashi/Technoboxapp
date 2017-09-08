package com.example.ishank.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences share;
    SharedPreferences.Editor editor;
    TextView homeusername;
    TextView homeuserid;
    ImageView image;
    String password;
    String username;
    String ServerResponse = null;
    String path;
    String name;
    String email;
    Context ctx1;
    SharedPreferences share1;
    SharedPreferences.Editor editor1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx1=this;
        init();
        setContentView(R.layout.activity_home);
        Fragment fragment = null;
        fragment = new BookList();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, fragment);
        ft.commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        share1 = getSharedPreferences("Home", MODE_PRIVATE);
        share = getSharedPreferences("LogIn", MODE_PRIVATE);

        username = share.getString("user", "user");
        password = share.getString("password", "password");

        Log.d("MyApp", username + password);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header =navigationView.getHeaderView(0);
        homeuserid = (TextView)header.findViewById(R.id.homeuserid);
        homeusername = (TextView)header. findViewById(R.id.homeuser);
        image = (ImageView) header.findViewById(R.id.imageView1);

        new Asynch1().execute();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(ctx1);
                dialog.setContentView(R.layout.imag);
                ;
                dialog.setTitle("Pick Image");
                dialog.getWindow().setLayout(550, 600);
                ImageView button = (ImageView) dialog.findViewById(R.id.im);
                Picasso.with(getApplicationContext()).load("http://uryietcom.000webhostapp.com/WebServices/"+path).into(button);
                dialog.show();
            }


        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      if (id == R.id.logout) {
            Intent intent = new Intent(this, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            editor = share.edit();
            editor.putBoolean("isLogedIn", false);
            editor.commit();
            Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.books) {
            displaySelectedScreen(R.id.books);
        } else if (id == R.id.quiz) {
            displaySelectedScreen(R.id.quiz);
        } else if (id == R.id.videoTutorial) {

            displaySelectedScreen(R.id.videoTutorial);

        } else if (id == R.id.whatsnew) {
            displaySelectedScreen(R.id.whatsnew);
        } else if (id == R.id.about) {
            displaySelectedScreen(R.id.about);
        } else if (id == R.id.nav_send) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "share this");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.books:
                fragment = new BookList();
                break;

            case R.id.quiz:
                fragment = new StartQuiz();
                break;

            case R.id.whatsnew:
                fragment = new Newtechnology();
                break;
            case R.id.videoTutorial:
                fragment = new VideoList();
                break;
            case R.id.about:
                fragment = new about();
        }



        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFrame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    class Asynch1 extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://uryietcom.000webhostapp.com/WebServices/home.php");
            ArrayList<NameValuePair> parameterList = new ArrayList<NameValuePair>();
            Log.d("myapp", "do");
            parameterList.add(new BasicNameValuePair("username", username));
            parameterList.add(new BasicNameValuePair("password", password));
            Log.d("MyApp", username + password);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(parameterList));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                ServerResponse = readResponse(httpResponse);
                Log.d("myapp", ServerResponse);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.d("myapp", ServerResponse);


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
                    name= jsonObject.getString("name");
                    email= jsonObject.getString("email");
                    path = jsonObject.getString("path");

                    Log.d("MyApp", name + email + path);                }
                homeusername.setText(name);
                homeuserid.setText(email);
                 Picasso.with(getApplicationContext()).load("http://uryietcom.000webhostapp.com/WebServices/"+path).into(image);

            } catch (Exception e) {

            }
            editor1=share1.edit();
            editor1.putString("email",email);
            editor1.commit();
        }
     }


    public void init(){

        createDirectory();
    }

    void createDirectory() {


        File folder = new File(Environment.getExternalStorageDirectory() + "/Technobox");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
            //   Log.d("MyApp", "New directory Created");
        }
        if (success) {


        } else {
            // Do something else on failure
        }

    }

}

