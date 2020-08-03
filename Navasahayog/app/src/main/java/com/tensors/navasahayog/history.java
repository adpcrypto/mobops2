package com.tensors.navasahayog;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String[] photoString,videoString;
    int pendingatt,pendingvid;
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
        photoCount=0;
        videoCount=0;
        pendingatt=0;
        pendingvid=0;
        check();
        checkvid();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int j =0;
                for (j = 0; j < pendingatt; j++) {
                    Map<String, Object> map = new HashMap<>();
                    Cursor result;
                    result = mydb.rawQuery(" SELECT * FROM AttendanceTable ;", null);
                    result.moveToFirst();
                    for(int i=0;i<j;i++){
                        result.moveToNext();
                    }
                    map.put("Present",result.getString(1));
                    map.put("Absentees",result.getString(2));


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
        for(int j=0;j<pendingvid;j++){
            LinearLayout linear = new LinearLayout(getContext());
            linear.setOrientation(LinearLayout.VERTICAL);
            TextView att = new TextView(getContext());
            att.setText("Pending Photo and Video:");
            Cursor result;
            result = mydb.rawQuery(" SELECT * FROM PhotoVideo ;", null);
            result.moveToFirst();
            for(int i=0;i<j;i++){
                result.moveToNext();
            }
            linear.addView(att);
            TextView date = new TextView(getContext());
            date.setText("DATE"+result.getString(0));
            photoString = convertStringtoArray(result.getString(1));
            photoCount = photoString.length;
            videoString = convertStringtoArray(result.getString(2));
            videoCount = videoString.length;
            LinearLayout lon = new LinearLayout(getContext());
            lon.setOrientation(LinearLayout.HORIZONTAL);
            int i;
            for(i=0;i<photoCount;i++){
                ImageView imgview = new ImageView(getContext());
                Uri uri = Uri.parse(photoString[i]);
                Toast.makeText(getContext(), ""+uri, Toast.LENGTH_LONG).show();
                imgview.setImageURI(uri);
                int dimens = 100;
                float density = getResources().getDisplayMetrics().density;
                int finalDimens = (int) (dimens * density);

                LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
                imgview.setLayoutParams(imgvwDimens);

                imgview.setScaleType(ImageView.ScaleType.FIT_XY);

                int dimensMargin = 4;
                float densityMargin = getResources().getDisplayMetrics().density;
                int finalDimensMargin = (int) (dimensMargin * densityMargin);

                LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
                imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);
                lon.addView(imgview,imgvwMargin);
            }
            for(i=0;i<videoCount;i++){
                VideoView videoView = new VideoView(getContext());
                Uri uri = Uri.parse(videoString[i]);
                videoView.setVideoURI(uri);
                lon.addView(videoView);
            }
            linear.addView(date);
            linear.addView(lon);
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