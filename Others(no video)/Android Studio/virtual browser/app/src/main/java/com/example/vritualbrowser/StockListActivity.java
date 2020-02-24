package com.example.vritualbrowser;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vritualbrowser.Adapter.StockViewAdaptor;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class StockListActivity extends AppCompatActivity{
    /**
     * String tag
     */
    private static final String TAG = "MainActivity";
    /**
     * The policy
     */
    private TextView policy;
    /**
     * The profile
     */
    private ImageView profile;
    /**
     * Image list
     */
    private ArrayList<String> mImage = new ArrayList<>();
    /**
     * Storename list
     */
    private ArrayList<String> mStockName = new ArrayList<>();
    /**
     * storeDescription list
     */
    private ArrayList<String> mStockAmount = new ArrayList<>();

    private ArrayList<String> mProductId = new ArrayList<>();

    private ArrayList<String> mPrice = new ArrayList<>();

    /**
     * resource
     */
    private String resource;
    /**
     * resource array
     */
    private ArrayList<String> resourceArray = new ArrayList<>();
    /**
     * A final resource array
     */
    private ArrayList<String[] > finalresourceArray = new ArrayList<>();

    private EditText searchtext;

    private ImageView searchButton;

    private String originalMessage;

    private Button addButton;

    private Button barcodeButton;

    private String selection;

    private String currentPhotoPath;
    /**
     * Initialize the activity
     *
     * @param savedInstanceState - previous saved state of this activity, can be null if no
     *                              data provided
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_search);

        ImageView backBtn = findViewById(R.id.backBtn);

        Log.d(TAG, "onCreate: started.");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        policy = findViewById(R.id.policy);
        profile = findViewById(R.id.profile);
        resource = (String) getIntent().getExtras().get("resource");
        searchtext = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchBtn);
        addButton = findViewById(R.id.addButton);
        barcodeButton = findViewById(R.id.barcodeButton);

        System.out.println("Intent: "+getIntent().getExtras().get("FROM_ACTIVITY"));
        if(getIntent().getExtras().get("FROM_ACTIVITY").equals("menu")){
            originalMessage = resource;
        } else if(getIntent().getExtras().get("FROM_ACTIVITY").equals("StoreList")){
            originalMessage = getIntent().getExtras().get("original").toString();
        } else if (getIntent().getExtras().get("FROM_ACTIVITY").equals("promotionAdd")){
            originalMessage = resource;
        }

        System.out.println("Original: "+originalMessage);

        setListeners();
        initImageBitMaps();
    }

    /**
     * Set the onClick listener for all the buttons
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
        profile.setOnClickListener(onClick);
        searchButton.setOnClickListener(onClick);
        addButton.setOnClickListener(onClick);
        barcodeButton.setOnClickListener(onClick);

    }

    /**
     * Acheive the function of the buttons, use intent to connect current activity to another
     * activity
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(StockListActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    intent = new Intent(StockListActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.searchBtn:
                    String result = passContent();
                    if(result.isEmpty()){
                        System.out.println("empty");
                        validate();
                    } else {
                        System.out.println("filtered");
                        StockListActivity.this.finish();
                        intent = new Intent(StockListActivity.this, StockListActivity.class);
                        intent.putExtra("resource",result);
                        intent.putExtra("original", originalMessage);
                        intent.putExtra("FROM_ACTIVITY", "StoreList");
                        startActivity(intent);
                    }
                    break;
                case R.id.addButton:
                    intent = new Intent(StockListActivity.this, StockAddActivity.class);
                    startActivity(intent);
                    break;
                 //Todo barcode implementation
                case R.id.barcodeButton:
                    AlertDialog.Builder dialogBox = new AlertDialog.Builder(StockListActivity.this);
                    dialogBox.setTitle("Method select");
                    dialogBox.setMessage("Where do you want to select the image?");

                    dialogBox.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            selection = "camera";
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                File photoFile = null;
                                try{
                                    photoFile = createImageFile();
                                } catch (IOException e){
                                    //Todo handle exception
                                    ;
                                }
                                System.out.println("photoFile: "+photoFile);
                                if(photoFile!=null){
                                        Uri photoUri = FileProvider.getUriForFile(StockListActivity.this,
                                            "com.example.vritualbrowser.fileprovider",photoFile);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                                    startActivityForResult(intent, 2);
                                }
                            }

                            dialog.dismiss();
                        }
                    });
                    dialogBox.setNegativeButton("Image", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selection = "image";
                            Intent intent = new Intent(Intent.ACTION_PICK ,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            //intent.setType("image/*");
                            System.out.println("Intent set");
                            startActivityForResult(intent, 1);
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = dialogBox.create();
                    alert.show();


                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("method called");
        if (requestCode == 2) {
            /*
            System.out.println("correct requestCode");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            System.out.println(imageBitmap);


            BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                    .build();
            if(!detector.isOperational()){
                System.out.println("detector not working");
            }

            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<Barcode> barcode = detector.detect(frame);

            System.out.println("barcode array:"+barcode);

            Barcode thisCode = barcode.valueAt(0);
            String result = thisCode.rawValue;
            System.out.println("result of barCode scanned: "+result);*/



            Intent intent = new Intent(StockListActivity.this,StockAddActivity.class);

            //Integer parse = Integer.parseInt(result);
            //int index = mProductId.indexOf(parse.toString());

            intent.putExtra("image_url", mImage.get(1));
            //intent.putExtra("Image_URL", imageBitmap);
            intent.putExtra("stock_name",mStockName.get(1));
            intent.putExtra("stock_description", mStockAmount.get(1));
            intent.putExtra("Product_id", mProductId.get(1));

            startActivity(intent);



        } else if (requestCode == 1){

            System.out.println("correct requestCode");
            Uri image = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
            } catch (Exception e){
                bitmap = null;
            }

            BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext()).
                    setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE | Barcode.CODE_128).build();
            if(!detector.isOperational()){
                System.out.println("detector not working");
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcode = detector.detect(frame);

            System.out.println("barcode array:"+barcode);

            Barcode thisCode = barcode.valueAt(0);
            String result = thisCode.rawValue;
            System.out.println("result of barCode scanned: "+result);



            Intent intent = new Intent(StockListActivity.this,StockAddActivity.class);
            Integer parse = Integer.parseInt(result);
            int index = mProductId.indexOf(parse.toString());


            intent.putExtra("image_url", mImage.get(index));
            intent.putExtra("stock_name",mStockName.get(index));
            intent.putExtra("stock_description", mStockAmount.get(index));
            intent.putExtra("Product_id", mProductId.get(index));

            startActivity(intent);
        } else {
            return;
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println("currentphotopath: "+currentPhotoPath);
        return image;
    }



    /**
     * Initialize all the images
     */
    private void initImageBitMaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");



        String[] resources = resource.split("\\^");

        System.out.println("resources " + Arrays.toString(resources));

        for (String element: resources){
            resourceArray.add(element);
        }

        resourceArray.remove(0);

        for (String elements : resourceArray){
            String[] a = elements.split("\\*");
            finalresourceArray.add(a);
        }

        System.out.println(finalresourceArray);

        for(String[] finalresource:finalresourceArray){
            mImage.add(finalresource[0]);
            mStockName.add(finalresource[1]);
            mStockAmount.add(finalresource[2]);
            mProductId.add(finalresource[3]);
            mPrice.add(finalresource[4]);
                    }

        System.out.println("Image"+mImage);
        System.out.println("StockName"+mStockName);
        System.out.println("StoreAmount"+mStockAmount);
        System.out.println("ProductId"+mProductId);
        System.out.println("Price"+mPrice);


        initRecycleView();
    }

    /**
     * Initialize the recyclerView for promotion
     */
    private void initRecycleView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        StockViewAdaptor adaptor = new StockViewAdaptor(mImage,mStockName,mStockAmount,mPrice, mProductId,this, getIntent());
        //System.out.println(adaptor.getmImage());
        //System.out.println(adaptor.getmStoreName());
        //System.out.println(adaptor.getmStoreDescription());
        //System.out.println(adaptor.getItemCount());
        //System.out.println(adaptor.getmContext());

        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private String passContent(){
        String result = "storelist";
        String searchString = searchtext.getText().toString().toLowerCase().trim();
        System.out.println("EditText: "+searchString);

        if(searchString.length()!=0){
            System.out.println("length success");
            getFullList();
            for(int i=0; i<mStockName.size(); i++) {
                if (mStockName.get(i).toLowerCase().contains(searchString)) {
                    System.out.println("Can find the name in list");
                    String item = mImage.get(i) + "*" + mStockName.get(i) + "*" + mStockAmount.get(i)+"*"+mProductId.get(i)+"*"+mPrice.get(i);
                    result = result + "^" + item;
                }
            }
        } else{
            getFullList();
            for(int i=0; i<mStockName.size(); i++) {

                String item = mImage.get(i) + "*" + mStockName.get(i) + "*" + mStockAmount.get(i)+"*"+mProductId.get(i)+"*"+mPrice.get(i);
                result = result + "^" + item;

            }
        }
        System.out.println("Result: "+result);

        return result;

    }

    private void validate() {
        String type = "stock";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type);
    }

    private void getFullList() {
        if(getIntent().getExtras().get("FROM_ACTIVITY").equals("StoreList")){
            String[] resources = ((String)getIntent().getExtras().get("original")).split("\\^");
            resourceArray.clear();
            for (String element: resources){
                resourceArray.add(element);
            }
            resourceArray.remove(0);
            finalresourceArray.clear();
            for (String elements : resourceArray){
                String[] a = elements.split("\\*");
                finalresourceArray.add(a);
            }
            mImage.clear();
            mStockName.clear();
            mStockAmount.clear();
            mProductId.clear();
            mPrice.clear();

            for(String[] finalresource:finalresourceArray){
                mImage.add(finalresource[0]);
                mStockName.add(finalresource[1]);
                mStockAmount.add(finalresource[2]);
                mProductId.add(finalresource[3]);
                mPrice.add(finalresource[4]);
                            }
        }
    }

    private String searchText(String searchString, String result) {
        for(int i=0; i<mStockName.size(); i++) {
            if (mStockName.get(i).toLowerCase().contains(searchString)) {
                System.out.println("Can find the name in list");
                String item = mImage.get(i) + "*" + mStockName.get(i) + "*" + mStockAmount.get(i)+"*"+mProductId.get(i)+"*"+mPrice.get(i);

                result = result + "^" + item;
            }
        }
        return result;
    }


}
