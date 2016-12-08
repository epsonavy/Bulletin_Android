package com.cs175.bulletinandroid.bulletin;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

/**
 * Created by Lucky on 11/22/16.
 */

public class BulletinAPI {

    private String APIAddress;

    private String token;

    public String getAPIAddress(){
        return APIAddress;
    }

    public void setAPIAddress(String address){
        this.APIAddress = address;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public BulletinAPI(){

    }

    public void login(final OnRequestListener listener, final String email, final String password){
        new Thread(new Runnable(){
            public void run(){
                try{
                    Log.d("Bulletin API", "Logging in with " + email + " and password " + password);
                    URL url = new URL(getAPIAddress() + "/auth/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\"}");
                    Log.d("Bulletin API" , "Logging in with JSON: { \"email\" : \"" + email + "\", \"password\" : \"" + password + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageTokenResponse response = gson.fromJson(sb.toString(), SuccessMessageTokenResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Login, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Login, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with login " + e.getMessage());
                }
            }
        }).start();
    }
    public void register(final OnRequestListener listener, final String email, final String password, final String displayName){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/register/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    Log.d("Bulletin API", "Registering with " + "{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\", \"display_name\" : \"" + displayName + "\"}");
                    os.write("{ \"email\" : \"" + email + "\", \"password\" : \"" + password + "\", \"display_name\" : \"" + displayName + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Register, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.Register, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with register " + e.getMessage());
                }
            }
        }).start();
    }

    public void checkDisplayName(final OnRequestListener listener, final String displayName){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/register/check/name/?display_name=" + displayName);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");

                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckDisplayName, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckDisplayName, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking display name " + e.getMessage());
                }
            }
        }).start();
    }

    public void checkEmail(final OnRequestListener listener, final String email){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/register/check/email/?email=" + email);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckEmail, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckEmail, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking email " + e.getMessage());
                }
            }
        }).start();


    }

    public void checkToken(final OnRequestListener listener, final String token){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/auth/?token=" + token);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckToken, response);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.CheckToken, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with checking token " + e.getMessage());
                }
            }
        }).start();


    }

    public void getItems(final OnRequestListener listener){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/items/all/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ItemResponse[] responses = gson.fromJson(sb.toString(), ItemResponse[].class);

                        for(int i=0; i<responses.length; i++){
                            responses[i].setResponseCode(connection.getResponseCode());
                        }

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetItems, connection.getResponseCode(), responses);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.GetItems, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting items " + e.getMessage());
                }
            }
        }).start();


    }

    public void getMyItems(final OnRequestListener listener){

        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/items/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ItemResponse[] responses = gson.fromJson(sb.toString(), ItemResponse[].class);

                        for(int i=0; i<responses.length; i++){
                            responses[i].setResponseCode(connection.getResponseCode());
                        }

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetItems, connection.getResponseCode(), responses);


                    }else{
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.GetItems, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting items " + e.getMessage());
                }
            }
        }).start();


    }

    public void makeConversation(final OnRequestListener listener, final String itemId){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/conversations/new/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"itemId\" : \"" + itemId + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        ConversationResponse response = gson.fromJson(sb.toString(), ConversationResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.MakeConversation, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with making conversation " + e.getMessage());
                }
            }
        }).start();
    }


    public void getMyUserDetails(final OnRequestListener listener){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/users/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        UserResponse response = gson.fromJson(sb.toString(), UserResponse.class);
                        response.setResponseCode(connection.getResponseCode());

                        listener.onResponseReceived(OnRequestListener.RequestType.GetMyUserDetails, response);


                    }else{
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(403);
                        listener.onResponseReceived(OnRequestListener.RequestType.GetMyUserDetails, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting user details " + e.getMessage());
                }
            }
        }).start();

    }

    public void getConverations(final OnRequestListener listener){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/conversations/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        ConversationResponse[] response = gson.fromJson(sb.toString(), ConversationResponse[].class);

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetConversations, connection.getResponseCode(), response);


                    }else{
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(403);
                        listener.onResponseReceived(OnRequestListener.RequestType.GetConversations, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting conversations " + e.getMessage());
                }
            }
        }).start();
    }


    public void postItem(final OnRequestListener listener, final String title, final String description, final String picture, final Double price){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/items/new/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    if(!picture.equals("")) {
                        os.write("{ \"title\":" + title + ", \"description\": " + description + ", \"pictures\": [\"" + picture + "\"], \"price\":" + Double.toString(price) +"}");
                    }else{
                        os.write("{ \"title\":" + title + ", \"description\": " + description + ", \"price\":" + Double.toString(price) + "}");
                    }
                    Log.d("Bulletin API", "{ \"title\":" + title + ", \"description\": " + description + ", \"pictures\": [\"" + picture + "\"], \"price\":" + Double.toString(price) +"}");
                    Log.d("Bulletin API", "{ \"title\":" + title + ", \"description\": " + description + ", \"price\":" + Double.toString(price) + "}");

                    //{ "title" : title, "description" : description, "pictures": ["onepicture"], "price" : price}
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        ItemResponse response = gson.fromJson(sb.toString(), ItemResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.PostItem, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.PostItem, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with making conversation " + e.getMessage());
                }
            }
        }).start();
    }

    public void getAllMessages(final OnRequestListener listener, final String conversationId){
        //need from and conversation
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/conversations/messages/?token=" + getToken() + "&conversationId=" + conversationId + "&from=0");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-length", "0");
                    Log.d("Bulletin API", Integer.toString(connection.getResponseCode()));
                    BufferedReader br = null;
                    if(connection.getResponseCode() == 200) {
                        br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    }else {
                        br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    }
                    String readLine = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    if(connection.getResponseCode() == 200){
                        MessageResponse[] response = gson.fromJson(sb.toString(), MessageResponse[].class);

                        listener.onResponsesReceived(OnRequestListener.RequestType.GetAllMessages, connection.getResponseCode(), response);


                    }else{
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.GetAllMessages, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with getting conversations " + e.getMessage());
                }
            }
        }).start();

    }

    public void sendMessage(final OnRequestListener listener, final String conversationId, final String message){
        //need from and conversation
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/conversations/messages/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"conversationId\": " + "\"" +conversationId + "\", \"message\": \"" + message + "\"}");
                    Log.d("Bulletin API", "{ \"conversationId\": " + "\"" +conversationId + "\", \"message\": \"" + message + "\"}");

                    //{ "title" : title, "description" : description, "pictures": ["onepicture"], "price" : price}
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        MessageResponse response = gson.fromJson(sb.toString(), MessageResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.SendMessage, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.SendMessage, response);

                    }



                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with sending a message " + e.getMessage());
                }
            }
        }).start();

    }

    public void uploadImage(final OnRequestListener listener, final File image){
        new Thread(new Runnable(){
            public void run(){
                try {
                    URL url = new URL(getAPIAddress() + "/upload/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    String boundary = "===" + System.currentTimeMillis() + "===";
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + boundary);
                    //connection.setFixedLengthStreamingMode(4096);
                    PrintWriter os = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));




                    String fileName = image.getName();
                    os.append("--" + boundary).append("\r\n");
                    os.append(
                            "Content-Disposition: form-data; name=\"" + "\"file\""
                                    + "\"; filename=\"" + fileName + "\"")
                            .append("\r\n");
                    os.append(
                            "Content-Type: "
                                    + URLConnection.guessContentTypeFromName(fileName))
                            .append("\r\n");
                    os.append("Content-Transfer-Encoding: binary").append("\r\n");
                    os.append("\r\n");
                    os.flush();

                    FileInputStream inputStream = new FileInputStream(image);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                       os.write(new String(buffer).toCharArray(), 0, bytesRead);
                    }
                    os.flush();
                    os.append("\r\n");
                    os.flush();
                    os.write("--" +boundary + "--" + "\r\n");
                    inputStream.close();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        UploadResponse response = gson.fromJson(sb.toString(), UploadResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.UploadImage, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = new SuccessMessageResponse();
                        response.setResponseCode(400);
                        listener.onResponseReceived(OnRequestListener.RequestType.UploadImage, response);

                    }



                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with uploading an image " + e.getMessage());
                }
            }
        }).start();
    }

    public void updateItem(final OnRequestListener listener, final String itemId, final String title, final String description, final Double price, final String picture){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/items/update/?token=" + getToken() + "&itemId=" + itemId);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    if(!picture.equals("")) {
                        os.write("{ \"title\": \"" + title + "\", \"description\": \"" + description + "\", \"pictures\": [\"" + picture + "\"], \"price\":" + Double.toString(price) +"}");
                    }else{
                        os.write("{ \"title\": \"" + title + "\", \"description\": \"" + description + "\", \"price\":" + Double.toString(price) + "}");
                    }
                    Log.d("Bulletin API", "{ \"title\": \"" + title + "\", \"description\": \"" + description + "\", \"pictures\": [\"" + picture + "\"], \"price\":" + Double.toString(price) +"}");
                    Log.d("Bulletin API", "{ \"title\": \"" + title + "\", \"description\": \"" + description + "\", \"price\":" + Double.toString(price) + "}");

                    //{ "title" : title, "description" : description, "pictures": ["onepicture"], "price" : price}
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        ItemResponse response = gson.fromJson(sb.toString(), ItemResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdateItem, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdateItem, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with updating item " + e.getMessage());
                }
            }
        }).start();
    }

    public void updatePassword(final OnRequestListener listener, final String password){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/users/update/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                        os.write("{ \"password\": \"" + password + "\"}");
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        UserResponse response = gson.fromJson(sb.toString(), UserResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdatePassword, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdatePassword, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with updating user " + e.getMessage());
                }
            }
        }).start();
    }


    public void updatePicture(final OnRequestListener listener, final String picture){
        new Thread(new Runnable(){
            public void run(){
                try{
                    URL url = new URL(getAPIAddress() + "/users/update/?token=" + getToken());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/json");
                    OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
                    os.write("{ \"profile_picture\": \"" + picture + "\"}");
                    //{ "title" : title, "description" : description, "pictures": ["onepicture"], "price" : price}
                    os.flush();
                    os.close();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = new Gson();

                    int resCode = connection.getResponseCode();
                    if(resCode == 200){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        UserResponse response = gson.fromJson(sb.toString(), UserResponse.class);
                        response.setResponseCode(connection.getResponseCode());
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdatePicture, response);

                    }else{
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while((line = br.readLine()) != null){
                            sb.append(line);
                        }
                        SuccessMessageResponse response = gson.fromJson(sb.toString(), SuccessMessageResponse.class);
                        response.setResponseCode(resCode);
                        listener.onResponseReceived(OnRequestListener.RequestType.UpdatePicture, response);

                    }


                }catch(Exception e){
                    Log.d("Bulletin API", "Something went wrong with updating user " + e.getMessage());
                }
            }
        }).start();
    }


}
