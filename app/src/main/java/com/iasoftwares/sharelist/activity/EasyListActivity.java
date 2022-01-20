package com.iasoftwares.sharelist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iasoftwares.sharelist.R;

public class EasyListActivity extends AppCompatActivity {
    private Button bntBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_list);
        bntBack = findViewById(R.id.btnBackID);

        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EasyListActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}