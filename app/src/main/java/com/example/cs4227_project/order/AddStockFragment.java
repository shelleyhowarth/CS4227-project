package com.example.cs4227_project.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cs4227_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class AddStockFragment extends Fragment implements View.OnClickListener {

    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private ImageView userPicture;
    private String path, category;
    private EditText pName, size1, q1, size2, q2, size3, q3;
    private RadioGroup genderGroup, categoryGroup;
    private boolean female;

    private final int PICK_IMAGE_REQUEST = 22;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int PERMISSION_CODE = 1000;

    public AddStockFragment() {
        // Required empty public constructor
    }

    public static AddStockFragment newInstance() {
        AddStockFragment fragment = new AddStockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_stock, container, false);

        Button uploadPic = rootView.findViewById(R.id.uploadImage);
        Button submit = rootView.findViewById(R.id.finish);

        pName = rootView.findViewById(R.id.productName);
        size1 = rootView.findViewById(R.id.size1);
        size2 = rootView.findViewById(R.id.size2);
        size3 = rootView.findViewById(R.id.size3);
        q1 = rootView.findViewById(R.id.quantity1);
        q2 = rootView.findViewById(R.id.quantity2);
        q3 = rootView.findViewById(R.id.quantity3);

        genderGroup = rootView.findViewById(R.id.genderGroup);
        categoryGroup = rootView.findViewById(R.id.categoryGroup);

        uploadPic.setOnClickListener(this);
        submit.setOnClickListener(this);

        userPicture = rootView.findViewById(R.id.productPic);
        Picasso.get().load("http://s3.amazonaws.com/37assets/svn/765-default-avatar.png").fit().centerCrop().into(userPicture);

        return rootView;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadPic(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Random rand = new Random();
            int int_random = rand.nextInt(10000);
            path = "images/" + int_random;
            StorageReference ref = storageReference.child(path);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Log.d("Check",""+ filePath);
            Picasso.get().load(filePath).centerCrop().fit().into(userPicture);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Log.d("Check"," checking activity");
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            Log.d("Check"," " + photo);
            userPicture.setImageBitmap(photo);
            Uri tempUri = getImageUri(getContext(), photo);
            filePath = tempUri;
            Log.d("Check"," " + tempUri);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Log.d("Check","get bitmap");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void validateInput(){
        HashMap<String, String> sizeQuantities = new HashMap<>();
        String productName = pName.getText().toString();
        String s1 = size1.getText().toString();
        String s2 = size2.getText().toString();
        String s3 = size3.getText().toString();
        String quant1 = q1.getText().toString();
        String quant2 = q2.getText().toString();
        String quant3 = q3.getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.female) {
                    female=true;
                } else if(checkedId == R.id.male) {
                    female=false;
                }
            }
        });

        categoryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.clothes){
                    category = "clothes";
                }else if(checkedId == R.id.shoes){
                    category = "shoes";
                }else if(checkedId == R.id.accessories){
                    category = "accessories";
                }
            }
        });

        if(s1 != null && quant1 != null){
            sizeQuantities.put(s1, quant1);
        }
        if(s2 != null && quant2 != null){
            sizeQuantities.put(s2, quant2);
        }
        if(s3 != null && quant3 != null){
            sizeQuantities.put(s3, quant3);
        }

        if(productName == null){
            pName.setError("Must have product name");
        }else if(sizeQuantities.size() == 0){
            Toast.makeText(getActivity(), "Product must contain stock", Toast.LENGTH_SHORT).show();
        }else{
            createProductAndStock(sizeQuantities);
        }
    }

    public void createProductAndStock(HashMap<String, String> sizeQuantities){
        //Product product = new Product();
    }

    @Override
    public void onClick(View v){
        int i = v.getId();

        if(i == R.id.uploadImage){
            selectImage();
        }
        if(i == R.id.finish){
            uploadPic();
        }
    }
}