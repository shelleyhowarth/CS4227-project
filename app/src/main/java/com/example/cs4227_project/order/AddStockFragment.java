package com.example.cs4227_project.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.example.cs4227_project.database.Database;
import com.example.cs4227_project.database.ProductDatabaseController;
import com.example.cs4227_project.database.StockDatabaseController;
import com.example.cs4227_project.misc.ProductType;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.commandPattern.AddStock;
import com.example.cs4227_project.order.commandPattern.CommandControl;
import com.example.cs4227_project.order.commandPattern.Stock;
import com.example.cs4227_project.products.abstractFactoryPattern.AbstractFactory;
import com.example.cs4227_project.products.abstractFactoryPattern.FactoryProducer;
import com.example.cs4227_project.products.abstractFactoryPattern.Product;
import com.example.cs4227_project.products.ProductTypeController;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AddStockFragment extends Fragment implements View.OnClickListener {

    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    private ImageView userPicture;
    private String path;
    private EditText pName, size1, q1, size2, q2, size3, q3, price, colour, brand, style;
    private RadioGroup genderGroup, categoryGroup;
    private AbstractFactory productFactory;
    private boolean female;
    private ProductType pType;

    private final int PICK_IMAGE_REQUEST = 22;
    private final int REQUEST_IMAGE_CAPTURE = 1;

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

        Random rand = new Random();
        int int_random = rand.nextInt(10000);
        path = "images/" + int_random;

        pName = rootView.findViewById(R.id.productName);
        size1 = rootView.findViewById(R.id.size1);
        size2 = rootView.findViewById(R.id.size2);
        size3 = rootView.findViewById(R.id.size3);
        q1 = rootView.findViewById(R.id.quantity1);
        q2 = rootView.findViewById(R.id.quantity2);
        q3 = rootView.findViewById(R.id.quantity3);
        price = rootView.findViewById(R.id.price);
        colour = rootView.findViewById(R.id.colour);
        brand = rootView.findViewById(R.id.brand);
        style = rootView.findViewById(R.id.style);

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
            StorageReference ref = storageReference.child(path);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
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
        String cost = price.getText().toString();
        String brandName = brand.getText().toString();
        String color = colour.getText().toString();
        String productStyle = style.getText().toString();
        String s1 = size1.getText().toString();
        String s2 = size2.getText().toString();
        String s3 = size3.getText().toString();
        String quant1 = q1.getText().toString();
        String quant2 = q2.getText().toString();
        String quant3 = q3.getText().toString();

        int genderId = genderGroup.getCheckedRadioButtonId();
        if(genderId == R.id.female) {
            Log.d(LogTags.COMMAND_DP, "Is female selected= " + female);
            female=true;
        } else if(genderId == R.id.male) {
            Log.d(LogTags.COMMAND_DP, "Is female selected= " + female);
            female=false;
        }

        int checkedId = categoryGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.clothes){
            pType = ProductType.CLOTHES;
        }else if(checkedId == R.id.shoes){
            pType = ProductType.SHOE;
        }else if(checkedId == R.id.accessories){
            pType = ProductType.ACCESSORIES;
        }

        Log.d(LogTags.COMMAND_DP, "Product type" + pType.getValue());
        if(s1 != "" && quant1 != ""){
            sizeQuantities.put(s1, quant1);
        }
        if(s2 != "" && quant2 != ""){
            sizeQuantities.put(s2, quant2);
        }
        if(s3 != "" && quant3 != ""){
            sizeQuantities.put(s3, quant3);
        }

        if(productName == ""){
            pName.setError("Must have product name");
        }else if(sizeQuantities.size() == 0){
            Toast.makeText(getActivity(), "Product must contain stock", Toast.LENGTH_SHORT).show();
        }else if(brandName == null || color == null || cost == null || productStyle == null){
            Toast.makeText(getActivity(), "All fields must be filled", Toast.LENGTH_SHORT).show();
        }else{
            createProduct(sizeQuantities, productName, color, brandName, cost, productStyle);
        }
    }

    public void createProduct(HashMap<String, String> sizeQuantities, String productName, String color, String brandName, String cost, String style){
        HashMap<String, Object> productData = new HashMap<>();
        productFactory = FactoryProducer.getFactory(female);
        List<String> sizes = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        for(Map.Entry<String, String> entry :  sizeQuantities.entrySet()){
            sizes.add(entry.getKey());
            quantities.add(entry.getValue());
        }
        //Create hashmap containing product values.
        productData.put("name", productName);
        productData.put("price", Double.parseDouble(cost));
        productData.put("sizes", sizes);
        productData.put("sizeQuantities", quantities);
        productData.put("brand", brandName);
        productData.put("colour", color);
        productData.put("style", style);
        productData.put("imageURL", path);

        String collection = pType.getValue() + female;
        //Create product and add to database
        Product p = productFactory.getProduct(pType, productData);
        Log.d(LogTags.COMMAND_DP, "New Product create: " + p.toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection(collection).document();
        String productId = ref.getId();
        ProductDatabaseController productDb = new ProductDatabaseController();
        productDb.addProductToDB(collection, productId, p);

        createStock(productId, sizeQuantities);
    }

    public void createStock(String id, HashMap<String, String> sizeQ){
        CommandControl commandController = new CommandControl();
        Stock stock = new Stock(id, sizeQ, pType.getValue(), female);
        AddStock addStock = new AddStock(stock);
        commandController.addCommand(addStock);
        commandController.executeCommands();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack ("addProduct", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onClick(View v){
        int i = v.getId();

        if(i == R.id.uploadImage){
            selectImage();
        }
        if(i == R.id.finish){
            validateInput();
            uploadPic();
        }
    }
}