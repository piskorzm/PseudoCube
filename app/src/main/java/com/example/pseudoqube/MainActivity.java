package com.example.pseudoqube;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.pseudoqube.views.PseudoCube;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        PseudoCube cube = new PseudoCube(this);

        Button sort = findViewById(R.id.sort);
        Button randomize = findViewById(R.id.randomize);

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PseudoCube tempCube = findViewById(R.id.cube);
                tempCube.init(null);
            }
        });

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PseudoCube tempCube = findViewById(R.id.cube);
                tempCube.randomize();
            }
        });


    }

}
