package com.tensors.navasahayog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class history extends Fragment {



    public history() {
        // Required empty public constructor
    }

    LinearLayout ll;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    SQLiteDatabase mydb;
    Button upload;
    int i;
    int pendingatt,pendingvid;
    ImageView f23f;
    Collection<Map<String,Uri>> photomaps;
    Collection<Map<String,Uri>> videomaps;
    List<String> photolist,videolist;
    int photoCount,videoCount;
    @SuppressLint("Recycle")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        mydb = getContext().openOrCreateDatabase("myDb",MODE_PRIVATE,null);
        ll = view.findViewById(R.id.linearlaaayout);
        upload = view.findViewById(R.id.button8);
        f23f= view.findViewById(R.id.imageView2);
        upload.setEnabled(true);
        photoCount=0;
        videoCount=0;
        pendingatt=0;
        photolist = new ArrayList<>();
        videolist = new ArrayList<>();
        i=0;
        pendingvid=0;
        check();
        checkvid();
        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int j = 0;
                for (j = 0; j < pendingatt; j++) {
                    Map<String, Object> map = new HashMap<>();
                    Cursor result;
                    result = mydb.rawQuery(" SELECT * FROM AttendanceTable ;", null);
                    result.moveToFirst();
                    for (int i = 0; i < j; i++) {
                        result.moveToNext();
                    }
                    map.put("Present", result.getString(1));
                    map.put("Absentees", result.getString(2));


                    final int finalJ = j;
                    db.collection(user.getEmail()).document("daily/" + result.getString(0) + "/Attendance").set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (finalJ >= pendingatt - 1) {
                                Toast.makeText(getView().getContext(), "Attendance upload complete", Toast.LENGTH_SHORT).show();
                                mydb.execSQL("DELETE FROM AttendanceTable;");
                                check();
                                checkvid();
                            }
                        }
                    });
                }
            }
        });





        return  view;
    }

    private void checkvid() {
        try {
            Cursor result;
            result = mydb.rawQuery(" SELECT COUNT(*) FROM PhotoVideo ;", null);
            result.moveToFirst();
            String name3 = result.getString(0);
            pendingvid = Integer.valueOf(name3);
        }catch (Exception e){
            pendingvid=0;
        }
        photomaps= new HashSet<>();
        videomaps= new HashSet<>();
        for(int j=0;j<pendingvid;j++){
            photoCount=0;
            videoCount=0;
            LinearLayout linear = new LinearLayout(getContext());
            linear.setOrientation(LinearLayout.VERTICAL);
            TextView att = new TextView(getContext());
            att.setText("Pending Photo and Video:");
            final Cursor result;
            result = mydb.rawQuery(" SELECT * FROM PhotoVideo ;", null);
            result.moveToFirst();
            for(int i=0;i<j;i++){
                result.moveToNext();
            }
            String[] photouris = convertStringtoArray(result.getString(1));

            photoCount = photouris.length;
            final Uri[] photoooouris = new Uri[photoCount];
            for(int i=0;i<photoCount;i++){
                photoooouris[i] = Uri.parse(photouris[i]);
            }
            String[] videouris = convertStringtoArray(result.getString(2));
            videoCount = videouris.length;
            final Uri[] videoooouris = new Uri[videoCount];
            for(int i=0;i<videoCount;i++){
                videoooouris[i] = Uri.parse(videouris[i]);
            }
            TextView date = new TextView(getContext());
            date.setText("DATE"+result.getString(0));
            TextView photcny = new TextView(getContext());
            photcny.setText("Number of pending photos:"+photoCount);
            TextView video = new TextView(getContext());
            video.setText("Number of pending videos"+videoCount);
            final Button upload2 = new Button(getContext());
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            upload2.setText("Upload");
            upload2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0;i<photoCount;i++){
                        final int finalI =i;
                        final ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        upload2.setEnabled(false);
                        upload2.setText("Uploading.....");
                        StorageReference file = FirebaseStorage.getInstance().getReference().child("daily_images/").child(user.getEmail() + "/").child(result.getString(0)+"/").child(String.valueOf(finalI) + ".jpg");
                        Toast.makeText(getContext(), ""+photoooouris[i].toString(), Toast.LENGTH_SHORT).show();
                        file.putFile(photoooouris[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getView().getContext(), "Upload" + String.valueOf(finalI) + "Completed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (finalI >= photoCount - 1) {
                                    upload2.setEnabled(true);
                                    upload2.setText("UPLOAD IT");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getView().getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (finalI >= photoCount - 1) {
                                    upload2.setEnabled(true);
                                    upload2.setText("UPLOAD IT");
                                }
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

                    }
                    for(int i=0;i<videoCount;i++){
                        final int finalI =i;
                        final ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();
                        upload2.setEnabled(false);
                        upload2.setText("Uploading.....");
                        StorageReference file = FirebaseStorage.getInstance().getReference().child("daily_videos/").child(user.getEmail() + "/").child(result.getString(0)+"/").child(String.valueOf(finalI) + ".mp4");
                        file.putFile(videoooouris[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getView().getContext(), "Upload" + String.valueOf(finalI) + "Completed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (finalI >= videoCount - 1) {
                                    upload2.setEnabled(true);
                                    upload2.setText("UPLOAD IT");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getView().getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                if (finalI >= videoCount - 1) {
                                    upload2.setEnabled(true);
                                    upload2.setText("UPLOAD IT");
                                }
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });

                    }

                }
            });
            linear.addView(date);
            linear.addView(photcny);
            linear.addView(video);
            linear.addView(upload2);
            linear.setBackgroundResource(R.drawable.edittext);
            ll.addView(linear);

        }
    }

    private void check() {
        ll.removeAllViews();
        try {
            Cursor result;
            result = mydb.rawQuery(" SELECT COUNT(*) FROM AttendanceTable ;", null);
            result.moveToFirst();
            String name3 = result.getString(0);
            pendingatt = Integer.valueOf(name3);
        }catch (Exception e){
            pendingatt=0;
        }

        for(int j=0;j<pendingatt;j++){
            LinearLayout linear = new LinearLayout(getContext());
            linear.setOrientation(LinearLayout.VERTICAL);
            TextView att = new TextView(getContext());
            att.setText("Pending attendence:");
            Cursor result;
            result = mydb.rawQuery(" SELECT * FROM AttendanceTable ;", null);
            result.moveToFirst();
            for(int i=0;i<j;i++){
                result.moveToNext();
            }
            linear.addView(att);
            TextView date = new TextView(getContext());
            TextView len = new TextView(getContext());
            TextView abs = new TextView(getContext());
            date.setText("DATE"+result.getString(0));
            len.setText("PRESENT"+result.getString(1));
            abs.setText("ABSENTEES"+result.getString(2));
            linear.addView(date);
            linear.addView(len);
            linear.addView(abs);
            linear.setBackgroundResource(R.drawable.edittext);
            ll.addView(linear);

        }
    }

    public static String strSeparator = ",";
    public static String[] convertStringtoArray(String string){
        String[] array = string.split(strSeparator);
        return array;
    }


}