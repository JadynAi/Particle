package com.example.jadynai.particle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View mLizi1;
    private View mLizi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLizi1 = findViewById(R.id.lizi1);
        mLizi2 = findViewById(R.id.lizi2);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    public void click() {
        if (mLizi1.getVisibility() == View.VISIBLE) {
            mLizi1.setVisibility(View.GONE);
            mLizi2.setVisibility(View.VISIBLE);
        } else {
            mLizi1.setVisibility(View.VISIBLE);
            mLizi2.setVisibility(View.GONE);
        }
    }
}
