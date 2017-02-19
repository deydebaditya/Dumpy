package com.debashubham.dumpy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by deba on 17/2/17.
 */
public class SavedPDFActivity extends AppCompatActivity {

    ListView saved_pdf_list;
    String fileNames[];
    File files[];
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_pdf_activity);

        saved_pdf_list=(ListView)findViewById(R.id.list_saved_pdfs);
        back_button=(ImageView)findViewById(R.id.back_button_saved_pdf);
        try {
            File dumpyDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Dumpy");
            Log.e("Directory",String.valueOf(dumpyDir.exists()));
            if(dumpyDir.exists()){
                files=dumpyDir.listFiles();
                Log.e("Files:",files.toString());
                fileNames=new String[files.length];
                for(int i=0;i<files.length;i++){
                    fileNames[i]=String.valueOf(i+1)+". "+files[i].getName();
                    Log.e("Filename:",fileNames[i]);
                }
            }
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(),"Folder not found!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.adapter,R.id.adapter_text,fileNames);
        saved_pdf_list.setAdapter(adapter);
        saved_pdf_list.setTextFilterEnabled(true);

        saved_pdf_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(files[position]),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(),"No PDF reader application found. Please install one.",Toast.LENGTH_SHORT).show();
                    // Instruct the user to install a PDF reader here, or something
                }
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
}
