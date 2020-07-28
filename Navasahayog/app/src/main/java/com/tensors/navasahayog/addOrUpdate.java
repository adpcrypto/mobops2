package com.tensors.navasahayog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class addOrUpdate extends AppCompatActivity {

    Button add,button4;
    FirebaseFirestore db;
    FirebaseUser user;
    EditText editname, editroll;
    String pictureFilePath,url;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    Uri photoURI = null;

    ImageView image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update);
        final String docID;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
        add = findViewById(R.id.button3);
        add.setEnabled(false);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        editname = findViewById(R.id.editTextName);
        editroll = findViewById(R.id.editTextRoll);
        image = findViewById(R.id.imageView);
        button4 = findViewById(R.id.button4);
        String what = getIntent().getStringExtra("What");
        if (what.equals("ADD")) {
            add.setText("ADD");
            docID = getIntent().getStringExtra("ID");
        } else {
            add.setText("UPDATE");
            docID = getIntent().getStringExtra("ID");
            db.collection(user.getEmail()).document(docID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    editname.setText(documentSnapshot.get("name").toString());
                    editroll.setText(documentSnapshot.get("rollno").toString());
                    url = documentSnapshot.get("url").toString();
                    Toast.makeText(getApplicationContext(),"Loading image...Please wait...",Toast.LENGTH_LONG).show();
                    CircularProgressDrawable circularProgressDrawable =  new CircularProgressDrawable(getApplicationContext());
                    circularProgressDrawable.setStrokeWidth(5f);
                    circularProgressDrawable.setCenterRadius(30f);
                    circularProgressDrawable.start();
                    Glide.with(getApplicationContext()).load(url).placeholder(circularProgressDrawable).into(image);
                }
            });
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(addOrUpdate.this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                add.setEnabled(false);
                add.setText("Uploading in progress");
                Date now = new Date();
                if(photoURI != null && editname.getText().toString() != null && editroll.getText().toString() != null){
                    final StorageReference fileref = FirebaseStorage.getInstance().getReference().child("images/").child(docID+"/").child(now+".jpg");
                    fileref.putFile(photoURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getApplicationContext(),"Upload Completed",Toast.LENGTH_SHORT).show();
                            fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = uri.toString();
                                    progressDialog.dismiss();
                                    Map<String,Object> map= new HashMap<>();
                                    map.put("name",editname.getText().toString());
                                    map.put("rollno",editroll.getText().toString());
                                    map.put("isstudent","true");
                                    map.put("url",url);
                                    db.collection(user.getEmail()).document(docID).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),"Details updated",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    add.setEnabled(true);
                                    add.setText("UPDATE");
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Upload Failed",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Fill the details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void capturePhoto() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            sendTakePictureIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.tensors.navasahayog.fileprovider", pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private File getPictureFile() throws IOException {
        Date now = new Date();
        String pictureFile = now.toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                image.setImageURI(Uri.fromFile(imgFile));
                add.setEnabled(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFiles(getCacheDir());
        deleteTempFiles(getExternalCacheDir());
        deleteTempFiles(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
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


}
/*ContentResolver ct = getContentResolver();
Minetypemap mintypemap = Minetypeap.getSingleton();
return minttypemap.getExtensionfomminttype(ct.getType(uRI)));
 */