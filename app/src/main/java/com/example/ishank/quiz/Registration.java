package com.example.ishank.quiz;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Registration extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    ImageView mimageView;
    ImageButton mime;
    EditText name;
    EditText username;
    EditText email;
    EditText password;
    EditText confirm;
    Button reg;
    TextView log;
    Context ctx1;
    String pname;
    String pusername;
    String pemail;
    String baseimage;
    String ppassword;
    String pconfirm;
    String id;
    String date;
    Boolean isLogin;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String ServerResponse=null;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
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
        isLogin= share.getBoolean("isLogedin",false);
        ctx1=this;
        if(isLogin){
            Intent intent = new Intent(this, LogIn.class);
            finish();
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mimageView = (ImageView)findViewById(R.id.image_from_camera);
        mime = (ImageButton) findViewById(R.id.imageicon);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
        reg = (Button)findViewById(R.id.registration);
        log= (TextView)findViewById(R.id.login);
        mime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog= new Dialog(ctx1);
                dialog.setContentView(R.layout.picimage);

                dialog.setTitle("Pick Image");
                dialog.getWindow().setLayout(650, 350);
                Button button = (Button) dialog.findViewById(R.id.camera);
                Button button1 = (Button) dialog.findViewById(R.id.galary);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                    }
                });
                dialog.show();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    id=telephonyManager.getDeviceId();
                    pname=name.getText().toString();
                    pusername = username.getText().toString();
                    pemail= email.getText().toString();
                    ppassword= password.getText().toString();
                    pconfirm=confirm.getText().toString();
                    date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                if(pname.equals("")||pusername.equals("")||pemail.equals("")||ppassword.equals("")||pconfirm.equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);
                    alertDialogBuilder.setMessage("plz Fill All Data...");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    if(ppassword.equals(pconfirm)) {

                        new Asynch().execute();
                        editor=share.edit();
                        editor.putBoolean("isLogedin", true);
                        editor.commit();
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);
                        alertDialogBuilder.setMessage("Password should be same...");
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }

            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getApplicationContext(),LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }
    class Asynch extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost("http://uryietcom.000webhostapp.com/WebServices/register.php");
            ArrayList<NameValuePair> parameterList=new ArrayList<NameValuePair>();
            parameterList.add(new BasicNameValuePair("name",pname));
            parameterList.add(new BasicNameValuePair("username",pusername));
            parameterList.add(new BasicNameValuePair("email",pemail));
            parameterList.add(new BasicNameValuePair("password",ppassword));
            parameterList.add(new BasicNameValuePair("imei",id));
            parameterList.add(new BasicNameValuePair("registrationDate",date));
            parameterList.add(new BasicNameValuePair("media",baseimage));
            parameterList.add(new BasicNameValuePair("medianame",pname+".png"));

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
            Log.d("myapp",ServerResponse);




            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

             if(ServerResponse.trim().equals("1")){



                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);
                alertDialogBuilder.setMessage("You already have an account");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            mimageView.setImageBitmap(mphoto);
            baseimage = encodeImage(mphoto);
            Log.d("myapp", baseimage);
            //new LogIn.Asynchchange().execute();
        }

            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                mimageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                baseimage = encodeImage(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        }


    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
