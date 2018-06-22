package com.example.charan.customsegmentedbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.charan.customsegmentedbutton.Custom.OnOptionChangedListener;
import com.example.charan.customsegmentedbutton.Custom.SegmentedButton;

public class MainActivity extends AppCompatActivity {

    SegmentedButton segmentedButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        segmentedButton = findViewById(R.id.segmented_button);
        textView = findViewById(R.id.position_tv);

        segmentedButton.setOnOptionChangedListener(new OnOptionChangedListener() {
            @Override
            public void onChange() {
                int pos = segmentedButton.getPosition();
                textView.setText("Position: " + pos);
            }
        });
    }
}
