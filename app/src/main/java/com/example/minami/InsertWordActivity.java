package com.example.minami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InsertWordActivity extends AppCompatActivity {
    private EditText et_en;
    private EditText et_cn;
    private Button btn_insert;
    private Button btn_query;
    private TextView tv_word;

    private MyOpenHelp myOpenHelp;

    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertword);

        myOpenHelp = new MyOpenHelp(this,"MyVocabulary.db",null,1);

        et_en = findViewById(R.id.et_enter_word);
        et_cn= findViewById(R.id.et_enter_zhongwenhanyi);
        btn_insert =findViewById(R.id.btn_enter_yes);
        btn_query = findViewById(R.id.btn_enter_query);
        tv_word = findViewById(R.id.tv_enter_word);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String en = et_en.getText().toString();
                String cn = et_cn.getText().toString();
                query(en);

                if((en.length()==0||cn.length()==0)&&query(en)==0){
                    Toast.makeText(getApplicationContext(),"请输入内容！",Toast.LENGTH_SHORT).show();
                }else if(en.length()!=0&&cn.length()!=0&&query(en)==0){
                    dbinsert(en,cn);
                    et_en.setText("");
                    et_cn.setText("");
                    Toast.makeText(getApplicationContext(),"录入成功！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = myOpenHelp.getWritableDatabase();
                Cursor cursor = db.query("Vocabulary",null,null,null,
                        null,null,null);
                List<String> word_list=new ArrayList<>();
                if(cursor.moveToFirst()){
                    do{
                        String ss =cursor.getString(cursor.getColumnIndex("en"));
                        word_list.add(ss);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                String [] word_Array = word_list.toArray(new String[word_list.size()]);

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(InsertWordActivity.this,android.R.layout.simple_list_item_1,word_Array);
                ListView listview=(ListView)findViewById(R.id.list_view);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(InsertWordActivity.this,word_Array[position],Toast.LENGTH_SHORT).show();
                        Intent intent = null;
                        intent = new Intent(InsertWordActivity.this, WordDetailActivity.class);
                        intent.putExtra("key",word_Array[position]);
                        startActivity(intent);
                    }
                });
//                for (String string:word_Array){
//
//                }

            }
        });
    }
    private void dbinsert(String en,String cn){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("en",en);
        values.put("cn",cn);
        db.insert("Vocabulary",null,values);
        values.clear();

    }

    private void dbalter(String en,String alen){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("en",alen);
        db.update("Vocabulary",values,"en=?",new String[]{en});

        values.clear();
    }

    private void dbdelete(String en){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("Vocabulary","en=?",new String[] {en});
    }

    private int query(String insert_en){
        int flag = 0;
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("Vocabulary",null,null,null,
                null,null,null);

        if(cursor.moveToFirst()){
            do{
                String en =cursor.getString(cursor.getColumnIndex("en"));
                if(en.equals(insert_en)){
                    flag = 1;
                    Toast.makeText(getApplicationContext(),"该单词已存在！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return flag;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = null;
            intent = new Intent(InsertWordActivity.this, InterfaceActivity.class);
            startActivity(intent);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}