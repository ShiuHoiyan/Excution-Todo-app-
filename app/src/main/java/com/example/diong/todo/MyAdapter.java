package com.example.diong.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

/**
 * Created by wangyunwen on 16/12/4.
 */
public class MyAdapter extends BaseAdapter {
    private List<TodoItem> list;
    private Context context;
    private myDB db;
    private SharedPreferences themePreferences;

    public MyAdapter(Context context, List<TodoItem> list) {
        this.context = context;
        this.list = list;
        db = new myDB(context);
    }
    @Override
    public int getCount() {
        if(list == null)
            return 0;
        else
            return list.size();
    }

    public void setList(List<TodoItem> temp) {
        list = temp;
    }

    @Override
    public TodoItem getItem(int i) {
        if(list == null)
            return null;
        else
            return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;
        final int ii = i;
        themePreferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);

        int unim_selector, im_selector;
        String color;
        if (themePreferences.getString("theme", null)==null) {
            color = "green";
        } else {
            color = themePreferences.getString("theme", null);
        }
        switch (color) {
            case "green":
                unim_selector = R.drawable.checkbox_unim_selector_green;
                im_selector = R.drawable.checkbox_im_selector_green;
                break;
            case "pink":
                unim_selector = R.drawable.checkbox_unim_selector_pink;
                im_selector = R.drawable.checkbox_im_selector_pink;
                break;
            case "grey":
                unim_selector = R.drawable.checkbox_unim_selector_grey;
                im_selector = R.drawable.checkbox_im_selector_grey;
                break;
            default:
                unim_selector = R.drawable.checkbox_unim_selector_green;
                im_selector = R.drawable.checkbox_im_selector_green;
                break;
        }

        if(view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.todo = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.todo.setText(list.get(i).getToDoContent());


        if (list.get(i).getImportant() == 1) {
            viewHolder.todo.setButtonDrawable(im_selector);
        }
        else {
            viewHolder.todo.setButtonDrawable(unim_selector);
        }
        if (list.get(i).getFinish() == 1) {
            viewHolder.todo.setChecked(true);
        }

        viewHolder.todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) list.get(ii).setFinish();
                else list.get(ii).cancelFinish();
                TodoItem temp = new TodoItem(list.get(ii).getToDoContent(), list.get(ii).getToDoYear(),
                        list.get(ii).getToDoMonth(), list.get(ii).getToDoDay(), list.get(ii).getToDoHour(),
                        list.get(ii).getToDoMinute(), list.get(ii).getImportant(), list.get(ii).getFinish(), list.get(ii).getToDoAlarmOP(), list.get(ii).getBeforeTime());
                db.updateEntry(temp);
            }
        });

        return convertView;
    }
    private class ViewHolder {
        public CheckBox todo;
    }
}
