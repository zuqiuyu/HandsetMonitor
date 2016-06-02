package com.zuqiuyu.testtext;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    private EditText COUNT;
    private EditText TIME;
    private EditText SPO2;
    private EditText PR;
    private String data;
    private static final String filename = "data.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        COUNT = (EditText) findViewById(R.id.COUNT);
        TIME = (EditText) findViewById(R.id.TIME);
        SPO2 = (EditText) findViewById(R.id.SPO2);
        PR = (EditText) findViewById(R.id.PR);
        final FileHelper helper = new FileHelper(getApplicationContext());
        try {
            helper.createSDFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        findViewById(R.id.SUBMIT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = COUNT.getText().toString()+"\t"+TIME.getText().toString()+"\t"+SPO2.getText().toString()+"\t"+PR.getText().toString()+"\t\n";
                helper.writeSDFile(data,filename);
            }
        });
        findViewById(R.id.RECORD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RecordActivity.class);
                startActivity(intent);
            }
        });
    }
}