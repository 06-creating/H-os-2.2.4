package com.harrisonos.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("HTC Mobile OS");
        text.setTextSize(24);
        setContentView(text);
    }
}
