package com.debashubham.dumpy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

/**
 * Created by deba on 12/2/17.
 */
public class SimpleCalcActivity extends AppCompatActivity {

    ImageView back_button;
    EditText bs,rl,is,fs;
    TextView hi;
    Button bs_input,rl_input,is_input,fs_input,clear_button;
    LinkedList<Double> bs_list;
    LinkedList<Double> rl_list;
    LinkedList<Double> is_list;
    LinkedList<Double> fs_list;
    LinkedList<Double> hi_list;
    FloatingActionButton accept;
    int c=0,counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_calc_acitivity);
        back_button=(ImageView)findViewById(R.id.back_button_simple_calc);

        bs=(EditText)findViewById(R.id.BS);
        rl=(EditText)findViewById(R.id.RL);
        is=(EditText)findViewById(R.id.IS);
        fs=(EditText)findViewById(R.id.FS);

        hi=(TextView)findViewById(R.id.hitext);

        bs_input=(Button)findViewById(R.id.button_BS);
        rl_input=(Button)findViewById(R.id.button_RL);
        is_input=(Button)findViewById(R.id.button_IS);
        fs_input=(Button)findViewById(R.id.button_FS);
        clear_button=(Button)findViewById(R.id.clear_button);

        accept=(FloatingActionButton)findViewById(R.id.fab_accept_simple_values);

        rl.setEnabled(false);
        is.setEnabled(false);
        fs.setEnabled(false);
        rl_input.setEnabled(false);
        is_input.setEnabled(false);
        fs_input.setEnabled(false);

        bs_list=new LinkedList<>();
        rl_list=new LinkedList<>();
        is_list=new LinkedList<>();
        fs_list=new LinkedList<>();
        hi_list=new LinkedList<>();

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bs_list.clear();
                rl_list.clear();
                is_list.clear();
                fs_list.clear();
                hi_list.clear();
                rl.setEnabled(false);
                is.setEnabled(false);
                fs.setEnabled(false);
                rl_input.setEnabled(false);
                is_input.setEnabled(false);
                fs_input.setEnabled(false);
                bs.setEnabled(true);
                bs_input.setEnabled(true);
                c=0;
                hi.setText("");
                Toast.makeText(getApplicationContext(),"Values in memory cleared!",Toast.LENGTH_SHORT).show();
            }
        });

        bs_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bs_list.add(Double.parseDouble(bs.getText().toString()));
                if(c==0) {
                    rl_input.setEnabled(true);
                    rl.setEnabled(true);
                }
                else{
                    is_input.setEnabled(true);
                    is.setEnabled(true);
                    fs_input.setEnabled(true);
                    fs.setEnabled(true);
                }
                bs.setText("");
                bs.setEnabled(false);
                bs_input.setEnabled(false);
                if(c==1) {
                    hi_list.add(bs_list.getLast() + rl_list.getLast());
                    hi.setText(hi_list.getLast().toString());
                }
                Toast.makeText(getApplicationContext(),"BS entered!",Toast.LENGTH_SHORT).show();
            }
        });

        rl_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_list.add(Double.parseDouble(rl.getText().toString()));
                is_input.setEnabled(true);
                is.setEnabled(true);
                fs_input.setEnabled(true);
                fs.setEnabled(true);
                bs_input.setEnabled(false);
                bs.setEnabled(false);
                rl.setText("");
                rl_input.setEnabled(false);
                rl.setEnabled(false);

                hi_list.add(bs_list.getLast() + rl_list.getLast());
                hi.setText(hi_list.getLast().toString());
                c=1;
                Toast.makeText(getApplicationContext(),"RL entered!",Toast.LENGTH_SHORT).show();
            }
        });

        is_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_list.add(Double.parseDouble(is.getText().toString()));
                is.setText("");

                rl_list.add(hi_list.getLast()-is_list.getLast());
                Toast.makeText(getApplicationContext(),"IS entered!",Toast.LENGTH_SHORT).show();
            }
        });

        fs_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fs_list.add(Double.parseDouble(fs.getText().toString()));
                fs.setText("");
                bs.setEnabled(true);
                bs_input.setEnabled(true);
                is.setEnabled(false);
                is_input.setEnabled(false);
                fs.setEnabled(false);
                fs_input.setEnabled(false);

                rl_list.add(hi_list.getLast()-fs_list.getLast());
                Toast.makeText(getApplicationContext(),"FS entered!",Toast.LENGTH_SHORT).show();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Values accepted!",Toast.LENGTH_SHORT).show();
                Log.e("BS:",bs_list.toString());
                Log.e("IS:",is_list.toString());
                Log.e("FS:",fs_list.toString());
                Log.e("HI:",hi_list.toString());
                Log.e("RL:",rl_list.toString());
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.reverse_fadein,R.anim.reverse_fadeout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
