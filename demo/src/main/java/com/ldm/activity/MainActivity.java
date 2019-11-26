package com.ldm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ldm.exception.R;

/**
 * 第一个页面，有注册EventBus
 *
 * @author ldm
 * @description�?
 * @date Created by generallizhong on 2019/11/26.
 */
public class MainActivity extends Activity {
    private TextView tv;
    Button next_btn_;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//		tv.setText("信息");
        next_btn_ = (Button) findViewById(R.id.next_btn_);
        next_btn_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent st = new Intent(MainActivity.this, TwoActivty.class);
                startActivity(st);
            }
        });
    }


}
