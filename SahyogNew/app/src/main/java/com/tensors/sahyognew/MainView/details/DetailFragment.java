package com.tensors.sahyognew.MainView.details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tensors.sahyognew.MainView.MainActivity;
import com.tensors.sahyognew.R;
import com.tensors.sahyognew.Student;

import java.util.List;

public class DetailFragment extends Fragment {

    private DetailsViewModel detailsViewModel;
    Button refresh,add;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        detailsViewModel =
                ViewModelProviders.of(this).get(DetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        final TextView textView = root.findViewById(R.id.text_details);
        final LinearLayout studentlist = root.findViewById(R.id.vert_linear);
        detailsViewModel.getstud().observe(getViewLifecycleOwner(), new Observer<List<Student>>() {
            @Override
            public void onChanged(final List<Student> students) {
                studentlist.removeAllViews();
                for(int i=0;i<students.size();i++){
                    LinearLayout linearLayout = new LinearLayout(getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText studName = new EditText(getContext());
                    final EditText studRoll = new EditText(getContext());
                    studName.setText(students.get(i).getName());
                    studRoll.setText(students.get(i).getRollno());
                    Button  studEdit = new Button(getContext());
                    studEdit.setText("Update");
                    Button  studDelete = new Button(getContext());
                    studDelete.setText("Delete");
                    final int finalI = i;
                    studEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Thread t = new Thread() {

                                public void run() {
                                    students.get(finalI).setName(studName.getText().toString());
                                    students.get(finalI).setRollno(studRoll.getText().toString());
                                    MainActivity.studentDatabase.studentDao().updateStudent(students.get(finalI));
                                }};
                            t.start();
                        }
                    });
                    studDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Thread t = new Thread() {

                                public void run() {
                                    MainActivity.studentDatabase.studentDao().deleteStudent(students.get(finalI));
                                }};
                            t.start();
                        }
                    });

                    linearLayout.addView(studName);
                    linearLayout.addView(studRoll);
                    linearLayout.addView(studEdit);
                    linearLayout.addView(studDelete);
                    studentlist.addView(linearLayout);
                }
            }
        });

        refresh = root.findViewById(R.id.button2);
        add = root.findViewById(R.id.button);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread() {

                    public void run() {

                        MainActivity.studentDatabase.studentDao().deleteAllStud();
                    }};
                t.start();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View deleteDialogView = getLayoutInflater().inflate(R.layout.add_student, null);
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getContext());
                deleteDialog.setView(deleteDialogView);
                final EditText studname = deleteDialogView.findViewById(R.id.editTextStudentName);
                final EditText studroll = deleteDialogView.findViewById(R.id.editTextStudentRollNo);
                deleteDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(studname.getText().toString()!="" && studroll.getText().toString()!=""){
                            final Student student = new Student(studroll.getText().toString(),studname.getText().toString());
                            Thread t = new Thread() {

                                public void run() {

                                    MainActivity.studentDatabase.studentDao().addStudent(student);
                                }};
                            t.start();
                            Toast.makeText(getContext(),"Student successfully added to local database",Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }else{
                            Toast.makeText(getContext(),"Enter valid Details",Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.cancel();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog deleteDialogb = deleteDialog.create();
                deleteDialogb.show();
            }
        });


        detailsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}