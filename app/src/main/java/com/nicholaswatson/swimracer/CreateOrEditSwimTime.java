package com.nicholaswatson.swimracer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CreateOrEditSwimTime extends AppCompatActivity implements View.OnClickListener {
    private SwimAppDBHelper dbHelper;
    EditText strokeEditText;
    EditText timeEditText;
    EditText yardsEditText;

    Button saveButton;
    LinearLayout buttonLayout;
    Button editButton, deleteButton;

    int swimTimeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_swim_time);

        swimTimeID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_CONTACT_ID, 0);

        setContentView(R.layout.activity_create_or_edit_swim_time);
        strokeEditText = (EditText) findViewById(R.id.editTextStroke);
        timeEditText = (EditText) findViewById(R.id.editTextTime);
        yardsEditText = (EditText) findViewById(R.id.editTextYards);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        editButton = (Button) findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        dbHelper = new SwimAppDBHelper(this);

        if(swimTimeID > 0) {
            saveButton.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.VISIBLE);

            Cursor rs = dbHelper.getSwimTime(swimTimeID);
            rs.moveToFirst();
            String swimTimeStroke = rs.getString(rs.getColumnIndex(SwimAppDBHelper.SWIMTIME_COLUMN_STROKE));
            String swimTimeTime = rs.getString(rs.getColumnIndex(SwimAppDBHelper.SWIMTIME_COLUMN_TIME));
            int swimTimeYards = rs.getInt(rs.getColumnIndex(SwimAppDBHelper.SWIMTIME_COLUMN_YARDS));
            if (!rs.isClosed()) {
                rs.close();
            }

            strokeEditText.setText(swimTimeStroke);
            strokeEditText.setFocusable(false);
            strokeEditText.setClickable(false);

            timeEditText.setText(swimTimeTime);
            timeEditText.setFocusable(false);
            timeEditText.setClickable(false);

            yardsEditText.setText(swimTimeYards + "");
            yardsEditText.setFocusable(false);
            yardsEditText.setClickable(false);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveButton:
                persistPerson();
                return;
            case R.id.editButton:
                saveButton.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.GONE);
                strokeEditText.setEnabled(true);
                strokeEditText.setFocusableInTouchMode(true);
                strokeEditText.setClickable(true);

                timeEditText.setEnabled(true);
                timeEditText.setFocusableInTouchMode(true);
                timeEditText.setClickable(true);

                yardsEditText.setEnabled(true);
                yardsEditText.setFocusableInTouchMode(true);
                yardsEditText.setClickable(true);
                return;
            case R.id.deleteButton:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteSwimTime)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteSwimTime(swimTimeID);
                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                AlertDialog d = builder.create();
                d.setTitle("Delete Swim Time?");
                d.show();
                //return;
        }
    }

    public void persistPerson() {
        if(swimTimeID > 0) {
            if(dbHelper.changeSwimTime(swimTimeID, strokeEditText.getText().toString(),
                    timeEditText.getText().toString(),
                    Integer.parseInt(yardsEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if(dbHelper.newSwimTime(strokeEditText.getText().toString(),
                    timeEditText.getText().toString(),
                    Integer.parseInt(yardsEditText.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Time added", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not add time", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
