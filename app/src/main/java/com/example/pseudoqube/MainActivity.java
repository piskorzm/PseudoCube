package com.example.pseudoqube;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.pseudoqube.views.PseudoCube;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        PseudoCube cube = new PseudoCube(this);
    }
}
