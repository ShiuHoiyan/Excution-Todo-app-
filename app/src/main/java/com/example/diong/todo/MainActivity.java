package com.example.diong.todo;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView todoList;
    private MyAdapter myAdapter;
    private Button addButton, settingB;
    private List<TodoItem> list = new ArrayList<TodoItem>();
    private myDB mydb;
    private bzDB bzdb;
    private MyService alarmservice = new MyService();
    private String lastClick = "";
    private SharedPreferences themePreferences;
    final private String widgetUp = "android.appwidget.action.UpdateWidgetText";

    //
    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;
    private static final int UPTATE_INTERVAL_TIME = 50;
    private static final int SPEED_SHRESHOLD = 30;//这个值调节灵敏度
    private long lastUpdateTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    private int important = 0;
    private int finish = 0;
    private String alarmOp = "ON";
    private int beforeTime = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        //获取所有数据并显示出来
        mydb = new myDB(getApplicationContext());
        bzdb = new bzDB(getApplicationContext());
      //  mydb.onCreateDelete();
        //list = mydb.getAllItems();
        refleshList();

        //设置
        myAdapter = new MyAdapter(this, list);
        todoList.setAdapter(myAdapter);
        sendWidgetBocast();
        //获得service
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

        //传感器部分
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 设置主题和颜色
        themePreferences = getSharedPreferences("theme", Context.MODE_PRIVATE);
        String color;
        if (themePreferences.getString("theme", null)==null) {
            color = "green";
        } else {
            color = themePreferences.getString("theme", null);
        }
        switch (color) {
            case "green":
                setTheme(R.style.AppThemeGreen);
                break;
            case "pink":
                setTheme(R.style.AppThemePink);
                break;
            case "grey":
                setTheme(R.style.AppThemeGrey);
                break;
            default:
                setTheme(R.style.AppThemeGreen);
                break;
        }

        //单击列表,跳转到选中的项目详情页面
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TodoItem temp = myAdapter.getItem(position);
                lastClick = temp.getToDoContent();

                Bundle bundle = new Bundle();
                bundle.putString("ToDoItemContent",temp.getToDoContent());
                Intent intent = new Intent(MainActivity.this, ItemDetails.class);
                intent.putExtras(bundle);
                startActivity(intent);
                if (Integer.valueOf(android.os.Build.VERSION.SDK)  > 2 ) {
                    overridePendingTransition(R.anim.activity_detail, 0);
                }
            }
        });

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String content = myAdapter.getItem(i).getToDoContent();
                Dialog add_item = new AlertDialog.Builder(MainActivity.this).
                        setTitle("你真的已经完成了吗？").
                        setNegativeButton("被你发现了，还没有", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).
                        setPositiveButton("真的做完了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把数据添加到数据库中
                                alarmservice.deleteAlarm(content);
                                mydb.deleteEntry(content);
                                bzdb.deleteBZ(content);
                                //list = mydb.getAllItems();
                                refleshList();
                               // Context context = getApplicationContext();
                                myAdapter.setList(list);
                                todoList.setAdapter(myAdapter);
                                sendWidgetBocast();

                            }
                        }).
                        create();
                add_item.show();
                return true;
            }
        });
        //添加新的项目
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_dialog, null);
                //得到日期和时间
                final TimePicker time_picker = (TimePicker) view1.findViewById(R.id.time_picker);
                final DatePicker date_picker = (DatePicker) view1.findViewById(R.id.date_picker);
                final EditText new_item_content = (EditText) view1.findViewById(R.id.new_item);
                final RadioGroup importantGroup = (RadioGroup) view1.findViewById(R.id.important_group);
                final RadioGroup alarmOpGroup = (RadioGroup)view1.findViewById(R.id.alarmGroup);
                final EditText beforeTime = (EditText)view1.findViewById(R.id.beforeTime);
                time_picker.setIs24HourView(true);

                //设置颜色
                int unim_selector;
                String color;
                if (themePreferences.getString("theme", null)==null) {
                    color = "green";
                } else {
                    color = themePreferences.getString("theme", null);
                }
                switch (color) {
                    case "green":
                        unim_selector = R.drawable.checkbox_unim_selector_green;
                        setTheme(R.style.AppThemeGreen);
                        break;
                    case "pink":
                        unim_selector = R.drawable.checkbox_unim_selector_pink;
                        setTheme(R.style.AppThemePink);
                        break;
                    case "grey":
                        unim_selector = R.drawable.checkbox_unim_selector_grey;
                        setTheme(R.style.AppThemeGrey);
                        break;
                    default:
                        unim_selector = R.drawable.checkbox_unim_selector_green;
                        setTheme(R.style.AppThemeGreen);
                        break;
                }
                RadioButton a, b, c, d;
                a = (RadioButton) view1.findViewById(R.id.important);
                b = (RadioButton) view1.findViewById(R.id.notImprotant);
                c = (RadioButton) view1.findViewById(R.id.alarmClose);
                d = (RadioButton) view1.findViewById(R.id.alarmOpen);
                a.setButtonDrawable(unim_selector);
                b.setButtonDrawable(unim_selector);
                c.setButtonDrawable(unim_selector);
                d.setButtonDrawable(unim_selector);

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

                alarmOpGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.alarmOpen){
                            alarmOp = "ON";
                        } else {
                            alarmOp = "OFF";
                        }
                    }
                });

                Dialog add_item = new AlertDialog.Builder(context).
                        setTitle("增加新备忘").
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
                                final int before = Integer.valueOf(beforeTime.getText().toString());
                                if (!content.isEmpty() ) {
                                    TodoItem temp = null;
                                    temp = mydb.getEntry(content);
                                    if (temp != null) {
                                        Toast.makeText(MainActivity.this, "事件已存在！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Calendar c = Calendar.getInstance();
                                        long tonow = c.getTimeInMillis();

                                        c.set(Calendar.YEAR, year);
                                        c.set(Calendar.MONTH, month-1);//也可以填数字，0-11,一月为0
                                        c.set(Calendar.DAY_OF_MONTH, day);
                                        c.set(Calendar.HOUR_OF_DAY, hour);
                                        c.set(Calendar.MINUTE, minute);
                                        c.set(Calendar.SECOND, 0);

                                        if (tonow < c.getTimeInMillis()) {
                                            TodoItem newItem = new TodoItem(content, year, month, day, hour, minute, important, finish, alarmOp, before);
                                            Log.i("---NewItem-----", newItem.getToDoContent());
                                            mydb.insertEntry(newItem);
                                            bzdb.insertBZ(content, "");
                                            //更新列表
                                            refleshList();
                                           // Context context = getApplicationContext();
                                            myAdapter.setList(list);
                                            todoList.setAdapter(myAdapter);
                                            sendWidgetBocast();
                                            alarmservice.addAlarm(content);
                                        } else {
                                            Toast.makeText(MainActivity.this, "设定完成时间不能小于现在时间", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "请输入事件", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).
                        setView(view1).
                        create();
                add_item.show();
            }
        });

        settingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                if (Integer.valueOf(android.os.Build.VERSION.SDK)  > 2 ) {
                    overridePendingTransition(R.anim.setting_in, 0);
                }
            }
        });

    }

    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (sensor != null && sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);//这里选择感应频率
        }


        refleshList();
        myAdapter.setList(list);
        todoList.setAdapter(myAdapter);
        sendWidgetBocast();


    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(sc);
    }
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            long currentUpdateTime = System.currentTimeMillis();
            long timeInterval = currentUpdateTime - lastUpdateTime;
            if (timeInterval < UPTATE_INTERVAL_TIME) {
                return;
            }
            lastUpdateTime = currentUpdateTime;
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;
            lastX = x;
            lastY = y;
            lastZ = z;
            double speed = (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval) * 100;
            if (speed >= SPEED_SHRESHOLD) {
                //vibrator.vibrate(300);
                refleshList();
                myAdapter.setList(list);
                todoList.setAdapter(myAdapter);
                //image.setImageResource(R.drawable.running01);
                Log.i("----", "shake");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    public void getViews(){
        todoList = (ListView) findViewById(R.id.todoList);
        addButton = (Button) findViewById(R.id.add);
        settingB = (Button)findViewById(R.id.toSetting);
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
            SharedPreferences sharedPreferences = getSharedPreferences("AlarmTF", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("alarmOrNot", true))  addAllAlarm();
        }
    };

    private void addAllAlarm() {
        for(int i = 0; i < list.size(); i++) {
            alarmservice.addAlarm(list.get(i).getToDoContent());
        }
    }


    private void refleshList() {
        SharedPreferences dDelete = getSharedPreferences("defaultdelete", Context.MODE_PRIVATE);
        String form1 = dDelete.getString("default", "NO");
        if (form1.equals("YES")) {
            mydb.deleteFinish();
        }
        SharedPreferences setform = getSharedPreferences("listfrom", Context.MODE_PRIVATE);
        String form = setform.getString("form", "important");
        if (form.equals("important")) {
            list = mydb.getImportantList();
            list.addAll(mydb.getNotImportantList());
            list.addAll(mydb.getFinishList());
        } else {
            list = mydb.getAllItems();
            Calendar c = Calendar.getInstance();
            long now = c.getTimeInMillis();
            for (int i = 0; i < list.size(); i++) {
                c.set(Calendar.YEAR, list.get(i).getToDoYear());
                c.set(Calendar.MONTH, list.get(i).getToDoMonth() - 1);//也可以填数字，0-11,一月为0
                c.set(Calendar.DAY_OF_MONTH, list.get(i).getToDoDay());
                c.set(Calendar.HOUR_OF_DAY, list.get(i).getToDoHour());
                c.set(Calendar.MINUTE, list.get(i).getToDoMinute());
                c.set(Calendar.SECOND, 0);
                long itemp = now - c.getTimeInMillis();
                for (int t = i; t < list.size(); t++) {
                    //从数据库取数据时间
                    c.set(Calendar.YEAR, list.get(t).getToDoYear());
                    c.set(Calendar.MONTH, list.get(t).getToDoMonth() - 1);//也可以填数字，0-11,一月为0
                    c.set(Calendar.DAY_OF_MONTH, list.get(t).getToDoDay());
                    c.set(Calendar.HOUR_OF_DAY, list.get(t).getToDoHour());
                    c.set(Calendar.MINUTE, list.get(t).getToDoMinute());
                    c.set(Calendar.SECOND, 0);
                    long ttemp = now - c.getTimeInMillis();
                    //TODO：改了这个垃圾冒泡
                    if (itemp <= ttemp) {
                        TodoItem temp = list.get(i);
                        list.set(i,list.get(t));
                        list.set(t, temp);
                    }
                }
            }
        }
    }

    private void sendWidgetBocast() {
        Intent intent = new Intent();
        intent.setAction(widgetUp);
// if have something to send, use Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("KEY", myAdapter.getCount());
//不用Bundle
        intent.putExtras(bundle);
        //intent.putExtra("age", 55);
        sendBroadcast(intent);
    }
}
