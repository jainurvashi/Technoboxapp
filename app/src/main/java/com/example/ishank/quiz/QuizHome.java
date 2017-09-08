package com.example.ishank.quiz;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;


/** Button button;
 Button read;
 String MainDirectoryPath="";
 String QuestionPaperPath="";
 * A simple {@link Fragment} subclass.
 */
public class QuizHome extends Fragment {
    int index = 0;
    ImageButton next;
    ImageButton follow;
TextView timer;
    String MainDirectoryPath = "";
    String QuestionPaperPath = "";
    ProgressDialog progressDialog;
    TextView textView;
    RadioButton option1;
    boolean backvisible=true;
    RadioButton option2;
    RadioButton option3;
    RadioButton option4;
    int sheetno;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
  ArrayList<QA> arrayList;

    ArrayList <String> answerlist;
int correct=0;
    int total=0;
    int counter=60;
    public QuizHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.fragment_quiz_home, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Fatching...");
        progressDialog.setCancelable(false);

        FetchDataFromServer();
        sheetno=getArguments().getInt("sheet");
        textView = (TextView) view.findViewById(R.id.textView2);
        option1 = (RadioButton) view.findViewById(R.id.option1);
        option2 = (RadioButton) view.findViewById(R.id.option2);
        option3 = (RadioButton) view.findViewById(R.id.option3);
        option4 = (RadioButton) view.findViewById(R.id.option4);
        answerlist=new ArrayList<>();
        timer=(TextView) view.findViewById(R.id.time);
        radioGroup=(RadioGroup)view.findViewById(R.id.radiogroup);
        next = (ImageButton) view.findViewById(R.id.next);
        follow = (ImageButton) view.findViewById(R.id.last);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.getCheckedRadioButtonId()==-1) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Plz select answer");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
                else{
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton) view.findViewById(selectedId);
                    Log.d("value", arrayList.get(index).getCorrect());
                    Log.d("value", radioButton.getText().toString());
                     answerlist.add(radioButton.getText().toString());

                    if (radioButton.getText().toString().equals(arrayList.get(index).getCorrect())) {
                        correct = correct + 1;

                        //  Log.d("value",""+correct);
                    }
                    total = total + 1;
                    next();
                }

            }

        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButton.getText().toString().equals(arrayList.get(index).getCorrect())) {
                    correct = correct - 1;

                }
                answerlist.remove(answerlist.size()-1);
                //  Log.d("value",""+correct);

            total=total-1;
    back();
            }
        });

       // radioButton=(RadioButton)view.findViewById(selectedId);
//Log.d("myapp",radioButton.getText().toString());
//        Toast.makeText(getContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();
        return view;

    }


    public void FetchDataFromServer() {


            new DownloadFileFromURL().execute("http://uryietcom.000webhostapp.com/QuestionSet/PaperSet1.xls");
        }



    public void creteQuestionPaperDirectory() {


        File
                folder = new File(Constants.MAIN_DIERECTORY_PATH + "/QuestionPaper");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();

          //  Log.d("MyApp", "Question directory Created" + folder.getPath());
        }
        if (success) {

            QuestionPaperPath
                    = folder.getPath();
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
            creteQuestionPaperDirectory();

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
                OutputStream output = new FileOutputStream(QuestionPaperPath
                        + "/PaperSet1.xls");

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
                Log.e("Error: ", e.getMessage());
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
            // dismiss the dialog after the file was downloaded

            readExcelFile(getActivity(), QuestionPaperPath + "/PaperSet1.xls");
            progressDialog.cancel();
            new CountDownTimer(300000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long mint =(millisUntilFinished / (1000*60));
                    timer.setText("Time remaining: "+mint+":" +counter);
                    counter--;
                    if(counter==0){
                        counter=60;
                    }
                }

                public void onFinish() {

                    timer.setText(" ");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Time is Over");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    Fragment fragment = null;
                    fragment = new result();
                    Bundle args = new Bundle();
                    args.putStringArrayList("answer", answerlist);
                    args.putInt("value", correct);
                    args.putInt("total", total);
                    args.putInt("sheet", sheetno);

                    fragment.setArguments(args);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.contentFrame, fragment);
                        ft.commit();

                }
            }.start();
        }

    }


    private void readExcelFile(Context context, String filename) {


        Log.d("MyApp","readExcel");
        arrayList = new ArrayList<QA>();
        try {
            File file = new File(filename);
            // Creating Input StreaFile file =
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(sheetno);
           Log.d("MyApp",mySheet.getSheetName());
            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                QA qa = new QA();
                int count = 1;
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (count == 1) {
                        qa.setQuestion(myCell.toString());
                    }
                    if (count == 2) {
                        qa.setOption1(myCell.toString());

                    }
                    if (count == 3) {
                        qa.setOption2(myCell.toString());
                    }
                    if (count == 4) {
                        qa.setOption3(myCell.toString());
                    }
                    if (count == 5) {
                        qa.setOption4(myCell.toString());
                    }

                    if (count == 6) {
                        qa.setCorrect(myCell.toString());


                    }

                    count++;


                 //   Log.d("MyApp", "Cell Value: " + myCell.toString());
                   // Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }


                arrayList.add(qa);
            }


        } catch (Exception e) {

            Log.d("MyApp", e.toString());

        }


       // Log.d("MyApp", "Size" + arrayList.size());

        jbInt();
        return;
    }


    void jbInt() {
        if (index == 0) {
            Log.d("aa",arrayList.get(index).getQuestion());
            textView.setText(index+1+arrayList.get(index).getQuestion());
            option1.setText(arrayList.get(index).getOption1());
            option2.setText(arrayList.get(index).getOption2());
            option3.setText(arrayList.get(index).getOption3());
            option4.setText(arrayList.get(index).getOption4());

        }

    }

    void next() {

        radioGroup.clearCheck();

//follow.setVisibility(View.VISIBLE);
        try {
if (backvisible==false) {
    backvisible=true;
    follow.setVisibility(View.VISIBLE);
}
            if (index < arrayList.size()-1 ) {

                 index++;

                textView.setText(index+1+arrayList.get(index).getQuestion());
                option1.setText(arrayList.get(index).getOption1());
                option2.setText(arrayList.get(index).getOption2());
                option3.setText(arrayList.get(index).getOption3());
                option4.setText(arrayList.get(index).getOption4());
            } else if(index==arrayList.size()-1) {
Log.d("value","result"+correct);
                Fragment fragment = null;
                fragment = new result();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putStringArrayList("answer", answerlist);
                    args.putInt("value", correct);
                    args.putInt("total", total);
                    args.putInt("sheet", sheetno);

     fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        } catch (Exception d) {

        }

    }
    void back() {
//next.setVisibility(View.VISIBLE);

        try {

            if (index >0) {

                  index--;

                textView.setText(index+1+arrayList.get(index).getQuestion());
                option1.setText(arrayList.get(index).getOption1());
                option2.setText(arrayList.get(index).getOption2());
                option3.setText(arrayList.get(index).getOption3());
                option4.setText(arrayList.get(index).getOption4());
            }
            else if(index==0){
                backvisible=false;
            follow.setVisibility(View.INVISIBLE);
            }

        } catch (Exception d) {

        }

    }


    }