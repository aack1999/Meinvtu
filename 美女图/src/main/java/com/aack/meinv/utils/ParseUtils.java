package com.aack.meinv.utils;

import com.google.gson.Gson;

public class ParseUtils {

	public static <T>T parseJson(String json,Class<T> c){
		Gson g=new Gson();
		return g.fromJson(json, c);
	}

    public static String toJson(Object c){
        Gson g=new Gson();
        return g.toJson(c);
    }
	
}
