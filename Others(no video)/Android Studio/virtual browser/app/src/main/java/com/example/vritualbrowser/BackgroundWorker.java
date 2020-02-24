package com.example.vritualbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * this class is used to input and output data with php file.
 * it can do multiple actions, and then send those information to those activity which needs them.
 * Tutorial link: https://www.youtube.com/watch?v=eldh8l8yPew&t=1765s
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {

    // context that this background worker is working on.
    Context context;

    // result of an action.
    public String result;

    /**
     * construct of this background worker, set the context as given ctx, and initial
     * result to an empty string.
     * @param ctx
     */
    public BackgroundWorker(Context ctx) {
        context = ctx;
        result = "";
    }

    /**
     * this method is used to deal with the input and output. To begin with, it will
     * determinate the type of this action by using the first params.
     * then, it will connect to specific php file, to send request and receive response.
     * After that, it will pass the result to other method.
     * @param params receive from constructor
     * @return - result the response from php file.
     */
    @Override
    protected String doInBackground(String... params) {
        result = "";
        String type = params[0];
        System.out.println(type);
        // Needs to be replaced by login.php
        String login_url = "https://deco3801-hungryrose.uqcloud.net/login.php";
        // Needs to be replaced by storelist.php
        String storelist_url = "https://deco3801-hungryrose.uqcloud.net/storelist.php";
        // Needs to be replaced by register.php
        String register_url = "https://deco3801-hungryrose.uqcloud.net/register.php";
        // Needs to be replaced by stores.php
        String stores_url = "https://deco3801-hungryrose.uqcloud.net/stores.php";
        // Needs to be replaced by stock.php
        String stock_url = "https://deco3801-hungryrose.uqcloud.net/stock.php";
        // Needs to be replaced by stockadd.php
        String stock_add_url = "https://deco3801-hungryrose.uqcloud.net/stockadd.php";
        // Needs to be replaced by storepromotion.php
        String store_promotion_url = "https://deco3801-hungryrose.uqcloud.net/storepromotion.php";
        // Needs to be replaced by promotion_add.php
        String promotion_add_url = "https://deco3801-hungryrose.uqcloud.net/promotionadd.php";
        // Needs to be replaced by stock1.php
        String stock_url1 = "https://deco3801-hungryrose.uqcloud.net/stock1.php";

        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                String user_type = params[3];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" +
                        URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8")+ "&" + URLEncoder.encode("type", "UTF-8") +"="+
                        URLEncoder.encode(user_type, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("storelist")){
            try{
                URL url = new URL(storelist_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                System.out.println("test");
                httpURLConnection.connect();
                System.out.print("connected");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("register")) {
            try {
                String user_name = params[1];
                System.out.println(user_name);
                String password = params[2];
                System.out.println(password);
                String email = params[3];
                System.out.println(email);
                String address1 = params[4];
                System.out.println(address1);
                String address2 = params[5];
                System.out.println(address2);
                String usertype = params[6];
                System.out.println("usertype"+usertype);
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" +
                        URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8") + "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("address1", "UTF-8") + "=" +
                        URLEncoder.encode(address1, "UTF-8") + "&" + URLEncoder.encode("address2", "UTF-8") + "=" +
                        URLEncoder.encode(address2, "UTF-8")+ "&" + URLEncoder.encode("usertype", "UTF-8") + "=" +
                        URLEncoder.encode(usertype, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("stores")){
            try{
                String extra = "";
                if (params.length > 1) {
                    extra = params[1];
                }

                URL url = new URL(stores_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                result += ":extra:" + extra;
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("stock")){
            try{
                URL url = new URL(stock_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("stockAdd")){
            try {
                String product_name = params[1];
                System.out.println(product_name);
                String price = params[2];
                System.out.println(price);
                String stock = params[3];
                System.out.println(stock);
                String productDescription = params[4];
                System.out.println(productDescription);
                String product_id = params[5];
                System.out.println(product_id);
                String pass_url = params[6];
                System.out.println(pass_url);
                URL url = new URL(stock_add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)));
                String post_data = URLEncoder.encode("product_name", "UTF-8") + "=" +
                        URLEncoder.encode(product_name, "UTF-8") + "&" + URLEncoder.encode("price", "UTF-8") + "=" +
                        URLEncoder.encode(price, "UTF-8") + "&" + URLEncoder.encode("stock", "UTF-8") + "=" +
                        URLEncoder.encode(stock, "UTF-8") + "&" + URLEncoder.encode("productDescription", "UTF-8") + "=" +
                        URLEncoder.encode(productDescription, "UTF-8")+ "&" + URLEncoder.encode("product_id", "UTF-8") + "=" +
                        URLEncoder.encode(product_id, "UTF-8")+ "&" + URLEncoder.encode("pass_url", "UTF-8") + "=" +
                        URLEncoder.encode(pass_url, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("storePromotion")){
            try{
                System.out.println("correct url");
                URL url = new URL(store_promotion_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("promotionAdd")){
            try {
                String productId = params[1];
                System.out.println(productId);
                String storeId = params[2];
                System.out.println(storeId);
                String promotionPrice = params[3];
                System.out.println(promotionPrice);
                String promotionDescription = params[4];
                System.out.println(promotionDescription);


                URL url = new URL(promotion_add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)));
                String post_data = URLEncoder.encode("product_id", "UTF-8") + "=" +
                        URLEncoder.encode(productId, "UTF-8") + "&" + URLEncoder.encode("store_id", "UTF-8") + "=" +
                        URLEncoder.encode(storeId, "UTF-8") + "&" + URLEncoder.encode("promotionPrice", "UTF-8") + "=" +
                        URLEncoder.encode(promotionPrice, "UTF-8") + "&" + URLEncoder.encode("promotionDescription", "UTF-8") + "=" +
                        URLEncoder.encode(promotionDescription, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type.equals("stock1")){
            try{
                URL url = new URL(stock_url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream, "ISO_8859_1"));
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println(result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return null;
    }

    /**
     * the method before we deal with back end data.
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     * After we receive the response from certain php file,
     * this method can send those information to correct activity and open this activity.
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        System.out.println("RESULT"+result);
        String[] resources = result.split("\\^");

        ArrayList<String> resourceArray = new ArrayList<>();

        for (String element: resources){
            resourceArray.add(element);
        }

        String tag = resourceArray.remove(0);


        System.out.println("result " + result);
        if (result.charAt(0) == '0' || result.charAt(0) =='2') {
            Intent intent = new Intent(context, ShoppingCentreList.class);
            intent.putExtra("PROFILE", result);
            intent.putExtra("parent", "customer");
            context.startActivity(intent);
            System.out.println("custoemr login success");
        } else if (result.charAt(0)=='1'){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("User register success. Please contact us through E-mail, sending your username and identity check, in order to activate the account");
            builder1.setCancelable(true);

            AlertDialog alert = builder1.create();
            alert.show();
        }  else if(result.charAt(0)=='3'){
            Intent intent = new Intent(context, StoreOwnerMenu.class);
            intent.putExtra("PROFILE", result);
            intent.putExtra("parent","store");
            context.startActivity(intent);
            System.out.println("store owner login success");

        } else if (result.equals("login not success")){
            Intent intent = new Intent(context, CustomerLogin.class);
            context.startActivity(intent);
            System.out.println("login not success");
        } else if (tag.equals("storelist")){
            Intent intent = new Intent(context, StoreListActivity.class);
            intent.putExtra("resource",result);
            intent.putExtra("FROM_ACTIVITY","menu");
            context.startActivity(intent);
            System.out.println("intent created");
        } else if (tag.equals("stocklist")){
            Intent intent = new Intent(context, StockListActivity.class);
            intent.putExtra("resource",result);
            intent.putExtra("FROM_ACTIVITY","menu");
            context.startActivity(intent);
            System.out.println("intent created");
        } else if (result.equals("store not success")){
            Intent intent = new Intent(context, ShoppingCentreList.class);
            intent.putExtra("PROFILE", result);
            context.startActivity(intent);
        } else if (result.equals("already exist")) {
            Intent intent = new Intent(context, CustomerLogin.class);
            context.startActivity(intent);
            System.out.println("username Already exist");
        } else if (tag.equals("stores")) {
            Intent intent = new Intent(context, NavActivity.class);
            intent.putExtra("stores",result);
            context.startActivity(intent);
        } else if (result.equals("stock add success")){
            System.out.println("stock add success");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Stock add success");
            builder1.setCancelable(true);

            AlertDialog alert = builder1.create();
            alert.show();
        } else if (result.equals("stock add fail")){
            System.out.println("stock add fail");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Stock add fail");
            builder1.setCancelable(true);

            AlertDialog alert = builder1.create();
            alert.show();
        } else if (tag.equals("storePromotion")) {
            Intent intent = new Intent(context, StorePromotionsActivity.class);
            intent.putExtra("resource",result);
            context.startActivity((intent));
        } else if (result.equals("promotion add success")){
            System.out.println("promotion add success");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Promotion add success");
            builder1.setCancelable(true);

            AlertDialog alert = builder1.create();
            alert.show();
            ((Activity)context).finish();
        } else if (result.equals("promotion add fail")){
            System.out.println("promotion add fail");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Promotion add fail");
            builder1.setCancelable(true);

            AlertDialog alert = builder1.create();
            alert.show();
        } else if (tag.equals("stocklist1")){
            Intent intent = new Intent(context, StockListActivity.class);
            intent.putExtra("resource",result);
            intent.putExtra("FROM_ACTIVITY","promotionAdd");
            context.startActivity(intent);
            System.out.println("intent created");
        }
     }

    /**
     * a default method
     * @param values
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
