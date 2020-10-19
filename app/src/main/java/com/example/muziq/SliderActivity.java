package com.example.muziq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class SliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        SlidingUpPanelLayout layout = findViewById(R.id.slidingUp);

        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                findViewById(R.id.textView).setAlpha(1-slideOffset);

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState==SlidingUpPanelLayout.PanelState.EXPANDED)
                    Toast.makeText(SliderActivity.this,"Panel Expanded",Toast.LENGTH_SHORT).show();
                else if(newState==SlidingUpPanelLayout.PanelState.COLLAPSED)
                    Toast.makeText(SliderActivity.this,"Panel Collapsed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}