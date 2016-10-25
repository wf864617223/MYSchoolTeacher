package com.rimi.schoolteacher.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Android on 2016/7/25.
 */
public class GsonUtils {
    public static Gson createGson(){
        return new GsonBuilder().create();
    }
}
