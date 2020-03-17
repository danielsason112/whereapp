package com.afeka.whereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afeka.whereapp.logic.DataService;

public class AddGroupActivity extends AppCompatActivity {

    private EditText groupNameInput;
    private EditText groupDescriptionInput;

    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        groupNameInput = findViewById(R.id.group_name_input);
        groupDescriptionInput = findViewById(R.id.group_description_input);
        dataService = new DataService(this);

        findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupNameInput.getText().toString();
                String description = groupDescriptionInput.getText().toString();
                Location location = (Location)getIntent().getParcelableExtra(MainActivity.LOCATION_EXTRA);
                dataService.createGroup(name, description, location);
                finish();
            }
        });

        ((TextView)findViewById(R.id.currentLocation)).setText(getIntent().getParcelableExtra("location").toString());

    }




}
