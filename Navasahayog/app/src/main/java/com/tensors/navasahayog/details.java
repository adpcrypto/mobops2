package com.tensors.navasahayog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class details extends Fragment {


    public details() {

    }


    EditText textName,textMail,textVillage;
    Button update,addb;
    TableLayout tableLayout;
    FirebaseFirestore db;
    FirebaseUser user;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_details, container, false);
        textName = view.findViewById(R.id.textViewName);
        textMail = view.findViewById(R.id.textViewMail);
        textVillage = view.findViewById(R.id.textViewVillage);
        update = view.findViewById(R.id.button);
        tableLayout = view.findViewById(R.id.tableLayout);
        addb = view.findViewById(R.id.button2);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            db.collection(user.getEmail()).document("Details").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    textName.setText(documentSnapshot.get("Name").toString());
                    textMail.setText(documentSnapshot.get("Email").toString());
                    textVillage.setText(documentSnapshot.get("Village").toString());
                }
            });
        }
        getStudents();

        addb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getView().getContext(),addOrUpdate.class);
                intent.putExtra("What","ADD");
                intent.putExtra("ID",getAlphaNumericString());
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                user = FirebaseAuth.getInstance().getCurrentUser();
                Map <String,Object> map= new HashMap<>();
                map.put("Name",textName.getText().toString());
                map.put("Email",textMail.getText().toString());
                map.put("Village",textVillage.getText().toString());
                db.collection(user.getEmail()).document("De tails").set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getView().getContext(),"Data Successfully updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }



    private void getStudents() {
        db =FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(user.getEmail()).whereEqualTo("isstudent","true").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                tableLayout.removeAllViews();
                int i=1;
                tableLayout = view.findViewById(R.id.tableLayout);
                TableRow row1= new TableRow(getContext());
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row1.setLayoutParams(lp1);
                TextView nm1 = new TextView(getContext());
                TextView rl1 = new TextView(getContext());
                nm1.setText("Name");
                rl1.setText("Roll no");
                nm1.setPadding(35,35,35,35);
                rl1.setPadding(35,35,35,35);
                nm1.setTextSize(17);
                rl1.setTextSize(17);
                row1.addView(nm1);
                row1.addView(rl1);
                tableLayout.addView(row1,0);

                for(final DocumentSnapshot doc: task.getResult()){
                    TableRow row= new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    Student student1 = doc.toObject(Student.class);

                    TextView nm = new TextView(getContext());
                    TextView  rl = new TextView(getContext());
                    Button button = new Button(getContext());
                    button.setText("Update");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(),addOrUpdate.class);
                            intent.putExtra("What","UPDATE");
                            intent.putExtra("ID",doc.getId());
                            startActivity(intent);
                        }
                    });
                    nm.setText(student1.getName());
                    rl.setText(student1.getRollno());
                    nm.setPadding(35,35,35,35);
                    rl.setPadding(35,35,35,35);
                    nm.setTextSize(17);
                    rl.setTextSize(17);
                    row.addView(nm);
                    row.addView(rl);
                    row.addView(button);
                    tableLayout.addView(row,i);
                    i++;
                }
            }
        });
    }
    String getAlphaNumericString()
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }


}
