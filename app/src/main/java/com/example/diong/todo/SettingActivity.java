package com.example.diong.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import android.view.Menu;

public class SettingActivity extends AppCompatActivity {
    private ToggleButton mTogBtn;
    private EditText alarmtime;
    private ImageButton goback;
    private int alarmTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (Integer.valueOf(android.os.Build.VERSION.SDK)  > 2 ) {
                    overridePendingTransition(0, R.anim.setting_out);
                }
            }
        });
        mTogBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    // 开启
                    alarmTime = Integer.parseInt(alarmtime.getText().toString());
                }else{
                    // 关闭
                    alarmTime = 999999999;
                    alarmtime.setText("");
                }
            }
        });

    }
    private void initView() {
        mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
        goback = (ImageButton)findViewById(R.id.setting_goback);
        alarmtime = (EditText)findViewById(R.id.alarmTime);
    }
}
