package com.zuqiuyu.testtext;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ListView DATA;
    private static final String filename = "data.txt";
    private String str[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        DATA = (ListView) findViewById(R.id.DATA);
        final FileHelper helper = new FileHelper(getApplicationContext());
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,getData());
        DATA.setAdapter(adapter);
    }
    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        return data;
    }
}
