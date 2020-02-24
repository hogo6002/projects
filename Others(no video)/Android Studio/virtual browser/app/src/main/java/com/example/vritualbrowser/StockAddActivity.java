package com.example.vritualbrowser;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.InputStream;

public class StockAddActivity extends AppCompatActivity{
    private TextView policy;
    private TextView feedback;
    private ImageView backBtn;
    private ImageView storeImage;
    private Button addImageButton;
    private EditText productname;
    private EditText price;
    private EditText stock;
    private EditText productDescription;
    private Button confirmBtn;
    private Button scanBtn;
    private Bitmap bitmap;
    private String productId;
    private String image_name;

    //Todo Add Barcode scanner for product_id scan
    //Todo Add support for store_id
    /**
     * Create the feedback page, by putting activity_feedback layout into this view, and set action
     * for both back button and policy button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_add);

        ImageView backBtn = findViewById(R.id.backBtn);
        policy = findViewById(R.id.policy);
        feedback = findViewById(R.id.textView37);
        this.backBtn = findViewById(R.id.backBtn);
        storeImage = findViewById(R.id.store_image);
        addImageButton = findViewById(R.id.addImagebutton);
        productname = findViewById(R.id.productNameEdit);
        price = findViewById(R.id.priceEdit);
        stock = findViewById(R.id.stockEdit);
        productDescription = findViewById(R.id.descriptionText);
        confirmBtn = findViewById(R.id.confirmButton);
        scanBtn = findViewById(R.id.scanBtn);

        try{
        if(getIntent().getExtras().get("image_url")!=null){
            Glide.with(this).asBitmap().load(getIntent().getExtras().get("image_url")).into(storeImage);
            //storeImage.setImageBitmap((Bitmap)getIntent().getExtras().get("Image_URL"));
        }

        if(getIntent().getExtras().get("stock_name")!=null){
            productname.setText((String)getIntent().getExtras().get("stock_name"));
        }

        if(getIntent().getExtras().get("stock_description")!=null){
            String stockResult = (String)getIntent().getExtras().get("stock_description");


            stock.setText(stockResult);
        }

        if(getIntent().getExtras().get("price")!=null) {
            price.setText((String) getIntent().getExtras().get("price"));
        }
        } catch (NullPointerException e){
            ;
        }



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setListeners();
    }

    /**
     * set listeners to policy button.
     */
    private void setListeners() {
        Onclick onClick = new Onclick();

        policy.setOnClickListener(onClick);
        addImageButton.setOnClickListener(onClick);
        confirmBtn.setOnClickListener(onClick);
        scanBtn.setOnClickListener(onClick);
    }

    /**
     * add the even for policy button.
     * when user click policy text view, it will bring user to PolicyActivity.
     */
    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.policy:
                    intent = new Intent(StockAddActivity.this, PolicyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.addImagebutton:
                    intent = new Intent(Intent.ACTION_PICK ,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    System.out.println("Intent set");
                    startActivityForResult(intent, 1);
                    break;
                case R.id.confirmButton:
                    validateStockAdd();
                    break;
                case R.id.scanBtn:
                    intent = new Intent(Intent.ACTION_PICK ,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //intent.setType("image/*");
                    System.out.println("Intent set");
                    startActivityForResult(intent, 2);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("method called");
        if (requestCode == 1) {
            System.out.println("correct requestCode");
            Uri image = data.getData();
            System.out.println("Path:"+image.getPath());
            String realUrl =getPath(image);
            String[] image_path = realUrl.split("/");

            image_name = image_path[image_path.length-1];

            System.out.println("Real image name:"+image_name);
            storeImage.setImageURI(image);


        } else if(requestCode ==2){
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

            Integer parse = Integer.parseInt(result);
            productId = parse.toString();

        } else{
            return;
        }
    }

    /**
     * Update the back end database
     */

    //"5" is product id, supported by barcode scanner
    //Todo Add Image support
    private void validateStockAdd() {
        String pass_url = null;
        String shoe_url = "https://deco3801-hungryrose.uqcloud.net/images/stock/sport_shoe.jpg";
        String durian_url ="https://deco3801-hungryrose.uqcloud.net/images/stock/durian.jpg";
        String milk_url = "https://deco3801-hungryrose.uqcloud.net/images/stock/milk.jpg";
        String blueberry_url ="https://deco3801-hungryrose.uqcloud.net/images/stock/blueberry.jpg";

        if(image_name.equals("sport_shoe.jpg")){
            pass_url = shoe_url;
        } else if (image_name.equals("milk.jpg")){
            pass_url = milk_url;
        } else if (image_name.equals("durian.jpg")){
            pass_url = durian_url;
        } else if (image_name.equals("blueberry.jpg")){
            pass_url = blueberry_url;
        }

        System.out.println("pass_url");

        String type = "stockAdd";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, productname.getText().toString(), price.getText().toString(),
                stock.getText().toString(), productDescription.getText().toString(),productId, pass_url);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
