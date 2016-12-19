package com.example.diong.todo;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ItemDetails extends AppCompatActivity {
    //项目详情页面

    private TextView timer, contentView;
    int year, month, day, hour, minute, second, important, finish;
    private String con;
    long leftTime;
    private MyTimer myTimer;
    private myDB mydb;
    private ImageButton editB, deleteB, returnB;
    private MyService alarmservice = new MyService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
            Log.i("-----v-----", version+"");
            //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


        editB = (ImageButton)findViewById(R.id.edit_detail);
        deleteB = (ImageButton)findViewById(R.id.delete_detail);
        returnB = (ImageButton)findViewById(R.id.detail_goback);

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

        //通过传过来的项目内容，找到项目
        Bundle bundle = this.getIntent().getExtras();
        final String content = bundle.getString("ToDoItemContent");
        mydb = new myDB(getApplicationContext());
        TodoItem item = mydb.getEntry(content);

        //提取该项目的日期时间
        year = item.getToDoYear();
        month = item.getToDoMonth();
        day = item.getToDoDay();
        hour = item.getToDoHour();
        minute = item.getToDoMinute();
        second = 60;
        con = content;
        important = item.getImportant();
        finish = item.getFinish();


        returnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (Integer.valueOf(android.os.Build.VERSION.SDK)  > 2 ) {
                    overridePendingTransition(0, R.anim.activity_detail_out);
                }
            }
        });
        //事件绑定
        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                View view1 = LayoutInflater.from(ItemDetails.this).inflate(R.layout.change_dialog, null);
                //得到日期和时间
                final TimePicker time_picker = (TimePicker) view1.findViewById(R.id.time_picker2);
                final DatePicker date_picker = (DatePicker) view1.findViewById(R.id.date_picker2);
                final TextView new_item_content = (TextView) view1.findViewById(R.id.item_content);
                final RadioGroup importantGroup = (RadioGroup) view1.findViewById(R.id.important_group2);
                final RadioButton importantB =(RadioButton) view1.findViewById(R.id.important2);
                final RadioButton NimportantB =(RadioButton) view1.findViewById(R.id.notImprotant2);
                time_picker.setIs24HourView(true);

                new_item_content.setText(con);
                if (important == 1) {
                    importantB.setChecked(true);
                    NimportantB.setChecked(false);
                } else {
                    importantB.setChecked(false);
                    NimportantB.setChecked(true);
                }


                //判断是否重要
                importantGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.important){
                            important = 1;
                        } else {
                            important = 0;
                        }
                    }
                });

                Dialog add_item = new AlertDialog.Builder(context).
                        setTitle("修改备忘").
                        setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把数据添加到数据库中
                                final int year = date_picker.getYear();
                                final int month = date_picker.getMonth() + 1;
                                final int day = date_picker.getDayOfMonth();
                                final int hour = time_picker.getHour();
                                final int minute = time_picker.getMinute();
                                final String content = new_item_content.getText().toString();

                                TodoItem temp = null;
                                temp = mydb.getEntry(content);
                                Calendar c = Calendar.getInstance();
                                long tonow = c.getTimeInMillis();

                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month - 1);//也可以填数字，0-11,一月为0
                                c.set(Calendar.DAY_OF_MONTH, day);
                                c.set(Calendar.HOUR_OF_DAY, hour);
                                c.set(Calendar.MINUTE, minute);
                                c.set(Calendar.SECOND, 0);

                                if (tonow < c.getTimeInMillis()) {
                                    TodoItem newItem = new TodoItem(content, year, month, day, hour, minute, important, finish);
                                    Log.i("---NewItem-----", newItem.getToDoContent());
                                    alarmservice.deleteAlarm(content);
                                    mydb.updateEntry(newItem);
                                    alarmservice.addAlarm(content);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ToDoItemContent", newItem.getToDoContent());
                                    Intent intent = new Intent(ItemDetails.this, ItemDetails.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                 /*   if(Integer.valueOf(android.os.Build.VERSION.SDK) > 5) {
                                        overridePendingTransition(0, R.anim.activity_detail_out);
                                    }*/
                                    //更新列表
                                    // Context context = getApplicationContext();
                                } else {
                                    Toast.makeText(ItemDetails.this, "设定完成时间不能小于现在时间", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }).
                        setView(view1).
                        create();
                add_item.show();
            }
        });

        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog add_item = new AlertDialog.Builder(ItemDetails.this).
                        setTitle("确认删除?").
                        setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).
                        setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把数据添加到数据库中
                             /*   TodoItem newItem = new TodoItem(content, year, month, day, hour, minute, important, 3);
                                mydb.updateEntry(newItem);
                                finish();*/
                                alarmservice.deleteAlarm(content);
                                mydb.deleteEntry(content);
                                unbindService(sc);
                                sc = null;
                                finish();
                            }
                        }).
                        create();
                add_item.show();
            }
        });



        timer = (TextView) findViewById(R.id.timer);
        contentView = (TextView) findViewById(R.id.content);
        contentView.setText(con);

        try {
            leftTime = calculateRemainTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myTimer = new MyTimer(leftTime, 1000);
        Log.i("------", leftTime+"");
        myTimer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sc != null) {
            unbindService(sc);
        }
        myTimer.cancel();
       // overridePendingTransition(0, R.anim.activity_detail_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTimer.cancel();
    }

    private long calculateRemainTime() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Log.i("expDate", day+"");
        String date = year + "-" + month + "-" + day + " " + hour + ":" + minute;
        Date curDate = new Date(System.currentTimeMillis());
        Date expDate = dateFormat.parse(date);
        Log.i("expDate", expDate.toString());
        Log.i("curDate", curDate.toString());

        Long diff = expDate.getTime() - curDate.getTime();

        return diff;
    }

    public class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //TODO:稍微改一下文字（语文差（蹲
        @Override
        public void onFinish() {
            if (finish == 0) { //时间超过仍然未完成
                timer.setText("DDL已过，别拖延了快去做");
            } else {
                timer.setText("你已在规定时间内完成:)");
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leftTime -= 1000;

            long days = millisUntilFinished / (1000 * 60 * 60 * 24);
            long hours = (millisUntilFinished % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (millisUntilFinished % (1000 * 60)) / 1000;
            String time = days+"天"+hours+"小时"+minutes+"分"+seconds+"秒";

//            String formatType = "HH时mm分ss秒";
//            Date tempdate = new Date(millisUntilFinished);
//            SimpleDateFormat format = new SimpleDateFormat(formatType);
//            format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
//            String time = (tempdate.getYear()-70) + "年" + (tempdate.getMonth()) + "月" +
//                    (tempdate.getDay()-4) + "日"+format.format(millisUntilFinished);
//            Log.i("----tempdate.getday----", tempdate.getDay()+"");
//            Log.i("-------tempdate--------", tempdate.toString());
            if (finish == 0) {
                timer.setText("剩余时间:\n\n"+time);
            } else {
                timer.setText("已提前完成任务!");
            }
        }
    }

    private ServiceConnection sc = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName
                                                  name) {
            alarmservice = null;}
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            alarmservice =
                    ((MyService.MyBinder)(service)).getService();
            //绑定成功后
            }
    };

   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
           /* unbindService(sc);
            try {
                MainActivity.this.finish();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;*/
      /*      Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意，这个地方最重要，关于解释，自己google吧
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);
          //  finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
