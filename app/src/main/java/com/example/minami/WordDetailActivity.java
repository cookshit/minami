package com.example.minami;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WordDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private MyOpenHelp myOpenHelp;

    private EditText et_cn;
    private TextView tv_en;
    private Button alter_cn,delete;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myOpenHelp =new MyOpenHelp(this,"MyVocabulary.db",null,1);

        et_cn = findViewById(R.id.detail_cn);
        tv_en = findViewById(R.id.detail_en);
        alter_cn = findViewById(R.id.detail_alter_cn);
        delete = findViewById(R.id.detail_delete);

        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            value = intent.getStringExtra("key");
            //Toast.makeText(DetailActivity.this,value,Toast.LENGTH_SHORT).show();
        }

        tv_en.setText(value);
        et_cn.setText(query());


        alter_cn.setOnClickListener(this);
        delete.setOnClickListener(this);


    }


    public String query(){

        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("Vocabulary",new String[]{"cn"},"en=?",new String[]{value},
                null,null,null);
        cursor.moveToFirst();
        String string = cursor.getString(0);

        cursor.close();
        return string;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_alter_cn:
                dbalter(value,et_cn.getText().toString().trim());
                mydialog();
                break;

            case R.id.detail_delete:
                dbdelete(value);
                mydialog2();
                break;
        }
    }

    private void dbalter(String en,String cn){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("cn",cn);
        db.update("Vocabulary",values,"en=?",new String[]{en});

        values.clear();
    }

    private void mydialog() {
        AlertDialog aldg;
        AlertDialog.Builder adBd = new AlertDialog.Builder(WordDetailActivity.this);
        adBd.setTitle("修改");

        adBd.setMessage("修改完成");
        adBd.setPositiveButton("返回录入单词页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                intent = new Intent(WordDetailActivity.this, InsertWordActivity.class);
                startActivity(intent);
            }
        });
        aldg=adBd.create();
        aldg.show();
    }

    private void mydialog2() {
        AlertDialog aldg;
        AlertDialog.Builder adBd = new AlertDialog.Builder(WordDetailActivity.this);
        adBd.setTitle("删除");

        adBd.setMessage("删除完成");
        adBd.setPositiveButton("返回录入单词页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                intent = new Intent(WordDetailActivity.this, InsertWordActivity.class);
                startActivity(intent);
            }
        });
        aldg=adBd.create();
        aldg.show();
    }

    private void dbdelete(String en){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Vocabulary","en=?",new String[] {en});
    }

}