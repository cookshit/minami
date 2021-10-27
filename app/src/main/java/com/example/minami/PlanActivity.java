package com.example.minami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PlanActivity extends AppCompatActivity {

    public static UserHelp userHelp ;
    private MyOpenHelp myOpenHelp ;
    private RadioGroup mrg1,mrg2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        myOpenHelp = userHelp.getOpenHelp();


        mrg1 = findViewById(R.id.rg_1);
        mrg2 = findViewById(R.id.rg_2);

        mrg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                if(checkedId==R.id.rd_plan_number10){
                    int i1=10;
                    dbalter_learn_num(i1,userHelp.getUsername());
                }
                else if(checkedId==R.id.rd_plan_number25){
                    int i2 =25;
                    dbalter_learn_num(i2,userHelp.getUsername());
                }
                else if(checkedId==R.id.rd_plan_number40){
                    int i3 =40;
                    dbalter_learn_num(i3,userHelp.getUsername());
                }
                mydialog("已设置为"+radioButton.getText());
                //Toast.makeText(PlanActivity.this,"已设置为"+radioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });

        mrg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                if(checkedId==R.id.rd_plan_book1){
                    int i1=1;
                    dbalter(i1,userHelp.getUsername());
                }
                else if(checkedId==R.id.rd_plan_book2){
                    int i2 =2;
                    dbalter(i2,userHelp.getUsername());
                }
                else if(checkedId==R.id.rd_plan_book3){
                    int i3 =3;
                    dbalter(i3,userHelp.getUsername());
                }
                //Toast.makeText(PlanActivity.this,"已设置为"+radioButton.getText(),Toast.LENGTH_SHORT).show();
                mydialog("已设置为"+radioButton.getText());
            }
        });
    }



    private void dbalter(int i,String username){
        SQLiteDatabase db =  myOpenHelp.getWritableDatabase();;
        ContentValues values = new ContentValues();

        values.put("word_book",i);
        db.update("User",values,"name=?",new String[] {username});

        values.clear();
    }

    private void dbalter_learn_num(int i,String username){
        SQLiteDatabase db =  myOpenHelp.getWritableDatabase();;
        ContentValues values = new ContentValues();

        values.put("learn_num",i);
        db.update("User",values,"name=?",new String[] {username});

        values.clear();
    }

    private void mydialog(String str) {
        AlertDialog aldg;
        AlertDialog.Builder adBd = new AlertDialog.Builder(PlanActivity.this);
        adBd.setTitle("设置完成");

        adBd.setMessage(str);
        adBd.setPositiveButton("返回主页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                intent = new Intent(PlanActivity.this, InterfaceActivity.class);
                startActivity(intent);
            }
        });
        aldg=adBd.create();
        aldg.show();
    }
}