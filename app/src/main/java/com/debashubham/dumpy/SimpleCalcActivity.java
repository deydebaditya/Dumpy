package com.debashubham.dumpy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by deba on 12/2/17.
 */
public class SimpleCalcActivity extends AppCompatActivity {

    ImageView back_button;
    EditText bs,rl,is,fs;
    TextView hi,last_entered;
    Button bs_input,rl_input,is_input,fs_input,clear_button,clear_single_value;
    HashMap<Integer,Double> bs_list;
    HashMap<Integer,Double> rl_list;
    HashMap<Integer,Double> is_list;
    HashMap<Integer,Double> fs_list;
    HashMap<Integer,Double> hi_list;
    HashMap<Integer,String> remarks_list;
    LinkedList<Integer> values_list;
    LinkedList<Integer> hi_counter_list;
    FloatingActionButton accept;
    String remarks="";
    int c=0,counter=0,store_hi_counter=0,change_point_count=0;
    int flag_enter=9999;//0 for only BS,1 for RL,2 for BS+FS+HI+RL,3 for IS+RL,4 for FS+HI
    Document document;
    File pdfDirectory;
    File newFile;
    boolean dirSuccess=false;
    SharedPreferences introduction;
    String work_name,date_of_survey,surveyor_name;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
    public String formattedDate = df.format(calendar.getTime());

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_calc_acitivity);
        back_button=(ImageView)findViewById(R.id.back_button_simple_calc);

        bs=(EditText)findViewById(R.id.BS);
        rl=(EditText)findViewById(R.id.RL);
        is=(EditText)findViewById(R.id.IS);
        fs=(EditText)findViewById(R.id.FS);
        hi=(TextView)findViewById(R.id.HI);
        last_entered=(TextView)findViewById(R.id.last_entered);

        document=new Document();
        introduction=getSharedPreferences("introduction",MODE_PRIVATE);
        String restoredText=introduction.getString("work_name",null);
        if(restoredText!=null){
            work_name=introduction.getString("work_name","Default");
            surveyor_name=introduction.getString("surveyor_name","Default");
            date_of_survey=introduction.getString("date_of_survey","00/00/0000");
        }

        pdfDirectory=new File(Environment.getExternalStorageDirectory()+File.separator+"Dumpy");
        if(!pdfDirectory.exists()){
            dirSuccess=pdfDirectory.mkdirs();
            Log.e("File:","Directory Dumpy created!");
        }
        else{
            Log.e("File:","Dumpy Directory already exists!");
        }

        bs_input=(Button)findViewById(R.id.button_BS);
        rl_input=(Button)findViewById(R.id.button_RL);
        is_input=(Button)findViewById(R.id.button_IS);
        fs_input=(Button)findViewById(R.id.button_FS);
        clear_button=(Button)findViewById(R.id.clear_button);
        clear_single_value=(Button)findViewById(R.id.clear_last_button);


        accept=(FloatingActionButton)findViewById(R.id.fab_accept_simple_values);

        rl.setEnabled(false);
        is.setEnabled(false);
        fs.setEnabled(false);
        rl_input.setEnabled(false);
        is_input.setEnabled(false);
        fs_input.setEnabled(false);
        accept.setEnabled(false);
        clear_single_value.setEnabled(false);

        bs_list=new HashMap<>();
        rl_list=new HashMap<>();
        is_list=new HashMap<>();
        fs_list=new HashMap<>();
        hi_list=new HashMap<>();
        remarks_list=new HashMap<>();
        values_list=new LinkedList<>();
        hi_counter_list=new LinkedList<>();


        clear_single_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!values_list.isEmpty()) {
                    switch (values_list.getLast()) {
                        case 0:
                            bs_list.remove(counter);
                            remarks_list.remove(counter);
                            counter--;
                            values_list.removeLast();
                            flag_enter=9999;
                            last_entered.setText("No Data!");
                            clear_single_value.setEnabled(false);
                            bs.setEnabled(true);
                            is.setEnabled(false);
                            rl.setEnabled(false);
                            fs.setEnabled(false);
                            bs_input.setEnabled(true);
                            is_input.setEnabled(false);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(false);
                            accept.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"All values cleared, no data remaining!",Toast.LENGTH_SHORT).show();
                            bs.setEnabled(true);
                            is.setEnabled(false);
                            rl.setEnabled(false);
                            fs.setEnabled(false);
                            bs_input.setEnabled(true);
                            is_input.setEnabled(false);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"BS value cleared!",Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            rl_list.remove(counter);
                            hi_list.remove(store_hi_counter);
                            hi_counter_list.removeLast();
                            if(!hi_counter_list.isEmpty())
                                store_hi_counter=hi_counter_list.getLast();
                            else
                                store_hi_counter=0;
                            values_list.removeLast();
                            last_entered.setText(getLastData(values_list.getLast()));
                            bs.setEnabled(false);
                            is.setEnabled(false);
                            rl.setEnabled(true);
                            fs.setEnabled(false);
                            bs_input.setEnabled(false);
                            is_input.setEnabled(false);
                            rl_input.setEnabled(true);
                            fs_input.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"RL and HI value cleared!",Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            bs_list.remove(counter);
                            hi_list.remove(store_hi_counter);
                            hi_counter_list.removeLast();
                            if(!hi_counter_list.isEmpty())
                                store_hi_counter=hi_counter_list.getLast();
                            else
                                store_hi_counter=0;
                            Log.e("Counter:",String.valueOf(counter));
                            values_list.removeLast();
                            last_entered.setText(getLastData(values_list.getLast()));
                            bs.setEnabled(true);
                            is.setEnabled(false);
                            rl.setEnabled(false);
                            fs.setEnabled(false);
                            bs_input.setEnabled(true);
                            is_input.setEnabled(false);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"BS value cleared!",Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            is_list.remove(counter);
                            rl_list.remove(counter);
                            counter--;
                            values_list.removeLast();
                            last_entered.setText(getLastData(values_list.getLast()));
                            bs.setEnabled(false);
                            is.setEnabled(true);
                            rl.setEnabled(false);
                            fs.setEnabled(true);
                            bs_input.setEnabled(false);
                            is_input.setEnabled(true);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"IS and RL value cleared!",Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            fs_list.remove(counter);
                            rl_list.remove(counter);
                            remarks_list.remove(counter);
                            change_point_count--;
                            counter--;
                            values_list.removeLast();
                            last_entered.setText(getLastData(values_list.getLast()));
                            bs.setEnabled(false);
                            is.setEnabled(true);
                            rl.setEnabled(false);
                            fs.setEnabled(true);
                            bs_input.setEnabled(false);
                            is_input.setEnabled(true);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"FS and HI value cleared!",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            last_entered.setText(getLastData(values_list.getLast()));
                            bs.setEnabled(true);
                            is.setEnabled(false);
                            rl.setEnabled(false);
                            fs.setEnabled(false);
                            bs_input.setEnabled(true);
                            is_input.setEnabled(false);
                            rl_input.setEnabled(false);
                            fs_input.setEnabled(false);
                            Toast.makeText(getApplicationContext(),"All values cleared, no data remaining!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    flag_enter=9999;
                    last_entered.setText("No Data!");
                    clear_single_value.setEnabled(false);
                    bs.setEnabled(true);
                    is.setEnabled(false);
                    rl.setEnabled(false);
                    fs.setEnabled(false);
                    bs_input.setEnabled(true);
                    is_input.setEnabled(false);
                    rl_input.setEnabled(false);
                    fs_input.setEnabled(false);
                    accept.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"All values cleared, no data remaining!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bs_list.clear();
                rl_list.clear();
                is_list.clear();
                fs_list.clear();
                hi_list.clear();
                remarks_list.clear();
                values_list.clear();
                rl.setEnabled(false);
                is.setEnabled(false);
                fs.setEnabled(false);
                last_entered.setText("");
                rl_input.setEnabled(false);
                is_input.setEnabled(false);
                fs_input.setEnabled(false);
                bs.setEnabled(true);
                bs_input.setEnabled(true);
                accept.setEnabled(false);
                clear_single_value.setEnabled(false);
                c=0;
                counter=0;
                change_point_count=0;
                hi.setText("");
                Toast.makeText(getApplicationContext(),"Values in memory cleared!",Toast.LENGTH_SHORT).show();
            }
        });

        bs_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c==0) {
                    rl_input.setEnabled(true);
                    rl.setEnabled(true);
                    clear_single_value.setEnabled(true);
                    counter++;
                    remarks="B.M.";
                    remarks_list.put(counter,remarks);
                    bs_list.put(counter,Double.parseDouble(bs.getText().toString()));
                    values_list.add(0);
                    last_entered.setText(getLastData(values_list.getLast()));

                    flag_enter=0;
                }
                else {
                    is_input.setEnabled(true);
                    is.setEnabled(true);
                    fs_input.setEnabled(true);
                    fs.setEnabled(true);
                    bs_list.put(counter,Double.parseDouble(bs.getText().toString()));
                    Log.e("BS:","Entered!");
                    values_list.add(2);
                    last_entered.setText(getLastData(values_list.getLast()));

                    flag_enter=2;
                }
                bs.setText("");
                bs.setEnabled(false);
                bs_input.setEnabled(false);
                if(c==1) {
                    Log.e("Counter:",String.valueOf(counter));
                    hi_list.put(counter,(Math.round((bs_list.get(counter) + rl_list.get(counter))*1000.0)/1000.0));
                    store_hi_counter=counter;
                    hi_counter_list.add(store_hi_counter);
                    hi.setText(String.valueOf(bs_list.get(counter) + rl_list.get(counter)));
                }
                Toast.makeText(getApplicationContext(),"BS entered!",Toast.LENGTH_SHORT).show();
            }
        });

        rl_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_list.put(counter,Double.parseDouble(rl.getText().toString()));
                is_input.setEnabled(true);
                is.setEnabled(true);
                fs_input.setEnabled(true);
                fs.setEnabled(true);
                bs_input.setEnabled(false);
                bs.setEnabled(false);
                rl.setText("");
                rl_input.setEnabled(false);
                rl.setEnabled(false);

                hi_list.put(counter,(Math.round((bs_list.get(counter) + rl_list.get(counter))*1000.0)/1000.0));
                store_hi_counter=counter;
                hi_counter_list.add(store_hi_counter);
                hi.setText(String.valueOf(hi_list.get(counter)));
                c=1;
                Toast.makeText(getApplicationContext(),"RL entered!",Toast.LENGTH_SHORT).show();
                values_list.add(1);
                last_entered.setText(getLastData(values_list.getLast()));

                flag_enter=1;
            }
        });

        is_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                is_list.put(counter,Double.parseDouble(is.getText().toString()));
                is.setText("");

                rl_list.put(counter,Math.round((hi_list.get(store_hi_counter)-is_list.get(counter))*1000.0)/1000.0);
                Toast.makeText(getApplicationContext(),"IS entered!",Toast.LENGTH_SHORT).show();
                values_list.add(3);
                last_entered.setText(getLastData(values_list.getLast()));

                flag_enter=3;
            }
        });

        fs_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                change_point_count++;
                remarks="CP:"+String.valueOf(change_point_count);
                remarks_list.put(counter,remarks);
                fs_list.put(counter,Double.parseDouble(fs.getText().toString()));
                fs.setText("");
                bs.setEnabled(true);
                bs_input.setEnabled(true);
                is.setEnabled(false);
                is_input.setEnabled(false);
                fs.setEnabled(false);
                fs_input.setEnabled(false);
                accept.setEnabled(true);

                rl_list.put(counter,Math.round((hi_list.get(store_hi_counter)-fs_list.get(counter))*1000.0)/1000.0);
                Toast.makeText(getApplicationContext(),"FS entered!",Toast.LENGTH_SHORT).show();
                values_list.add(4);
                last_entered.setText(getLastData(values_list.getLast()));

                flag_enter=4;
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks="Last Station";
                remarks_list.put(counter,remarks);
                Toast.makeText(getApplicationContext(),"Values accepted!",Toast.LENGTH_SHORT).show();
                Log.e("BS:",bs_list.toString());
                Log.e("IS:",is_list.toString());
                Log.e("FS:",fs_list.toString());
                Log.e("HI:",hi_list.toString());
                Log.e("RL:",rl_list.toString());
                Log.e("Remarks:",remarks_list.toString());
                accept.setEnabled(false);


                newFile=new File(Environment.getExternalStorageDirectory()+File.separator+"Dumpy"+File.separator+"Reading"+formattedDate+".pdf");

                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"File not created!",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(newFile));
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),"File to be written not created!",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                document.open();
                try {
                    addMetaData(document);
                    addTitlePage(document);
                    addContent(document);
                    document.close();
                    Toast.makeText(getApplicationContext(),"PDF Exported to "+newFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(newFile),"application/pdf");
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

    private String getLastData(int flag){

        switch(flag){
            case 0:
                return "BS: "+bs_list.get(counter).toString();
            case 1:
                return "RL: "+rl_list.get(counter).toString();
            case 2:
                return "BS: "+bs_list.get(counter).toString();
            case 3:
                return "IS: "+is_list.get(counter).toString();
            case 4:
                return "FS: "+fs_list.get(counter).toString();
            default:
                return "No Data";
        }

    }

    private void addMetaData(Document document) {
        document.addTitle("Report generated on:"+formattedDate);
        document.addSubject("Dumpy Level Report");
        document.addKeywords("Dumpy Level, PDF");
        document.addAuthor("Dumpy");
        document.addCreator("Dumpy");
    }

    private void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("SURVEY REPORT", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + surveyor_name + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document is the report of a survey conducted on "+date_of_survey,
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document is the report of a survey with the work named as: "+work_name,
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with the user.",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("Survey Report", catFont);
        anchor.setName("SurveyReport");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Entries:", subFont);
        Section subCatPart = catPart.addSection(subPara);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);
    }

    private void createTable(Section subCatPart)throws BadElementException{
        PdfPTable entry_table=new PdfPTable(7);
        entry_table.setWidthPercentage(100);
        PdfPCell id=new PdfPCell(new Phrase("Station Point"));
            id.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell bs=new PdfPCell(new Phrase("BS"));
            bs.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell is=new PdfPCell(new Phrase("IS"));
            is.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell fs=new PdfPCell(new Phrase("FS"));
            fs.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell hi=new PdfPCell(new Phrase("HI"));
            hi.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell rl=new PdfPCell(new Phrase("RL"));
            rl.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell remarks=new PdfPCell(new Phrase("REMARKS"));
            remarks.setHorizontalAlignment(Element.ALIGN_CENTER);
        entry_table.addCell(id);
        entry_table.addCell(bs);
        entry_table.addCell(is);
        entry_table.addCell(fs);
        entry_table.addCell(hi);
        entry_table.addCell(rl);
        entry_table.addCell(remarks);
        entry_table.setHeaderRows(1);
        int largest = rl_list.size();

        Log.e("Total RL:",String.valueOf(largest));
        double total_bs=0,total_fs=0,first_rl=0,last_rl=0;

        for(int i=1;i<=largest;i++){

            entry_table.addCell(String.valueOf(i));

            if(i==1)
                first_rl=rl_list.get(i);
            if(i==largest)
                last_rl=rl_list.get(i);

            if(bs_list.containsKey(i)){
                entry_table.addCell(String.valueOf(bs_list.get(i)));
                total_bs+=bs_list.get(i);
                Log.e("TABLE:","BS");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","BS not entered");
            }
            if(is_list.containsKey(i)){
                entry_table.addCell(String.valueOf(is_list.get(i)));
                Log.e("TABLE:","IS");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","IS not entered");
            }
            if(fs_list.containsKey(i)){
                entry_table.addCell(String.valueOf(fs_list.get(i)));
                total_fs+=fs_list.get(i);
                Log.e("TABLE:","FS");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","FS not entered");
            }
            if(hi_list.containsKey(i)){
                entry_table.addCell(String.valueOf(hi_list.get(i)));
                Log.e("TABLE:","HI");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","HI not entered");
            }
            if(rl_list.containsKey(i)){
                entry_table.addCell(String.valueOf(rl_list.get(i)));
                Log.e("TABLE:","RL");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","RL not entered");
            }
            if(remarks_list.containsKey(i)){
                entry_table.addCell(String.valueOf(remarks_list.get(i)));
                Log.e("TABLE:","REMARKS");
            }
            else{
                entry_table.addCell("");
                Log.e("TABLE:","REMARKS not entered");
            }
        }
        subCatPart.add(entry_table);
        Log.e("Total BS:",String.valueOf(total_bs));
        Log.e("Total FS:",String.valueOf(total_fs));
        Log.e("First RL:",String.valueOf(first_rl));
        Log.e("Last RL:",String.valueOf(last_rl));
        Paragraph check_para=new Paragraph("Arithmetic Check:",subFont);
        addEmptyLine(check_para,2);
        check_para.add("Summation(BS) - Summation(FS) = Last RL - First RL");
        addEmptyLine(check_para,1);
        double bsfs_diff=Math.round((total_bs-total_fs)*1000.0)/1000.0;
        double rl_diff=Math.round((last_rl-first_rl)*1000.0)/1000.0;
        boolean checktrue=(bsfs_diff)==(rl_diff)?true:false;
        check_para.add(String.valueOf(bsfs_diff)+" = "+String.valueOf(rl_diff));
        addEmptyLine(check_para,1);
        if(checktrue){
            check_para.setSpacingBefore(20);
            check_para.add("OK");
        }
        else{
            check_para.setSpacingBefore(20);
            check_para.add("Check Fail!");
        }
        subCatPart.add(check_para);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
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
        if (id == R.id.show_pdfs) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
