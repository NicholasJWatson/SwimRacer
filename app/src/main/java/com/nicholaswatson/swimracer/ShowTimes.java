package com.nicholaswatson.swimracer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ShowTimes extends AppCompatActivity {

    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

    private ListView listView;
    SwimAppDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_times);

        dbHelper = new SwimAppDBHelper(this);

        final Cursor cursor = dbHelper.getAllSwimTimes();
        String [] columns = new String[] {
                //SwimAppDBHelper.SWIMTIME_COLUMN_ID,
                SwimAppDBHelper.SWIMTIME_COLUMN_STROKE,
                SwimAppDBHelper.SWIMTIME_COLUMN_TIME
        };
        int [] widgets = new int[] {
                //R.id.swimTimeID,
                R.id.swimTimeStroke

        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.swimtime_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) ShowTimes.this.listView.getItemAtPosition(position);
                int swimTimeID = itemCursor.getInt(itemCursor.getColumnIndex(SwimAppDBHelper.SWIMTIME_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), CreateOrEditSwimTime.class);
                intent.putExtra(KEY_EXTRA_CONTACT_ID, swimTimeID);
                startActivity(intent);
            }
        });

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
