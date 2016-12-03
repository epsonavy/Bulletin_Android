package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 11/22/16.
 */

public class SuccessMessageResponse extends Response {
        public boolean success;
        public String message;

        public void setSuccess(boolean success)
        {
            this.success = success;
        }
        public boolean getSuccess(){
            return this.success;
        }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

}
