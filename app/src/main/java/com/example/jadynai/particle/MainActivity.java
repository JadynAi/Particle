package com.example.jadynai.particle;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.jadynai.particle.view.FirewormsView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private FirewormsView mFirwWormsView;
    private SeekBar mSeekBarScale;
    private SeekBar mSeekBarRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirwWormsView = (FirewormsView) findViewById(R.id.fire_worms_view);

        mSeekBarScale = (SeekBar) findViewById(R.id.seekbar_1);
        mSeekBarRotate = (SeekBar) findViewById(R.id.seekbar_2);


        findViewById(R.id.set_src_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirwWormsView.setParticleSrcID(R.drawable.doge);
            }
        });

        findViewById(R.id.set_fish_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirwWormsView.setParticleSrcID(R.drawable.fish_master);
            }
        });

        mSeekBarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "mSeekBarScale: " + progress);
                int realProgress = seekBar.getMax() - progress;
                float ratio = Float.intBitsToFloat(realProgress) / Float.intBitsToFloat(seekBar.getMax());
                ViewCompat.setScaleX(mFirwWormsView, ratio);
                ViewCompat.setScaleY(mFirwWormsView, ratio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "mSeekBarRotate: " + progress);
                ViewCompat.setRotation(mFirwWormsView, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
