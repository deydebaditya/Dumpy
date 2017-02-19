package com.debashubham.dumpy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button simple_calc,chainage_calc;
    EditText work_name,surveyor_name;
    DatePicker date_of_survey;
    String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simple_calc=(Button)findViewById(R.id.simple_calc);
        chainage_calc=(Button)findViewById(R.id.chainage_calc);
        work_name=(EditText)findViewById(R.id.work_name);
        surveyor_name=(EditText)findViewById(R.id.surveyor_name);
        date_of_survey=(DatePicker)findViewById(R.id.datePicker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Calendar c = Calendar.getInstance();

        int maxYear = c.get(Calendar.YEAR);
        int maxMonth = c.get(Calendar.MONTH);
        int maxDay = c.get(Calendar.DAY_OF_MONTH);
        setSupportActionBar(toolbar);

        date=String.valueOf(date_of_survey.getDayOfMonth())+"/"+String.valueOf(date_of_survey.getMonth()+1)+"/"+String.valueOf(date_of_survey.getYear());

        date_of_survey.init(maxYear, maxMonth, maxDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date=String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(year);
            }
        });

        simple_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(work_name.getText().toString().equals("")||surveyor_name.getText().toString().equals("")){
                    if(work_name.getText().toString().equals(""))
                        work_name.setError("Enter the work name!");
                    else if(surveyor_name.getText().toString().equals(""))
                        surveyor_name.setError("Enter surveyor name!");
                    else{
                        work_name.setError("Enter the work name!");
                        surveyor_name.setError("Enter surveyor name!");
                    }

                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("introduction", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("work_name", work_name.getText().toString());
                    editor.putString("surveyor_name", surveyor_name.getText().toString());
                    editor.putString("date_of_survey", date);
                    Log.e("Date of survey:", date);
                    Log.e("Work Name:", work_name.getText().toString());
                    Log.e("Surveyor name:", surveyor_name.getText().toString());
                    editor.commit();
                    Intent mainActivityIntent = new Intent(MainActivity.this, SimpleCalcActivity.class);
                    startActivity(mainActivityIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        });

        chainage_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(work_name.getText().toString().equals("")||surveyor_name.getText().toString().equals("")){
                    if(work_name.getText().toString().equals(""))
                        work_name.setError("Enter the work name!");
                    else
                        surveyor_name.setError("Enter surveyor name!");
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("introduction", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("work_name", work_name.getText().toString());
                    editor.putString("surveyor_name", surveyor_name.getText().toString());
                    editor.putString("date_of_survey", date);
                    Log.e("Date of survey:", date);
                    Log.e("Work Name:", work_name.getText().toString());
                    Log.e("Surveyor name:", surveyor_name.getText().toString());
                    editor.commit();
                    Intent mainActivityIntent = new Intent(MainActivity.this, ChainageCalcActivity.class);
                    startActivity(mainActivityIntent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.show_pdfs) {
            Intent intent=new Intent(MainActivity.this,SavedPDFActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.exit){
            Toast.makeText(getApplicationContext(),"Exiting",Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
}
