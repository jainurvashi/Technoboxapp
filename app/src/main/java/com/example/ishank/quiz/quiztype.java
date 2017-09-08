package com.example.ishank.quiz;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class quiztype extends Fragment {
    Button cquiz;
    Button cppquiz;
    Button htmlquiz;
    Button javaquiz;
    Button androidquiz;
    Button csharpquiz;
    public quiztype() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_quiztype, container, false);
        cquiz =(Button)view.findViewById(R.id.CQuiz);
        cppquiz =(Button)view.findViewById(R.id.CppQuiz);
        csharpquiz =(Button)view.findViewById(R.id.CsharpQuiz);
        htmlquiz =(Button)view.findViewById(R.id.htmlQuiz);
        javaquiz =(Button)view.findViewById(R.id.javaQuiz);
        androidquiz =(Button)view.findViewById(R.id.androidQuiz);
        htmlquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet",3 );
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
    csharpquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet",5 );
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
        cppquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet", 4);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
        javaquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet", 2);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
        androidquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet", 0);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
        cquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new QuizHome();

                if (fragment != null) {
                    Bundle args = new Bundle();
                    args.putInt("sheet", 1);
                    fragment.setArguments(args);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.contentFrame, fragment);
                    ft.commit();
                }
            }
        });
        return view;
    }

}
