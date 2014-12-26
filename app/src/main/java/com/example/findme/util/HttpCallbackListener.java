package com.example.findme.util;

/**
 * Created by Mark on 2014/12/23.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
