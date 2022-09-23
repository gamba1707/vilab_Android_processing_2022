package com.example.omata.vilab_processing;

import android.os.Bundle;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

import processing.android.PFragment;
import processing.android.CompatUtils;
import processing.core.PApplet;

public class MainActivity extends AppCompatActivity {
    //スケッチのインスタンス変数
    private PApplet sketch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //ここでスケッチを生成する
        sketch = new Sketch();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (sketch != null) {
            sketch.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }
}