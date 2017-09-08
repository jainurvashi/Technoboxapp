package com.example.ishank.quiz;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Answer extends Fragment {
    ArrayList arrayList=new ArrayList();
     ArrayList answerlist=new ArrayList();
int sheet;
    ArrayList coranswerlist=new ArrayList();
    ListView answer1;
    Button again;
    public Answer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_answer, container, false);
        sheet = getArguments().getInt("sheet");
        coranswerlist = getArguments().getStringArrayList("correct");
        again=(Button) view.findViewById(R.id.again);
        answer1=(ListView) view.findViewById(R.id.ans);
        readExcelFile(getActivity(), Constants.MAIN_DIERECTORY_PATH + "/QuestionPaper" + "/PaperSet1.xls");
        again.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        fragment = new quiztype();

        if (fragment != null) {

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentFrame, fragment);
            ft.commit();
        }
    }
});
        return view;
    }
    private void readExcelFile(Context context, String filename) {


        Log.d("MyApp","readExcel");
        arrayList = new ArrayList();
        answerlist = new ArrayList();
        try {
            File file = new File(filename);
            // Creating Input StreaFile file =
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(sheet);
            Log.d("MyApp",mySheet.getSheetName());
            /** We now need something to iterate through the cells.**/
            Iterator<Row> rowIter = mySheet.rowIterator();
                       int k=0;
            Log.d("myapp","v"+coranswerlist);
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();

                int count = 1;
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (count == 1) {
                        arrayList.add(myCell.toString());
                    }

                    if (count == 6) {

                            answerlist.add(myCell.toString());


                    }
                    count++;
                    //   Log.d("MyApp", "Cell Value: " + myCell.toString());
                    // Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                }
                k=k++;
            }
Log.d("myapp",arrayList.toString());
            Log.d("myapp",answerlist.toString());
        } catch (Exception e) {

            Log.d("MyApp", e.toString());

        }
Log.d("myapp",arrayList.toString());
         answer1.setAdapter(new AnswerAdapter(getActivity(),arrayList,answerlist,coranswerlist));

        // Log.d("MyApp", "Size" + arrayList.size());

        return;
    }

}
