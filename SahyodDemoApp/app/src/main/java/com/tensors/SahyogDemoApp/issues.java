package com.tensors.SahyogDemoApp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;


public class issues extends Fragment {



    private static final String STUDENT_TEXT = "testattend.STUDENT_TEXT";
    private DatePickerDialog datepicker;
    private EditText datetext;
    private EditText datetext1;
    private EditText bookid;
    private EditText sbookid;
    private EditText rbookid;
    private EditText remarks;
    private TextView avail;
    private TextView bookname;
    private TextView showstudent;
    private String[] StudentList;
    private String[] booklist;
    private Spinner studentspinner;

    private int Bookid;
    private int rBookid;
    private String Bookissuedate;
    private String Bookreturndate;

    private ArrayAdapter studentadapter;
    private String studentsname;
    private String studentname;

    private String Remarks;


    public issues() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issues, container, false);


        studentspinner = view.findViewById(R.id.studentspinner);
        StudentList = new String[]{"Hermine", "Paul", "Gertrude", "Leon","Marcus","Mason","Bruno","Nemanja","David","Luke"};
        studentadapter = new ArrayAdapter<>(container.getContext(), android.R.layout.simple_spinner_item, StudentList);
        studentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentspinner.setAdapter(studentadapter);
        studentspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                studentsname = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //////////////////////////////Start:Date Input Code/////////////////////////////

        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        final int month = cldr.get(Calendar.MONTH);
        final int year = cldr.get(Calendar.YEAR);

        datetext = view.findViewById(R.id.issuedate);
        datetext.setInputType(InputType.TYPE_NULL);
        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // date picker dialog
                datepicker = new DatePickerDialog(container.getContext(), android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  //year,monthOfYear + 1, dayOfMonth are the required date values
                                datetext.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);    //date in dd/mm/yyyy format
                            }
                        }, year, month, day);

                datepicker.show();
            }
        });

        final Calendar cldr1 = Calendar.getInstance();
        final int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
        final int month1 = cldr1.get(Calendar.MONTH);
        final int year1 = cldr1.get(Calendar.YEAR);

        datetext1 = view.findViewById(R.id.returndate);
        datetext1.setInputType(InputType.TYPE_NULL);
        datetext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // date picker dialog
                datepicker = new DatePickerDialog(container.getContext(), android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year1, int monthOfYear, int dayOfMonth) {  //year,monthOfYear + 1, dayOfMonth are the required date values
                                datetext1.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);    //date in dd/mm/yyyy format
                            }
                        }, year1, month1, day1);
                datepicker.show();
            }
        });
        //////////////////////////////Book/asset id assigning Code/////////////////////////////

        booklist=new String[]{"The Grass ","Murder!","Owl Creek","Open Boat","Signalman","Cormack's","tarzen"};
        int i=0;
        int a=0;
        final Book[] s1 = new Book[10];
        for(i=0;i<6;++i){
            s1[i]=new Book();
            s1[i].assignid(i,booklist[i]);
        }


        bookid = view.findViewById(R.id.Bookid);
        bookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookid.setText("");
            }
        });

        //////////////////////////////Book/asset id check availability Code/////////////////////////////

        Button availab = view.findViewById(R.id.bookavail);
        avail = view.findViewById(R.id.status);
        showstudent = view.findViewById(R.id.studentname);
        bookname = view.findViewById(R.id.bookname);
        availab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j = 0;

                for (j = 0;j<6; ++j) {
                    boolean Status;
                    if (s1[j].getBookId() == Integer.parseInt(bookid.getText().toString())) {
                        Status = s1[j].checkbookavailability();

                        if (!Status) {
                            avail.setText("Not available");
                            bookname.setText(s1[j].bookname);
                            showstudent.setText(s1[j].studentname);
                        } else {
                            avail.setText("Available");
                            bookname.setText(s1[j].bookname);
                            showstudent.setText("Not issued");
                        }
                    }
                }
            }
        });
        sbookid = view.findViewById(R.id.sbookid);
        sbookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbookid.setText("");
            }
        });

        //////////////////////////////Book/asset id issue submit Code/////////////////////////////

        Button submit = view.findViewById(R.id.subbutton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentname = studentsname;

                int j = 0;
                for (j = 0;j<6 ; ++j) {
                    boolean Status;
                    if (s1[j].getBookId() == Integer.parseInt( sbookid.getText().toString() )) {
                        Status = s1[j].checkbookavailability();
                        if (!Status) {
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Book/Asset not available", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            Bookid = Integer.parseInt( sbookid.getText().toString());
                            s1[j].bookavail=false;
                            s1[j].studentname=studentsname;
                        }
                    }

                }
                Bookissuedate = datetext.getText().toString();
                Bookreturndate = datetext1.getText().toString();


            }
        });

        rbookid = view.findViewById(R.id.Returnid);
        rbookid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbookid.setText("");
            }
        });

        //////////////////////////////Book/asset id return submit Code/////////////////////////////
        Button rsubmit = view.findViewById(R.id.Return);
        remarks=view.findViewById(R.id.remarks);
        rsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int j = 0;
                for (j = 0;j<6 ; ++j) {
                    boolean Status;
                    if (s1[j].getBookId() == Integer.parseInt( rbookid.getText().toString() )) {
                        Status = s1[j].checkbookavailability();
                        if (!Status) {
                            rBookid = Integer.parseInt( sbookid.getText().toString());
                            s1[j].bookavail=true;
                            s1[j].studentname="none";
                            Remarks=remarks.getText().toString();

                        }
                        else {

                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Book/Asset is already in the library", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                }

            }
        });



        return view;
    }
}

