package com.example.ipfinder;

import android.content.Intent;

public class IpItem {


    private int _imageResource;
    private String _text1;
    private String _text2;

    public IpItem(int imageResource, String text1, String text2){
        _imageResource = imageResource;
        _text1 = text1;
        _text2 = text2;
    }


    public int getImageResource(){
        return _imageResource;
    }
    public String getText1(){
        return _text1;
    }
    public String getText2(){
        return _text2;
    }
}
