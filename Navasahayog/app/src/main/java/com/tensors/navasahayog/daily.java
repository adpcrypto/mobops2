package com.tensors.navasahayog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.tensors.navasahayog.addOrUpdate.REQUEST_PICTURE_CAPTURE;


public class daily extends Fragment {



    public daily() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = getContext().openOrCreateDatabase("myDb",MODE_PRIVATE,null);

        all =new ArrayList<>();
        photoURI = new ArrayList<>();
        videoURI = new ArrayList<>();
        absentees = new ArrayList<>();
        photoURI.clear();
        videoURI.clear();
        vidcounter=0;
        counter=0;
        deleteTempFiles(getContext().getCacheDir());
        deleteTempFiles(getContext().getExternalCacheDir());
        deleteTempFiles(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    Button click,upload,attendance,video;
    String pictureFilePath;
    List<Uri> photoURI;
    List<Uri> videoURI;
    int vidcounter;
    int counter;
    EditText datetext;
    int i;
    List<String> all;
    int VIDEO_CAPTURE =101;
    List<String> absentees;
    LinearLayout ll;

    SQLiteDatabase mydb;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);



        datetext = view.findViewById(R.id.editTextDate);
        ll = view.findViewById(R.id.linearLayout);
        click = view.findViewById(R.id.button5);
        upload = view.findViewById(R.id.button6);
        video = view.findViewById(R.id.button7);
        attendance = view.findViewById(R.id.attendance);
        Log.d("asds",""+counter);
        refresh();
        getAllStudents();
        final Calendar myCalendar = Calendar.getInstance();



        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datetext.getText().toString() .equals("Enter date")) {
                    Toast.makeText(getView().getContext(), "Set a date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog dialog;
                int i=0;
                absentees.clear();
                final String allstr[] =new String[all.size()];
                for(String str:all){
                    allstr[i] = str;
                    absentees.add(i,str);
                    i++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("Take Atttendance:");
                builder.setMultiChoiceItems(allstr ,null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId, boolean isSelected) {
                        if (absentees.contains(allstr[selectedItemId])) {
                            absentees.set(selectedItemId,"0");
                        }else {
                            absentees.add(selectedItemId,allstr[selectedItemId]);
                        }
                    }
                }).setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map <String,Object> map= new HashMap<>();
                        int i1 = 0;
                        i1 = absentees.size();
                        for(String abc:absentees){
                            if(abc.equals("0")){
                                i1--;
                            }
                        }
                        String  lenght = Integer.toString(all.size() -i1);
                        for(String str:absentees) {
                            if(!str.equals("0")) {
                                map.put(str, "Absent");
                            }
                        }
                        map.put(lenght,"Present");
                        db.collection(user.getEmail()).document("daily/"+datetext.getText().toString()+"/Attendance").set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getView().getContext(),"Attendance updated",Toast.LENGTH_SHORT).show();
                            }
                        });*/
                        int i1 = 0;
                        i1 = absentees.size();
                        for(String abc:absentees){
                            if(abc.equals("0")){
                                i1--;
                            }
                        }
                        int cntr=0;
                        String[] absentstr = new String[i1] ;
                        for(int i2=0;i2<absentees.size();i2++){
                            if(!absentees.get(i2).equals("0")) {
                                absentstr[cntr] = absentees.get(i2);
                                cntr++;
                            }
                        }
                        String  lenght = Integer.toString(all.size() -i1);
                        mydb.execSQL("CREATE TABLE IF NOT EXISTS AttendanceTable(Date DATE,Present INT,Absent VARCHAR(50));");
                        mydb.execSQL("INSERT INTO AttendanceTable VALUES('"+datetext.getText()+"','"+lenght+"','"+convertArrayToString(absentstr)+"');");
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });




        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                datetext.setText(dayOfMonth+"-"+(Integer.valueOf(monthOfYear)+1)+"-"+year);
            }

        };

        datetext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getView().getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 200);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 200);
        }
        click.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureVideo();
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datetext.getText().toString() .equals("Enter date")) {
                    Toast.makeText(getView().getContext(), "Set a date first", Toast.LENGTH_SHORT).show();
                    return;
                }
                mydb.execSQL("CREATE TABLE IF NOT EXISTS PhotoVideo(Date DATE UNIQUE,Photo VARCHAR(50),Video VARCHAR(50));");
                String photos =null,videos=null;
                for(int j=0;j<photoURI.size();j++){
                    photos = photos + photoURI.get(j).toString()+",";
                }
                for(int j=0;j<videoURI.size();j++){
                    videos = videos + videoURI.get(j).toString()+",";
                }
                mydb.execSQL("REPLACE INTO PhotoVideo (Date,Photo,Video) VALUES('"+datetext.getText()+"','"+photos+"','"+videos+"');");

                Toast.makeText(getView().getContext(),"P"+photos+"V"+videos,Toast.LENGTH_SHORT).show();
            }
        });
        /*upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(getView().getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                upload.setEnabled(false);
                upload.setText("Uploading in Progress");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                for (i = 0; i < counter; i++) {
                    Toast.makeText(getContext(),"counter"+counter+"i"+i,Toast.LENGTH_SHORT).show();
                    final StorageReference fileref = FirebaseStorage.getInstance().getReference().child("daily_images/").child(user.getEmail() + "/").child(String.valueOf(java.time.LocalDate.now())).child(java.time.LocalTime.now() + String.valueOf(i) + ".jpg");
                    fileref.putFile(photoURI.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getView().getContext(), "Upload" + String.valueOf(i) + "Completed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            if( i >= counter - 1){
                                upload.setEnabled(true);
                                upload.setText("UPLOAD IT");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getView().getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
                }
                for (i = 0; i < vidcounter; i++) {
                    Toast.makeText(getContext(),"counter"+counter+"i"+i,Toast.LENGTH_SHORT).show();
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    final StorageReference fileref = FirebaseStorage.getInstance().getReference().child("daily_videos/").child(user.getEmail() + "/").child(String.valueOf(java.time.LocalDate.now())).child(java.time.LocalTime.now() + String.valueOf(i)+ "."+ mimeTypeMap.getExtensionFromMimeType(getContext().getContentResolver().getType(videoURI.get(i))));
                    fileref.putFile(videoURI.get(i)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getView().getContext(), "Upload" + String.valueOf(i) + "Completed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            if( i >= vidcounter - 1){
                                upload.setEnabled(true);
                                upload.setText("UPLOAD IT");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getView().getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
        });*/
        return view;
    }

    private void refresh() {
        Log.d("asds",""+counter);
        for(i=0;i<photoURI.size();i++){
            Toast.makeText(getContext(), ""+photoURI.get(i), Toast.LENGTH_SHORT).show();
            ImageView imageView;
            imageView = new ImageView(getContext());
            imageView.setImageURI(photoURI.get(i));
            int dimens = 100;
            float density = getResources().getDisplayMetrics().density;
            int finalDimens = (int) (dimens * density);

            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imageView.setLayoutParams(imgvwDimens);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            int dimensMargin = 4;
            float densityMargin = getResources().getDisplayMetrics().density;
            int finalDimensMargin = (int) (dimensMargin * densityMargin);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


            Log.d("asds",""+counter);
            ll.addView(imageView,i,imgvwMargin);
        }
        for(i=0;i<videoURI.size();i++){
            VideoView videoView = new VideoView(getContext());
            videoView.setVideoURI(videoURI.get(i));
            int dimens = 100;
            float density = getResources().getDisplayMetrics().density;
            int finalDimens = (int) (dimens * density);

            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            videoView.setLayoutParams(imgvwDimens);


            int dimensMargin = 4;
            float densityMargin = getResources().getDisplayMetrics().density;
            int finalDimensMargin = (int) (dimensMargin * densityMargin);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


            ll.addView(videoView,i,imgvwMargin);
            videoView.requestFocus();
            videoView.start();
        }
    }

    private void captureVideo() {
        if (getView().getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            sendTakeVideoIntent();
        }
    }

    private void sendTakeVideoIntent() {
        Intent VideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (VideoIntent.resolveActivity(getView().getContext().getPackageManager()) != null) {
            startActivityForResult(VideoIntent, VIDEO_CAPTURE);
        }
    }


    private void getAllStudents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        all.clear();
        db.collection(user.getEmail()).whereEqualTo("isstudent","true").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(final DocumentSnapshot doc: task.getResult()) {
                    all.add((String) doc.get("name"));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void capturePhoto() {
        if (getView().getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            sendTakePictureIntent();
        }
    }

    private void sendTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getView().getContext().getPackageManager()) != null) {
            File pictureFile = null;
            pictureFile = getPictureFile();
            if (pictureFile != null) {
                photoURI.add(counter,FileProvider.getUriForFile(getView().getContext(), "com.tensors.navasahayog.fileprovider", pictureFile));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.get(counter));
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    private File getPictureFile() {
        Date now = new Date();
        String pictureFile = now.toString();
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir,pictureFile+".jpg");
        pictureFilePath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            ImageView imageView;
            imageView = new ImageView(getContext());
            imageView.setImageURI(Uri.fromFile(imgFile));
            int dimens = 100;
            float density = getResources().getDisplayMetrics().density;
            int finalDimens = (int) (dimens * density);

            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imageView.setLayoutParams(imgvwDimens);

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            int dimensMargin = 4;
            float densityMargin = getResources().getDisplayMetrics().density;
            int finalDimensMargin = (int) (dimensMargin * densityMargin);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


            ll.addView(imageView,counter,imgvwMargin);

            try {
                saveToGallery();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            upload.setEnabled(true);
            counter++;
            Log.d("asds",""+counter);
        }else if(requestCode == VIDEO_CAPTURE && resultCode == RESULT_OK){
            videoURI.add(data.getData());
            VideoView videoView = new VideoView(getView().getContext());
            videoView.setVideoURI(videoURI.get(vidcounter));
            int dimens = 100;
            float density = getResources().getDisplayMetrics().density;
            int finalDimens = (int) (dimens * density);

            LinearLayout.LayoutParams imgvwDimens = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            videoView.setLayoutParams(imgvwDimens);


            int dimensMargin = 4;
            float densityMargin = getResources().getDisplayMetrics().density;
            int finalDimensMargin = (int) (dimensMargin * densityMargin);

            LinearLayout.LayoutParams imgvwMargin = new LinearLayout.LayoutParams(finalDimens, finalDimens);
            imgvwMargin.setMargins(finalDimensMargin, finalDimensMargin, finalDimensMargin, finalDimensMargin);


            ll.addView(videoView,vidcounter,imgvwMargin);
            videoView.requestFocus();
            videoView.start();

            upload.setEnabled(true);
            vidcounter++;
        }
    }


    private void saveToGallery() throws FileNotFoundException {
        Date now = new Date();
        String gallerySrc = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),pictureFilePath, now.toString(), "Images of Navasahayog");
        Toast.makeText(getContext(),"Image saved at "+gallerySrc,Toast.LENGTH_LONG).show();
    }

    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }
    public static String strSeparator = ",";
    public static String convertArrayToString(String[] array){
        String str = " ";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

}