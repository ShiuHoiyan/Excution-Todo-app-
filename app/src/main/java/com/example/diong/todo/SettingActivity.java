package com.example.diong.todo;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    private ImageButton goback;
    private myDB mydb;
    private ListView listView;
    private RadioGroup theme_picker;
    private SharedPreferences themePreferences;
    private SharedPreferences.Editor themeEditor;

  /*  private SharedPreferences set0 = getSharedPreferences("listfrom", Context.MODE_PRIVATE); //私有数据
    private SharedPreferences set1 = getSharedPreferences("defaultdelete", Context.MODE_PRIVATE); //私有数据
    private SharedPreferences set2 = getSharedPreferences("bg", Context.MODE_PRIVATE); //私有数据
*/
    boolean alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        final SharedPreferences sharedPreferences = getSharedPreferences("AlarmTF", Context.MODE_PRIVATE);
        themePreferences = getSharedPreferences("theme", Context.MODE_PRIVATE);
        themeEditor = themePreferences.edit();
        RadioButton greenB = (RadioButton)findViewById(R.id.green);
        RadioButton pinkB = (RadioButton)findViewById(R.id.pink);
        RadioButton greyB = (RadioButton)findViewById(R.id.grey);

        String theme_1 = themePreferences.getString("theme", "green");
        switch (theme_1) {
            case "green":
                greenB.setChecked(true);break;
            case "pink":
                pinkB.setChecked(true);break;
            case "grey":
                greyB.setChecked(true);break;
        }


        mydb = new myDB(getApplicationContext());

        reflesh();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (Integer.valueOf(android.os.Build.VERSION.SDK)  > 2 ) {
                    overridePendingTransition(0, R.anim.setting_out);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Context context = view.getContext();
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.set0dialog, null);

                    RadioGroup set0 = (RadioGroup)v.findViewById(R.id.setfrom1);
                    final RadioButton set0impor = (RadioButton)v.findViewById(R.id.set0important);
                    final RadioButton set0time = (RadioButton)v.findViewById(R.id.set0time);
                    final SharedPreferences setform = getSharedPreferences("listfrom", Context.MODE_PRIVATE);
                    String form = setform.getString("form", "important");

                    if (form.equals("important")) {
                        set0impor.setChecked(true);
                    } else {
                        set0time.setChecked(true);
                    }

                    final Dialog dialog = new AlertDialog.Builder(context).
                            setView(v).
                            create();


                    set0impor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor= setform.edit();
                            editor.putString("form", "important");
                            editor.commit();
                            set0impor.setChecked(true);
                            dialog.dismiss();
                            reflesh();
                        }
                    });

                    set0time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor= setform.edit();
                            editor.putString("form", "time");
                            editor.commit();
                            set0time.setChecked(true);
                            dialog.dismiss();
                            reflesh();
                        }
                    });
                    dialog.show();

                } else if (i == 1) {
                    Context context = view.getContext();
                    View v = LayoutInflater.from(SettingActivity.this).inflate(R.layout.set1layout, null);

                    RadioGroup set1 = (RadioGroup)v.findViewById(R.id.set1from);
                    final RadioButton set1yes = (RadioButton)v.findViewById(R.id.set1yes);
                    final RadioButton set1no = (RadioButton)v.findViewById(R.id.set1no);
                    final SharedPreferences dDelete = getSharedPreferences("defaultdelete", Context.MODE_PRIVATE);
                    String form = dDelete.getString("default", "NO");

                    if (form.equals("YES")) {
                        set1yes.setChecked(true);
                    } else {
                        set1no.setChecked(true);
                    }

                    final Dialog dialog = new AlertDialog.Builder(context).
                            setView(v).
                            create();


                    set1yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor= dDelete.edit();
                            editor.putString("default", "YES");
                            editor.commit();
                            dialog.dismiss();
                            reflesh();
                        }
                    });

                    set1no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor= dDelete.edit();
                            editor.putString("default", "NO");
                            editor.commit();
                            dialog.dismiss();
                            reflesh();
                        }
                    });
                    dialog.show();
                } else if (i == 2) {

                }
            }
        });

        theme_picker.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.green:
                        themeEditor.putString("theme", "green");break;
                    case R.id.pink:
                        themeEditor.putString("theme", "pink");break;
                    case R.id.grey:
                        themeEditor.putString("theme", "grey");break;
                }
                Log.i("====theme===", checkedId+"");
                themeEditor.commit();
            }
        });

    }

    private void changeTheme(String color) {
        int unim_selector, im_selector;
        switch (color) {
            case "green":
                unim_selector = R.drawable.checkbox_unim_selector_green;
                im_selector = R.drawable.checkbox_im_selector_green;
            case "pink":
                unim_selector = R.drawable.checkbox_unim_selector_pink;
                im_selector = R.drawable.checkbox_im_selector_pink;
            case "grey":
                unim_selector = R.drawable.checkbox_unim_selector_grey;
                im_selector = R.drawable.checkbox_im_selector_grey;
        }
        View view1 = LayoutInflater.from(SettingActivity.this).inflate(R.layout.item,
                        null);
    }

    private void initView() {
        goback = (ImageButton)findViewById(R.id.setting_goback);
        listView = (ListView)findViewById(R.id.settingList);
        theme_picker = (RadioGroup)findViewById(R.id.theme_picker);

    }

    private List<Map<String, Object>> getData() {
        String[] set = {"排序方式", "自动删除已完成项目"};
       List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < set.length; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("settinglist_text", set[i]);
            if (i == 0) {
                SharedPreferences setform = getSharedPreferences("listfrom", Context.MODE_PRIVATE);
                String form = setform.getString("form", "important");
                if (form.equals("important")) {
                    map1.put("setting_state", "重要性");
                } else {
                    map1.put("setting_state", "时间");
                }
            }
            if (i == 1) {
                SharedPreferences dDelete = getSharedPreferences("defaultdelete", Context.MODE_PRIVATE);
                String form = dDelete.getString("default", "NO");
                if (form.equals("NO")) {
                    map1.put("setting_state", "关闭");
                } else {
                    map1.put("setting_state", "开启");
                }
            }
            list.add(map1);
        }
        return list;
    }

 /*   private ServiceConnection sc = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName
                                                  name) {
            alarmservice = null;}
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            alarmservice =
                    ((MyService.MyBinder)(service)).getService();
            //绑定成功后

            }
    };*/

    private void reflesh() {
        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                R.layout.settinglistlayout, new String[] {"settinglist_text", "setting_state" },
                new int[] { R.id.settinglist_text, R.id.settinglist_state });
        listView.setAdapter(adapter);
    }

}
