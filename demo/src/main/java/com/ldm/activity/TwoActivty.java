package com.ldm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.ldm.exception.R;

/**
 * Created by generallizhong on 2019/11/26.
 */

public class TwoActivty extends Activity {
    Button btn;
    String bg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two);
        btn= (Button) findViewById(R.id.next_btn);
        btn.setText("异常按钮");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bg.equals("123")){

                }
            }
        });
    }
}
