package com.afeka.whereapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afeka.whereapp.dao.GroupDao;
import com.afeka.whereapp.dao.firebase.FirebaseGroupDao;
import com.afeka.whereapp.data.Group;
import com.afeka.whereapp.data.IdGenerator;
import com.afeka.whereapp.data.util.EntityFactory;

import java.util.UUID;

public class AddGroupActivity extends AppCompatActivity {

    private GroupDao groupDao;
    private EditText groupNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        groupDao = new FirebaseGroupDao();
        groupNameInput = findViewById(R.id.group_name_input);

        findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = groupNameInput.getText().toString();
                EntityFactory factory = new EntityFactory();
                Location location = (Location)getIntent().getParcelableExtra("location");
                Group created = groupDao.create(factory.createNewGroup(null, name, location.getLatitude(), location.getLongitude()));
                Toast toast = Toast.makeText(v.getContext(), "Group Created", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        ((TextView)findViewById(R.id.currentLocation)).setText(getIntent().getParcelableExtra("location").toString());

    }




}
