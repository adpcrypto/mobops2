/*package com.tensors.SahyogDemoApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
                db.collection(user.getEmail()).document("Details").set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
*/

package com.tensors.SahyogDemoApp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Environment;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
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
import com.tensors.SahyogDemoApp.R;
import com.tensors.SahyogDemoApp.Student;
import com.tensors.SahyogDemoApp.addOrUpdate;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class details extends Fragment {


    private static final int REQUEST_PICTURE_CAPTURE = 1;

    public details() {

    }


    EditText textName,textMail,textVillage;
    Button update,addb;
    String pictureFilePath;
    ImageView coordimg;
    TableLayout tableLayout;
    FirebaseFirestore db;
    Uri photoURI;
    FirebaseUser user;
    View view;
    Button coordimgbutton;
    String coordurl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_details, container, false);
        coordimg = view.findViewById(R.id.imageView2);
        coordimgbutton = view.findViewById(R.id.button9);
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
                    try {
                        textName.setText(documentSnapshot.get("Name").toString());
                        textMail.setText(documentSnapshot.get("Email").toString());
                        textVillage.setText(documentSnapshot.get("Village").toString());
                    }catch (Exception e){
                        textName.setText("Enter Name");
                        textMail.setText(user.getEmail());
                        textVillage.setText("Enter Village");
                    }
                    try {
                        coordurl = documentSnapshot.get("url").toString();
                    }catch (Exception e){
                        coordurl = "";
                    }
                    if(!coordurl.equals("")) {
                        Toast.makeText(getContext(), "Loading image...Please wait...", Toast.LENGTH_LONG).show();
                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
                        circularProgressDrawable.setStrokeWidth(5f);
                        circularProgressDrawable.setCenterRadius(30f);
                        circularProgressDrawable.start();
                        Glide.with(getContext()).load(coordurl).placeholder(circularProgressDrawable).into(coordimg);
                    }
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

        coordimgbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                user = FirebaseAuth.getInstance().getCurrentUser();
                Date now= new Date();
                if(photoURI!=null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    update.setEnabled(false);
                    update.setText("Uploading");
                    final StorageReference fileref = FirebaseStorage.getInstance().getReference().child("images/").child(user.getEmail() + "/").child(now + ".jpg");
                    fileref.putFile(photoURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getContext(), "Photo Upload Completed", Toast.LENGTH_SHORT).show();
                            fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    coordurl = uri.toString();
                                    progressDialog.dismiss();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Name", textName.getText().toString());
                                    map.put("Email", textMail.getText().toString());
                                    map.put("Village", textVillage.getText().toString());
                                    map.put("url", coordurl);
                                    db.collection(user.getEmail()).document("Details").set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Details updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    update.setEnabled(true);
                                    update.setText("UPDATE");
                                    try {
                                        deleteTempFiles(new File(pictureFilePath));
                                    }catch (Exception e){

                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
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
                    final Student student1 = doc.toObject(Student.class);

                    TextView nm = new TextView(getContext());
                    TextView  rl = new TextView(getContext());
                    Button button = new Button(getContext());
                    button.setText("Update");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), addOrUpdate.class);
                            intent.putExtra("What","UPDATE");
                            intent.putExtra("ID",student1.getRollno());
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void capturePhoto() {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            sendTakePictureIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(), "com.tensors.SahyogDemoApp.fileprovider", pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private File getPictureFile() throws IOException {
        Date now = new Date();
        String pictureFile = now.toString();
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                coordimg.setImageURI(Uri.parse(pictureFilePath));
            }else {
            }
        }
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
