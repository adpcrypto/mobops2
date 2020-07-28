package com.tensors.navasahayog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.tensors.navasahayog.addOrUpdate.REQUEST_PICTURE_CAPTURE;


public class daily extends Fragment {



    public daily() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Button click,upload;
    String pictureFilePath;
    List<Uri> photoURI;
    int counter=0;
    int i;
    LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_daily, container, false);
        deleteTempFiles(getContext().getCacheDir());
        deleteTempFiles(getContext().getExternalCacheDir());
        deleteTempFiles(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        click = view.findViewById(R.id.button5);
        upload = view.findViewById(R.id.button6);
        photoURI = new ArrayList<>();
        ll = view.findViewById(R.id.linearLayout);
        upload.setEnabled(false);
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
        upload.setOnClickListener(new View.OnClickListener() {
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

            }
        });
        return view;
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


            ll.addView(imageView,counter);

            try {
                saveToGallery();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            upload.setEnabled(true);
            counter++;
        }
    }

    private void saveToGallery() throws FileNotFoundException {
        Date now = new Date();
        String gallerySrc = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),pictureFilePath, now.toString(), "Images of Navasahayog");
        Toast.makeText(getContext(),"Image saved at "+gallerySrc,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteTempFiles(getContext().getCacheDir());
        deleteTempFiles(getContext().getExternalCacheDir());
        deleteTempFiles(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
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