package com.example.minami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InterfaceActivity extends AppCompatActivity {
    private Button insert_word;
    private Button learn_word;
    private Button test;
    private Button plan;
    private TextView book,learn_num,learned_num;

    public static UserHelp userHelp;
    private MyOpenHelp myOpenHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        myOpenHelp = userHelp.getOpenHelp();

        insert_word = findViewById(R.id.func_insert);
        learn_word = findViewById(R.id.func_learn);
        test = findViewById(R.id.func_self_test);
        plan = findViewById(R.id.func_plan);
        book = findViewById(R.id.tv_book);
        learn_num = findViewById(R.id.tv_learn_number);
        learned_num = findViewById(R.id.tv_learned_number);


        if(querybook()==1){
            book.setText("当前词书：四级词书");
        }else if(querybook()==2){
            book.setText("当前词书：六级词书");
        }else if(querybook()==3){
            book.setText("当前词书：高频词书");
        }else{

        }

        learn_num.setText("学习单词数："+query_learn_num());
        learned_num.setText("已学习单词数："+query());

        insert_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(InterfaceActivity.this, InsertWordActivity.class);
                startActivity(intent);
            }
        });

        learn_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(InterfaceActivity.this, LearnWordActivity.class);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(InterfaceActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(InterfaceActivity.this, PlanActivity.class);
                startActivity(intent);
            }
        });
    }

    public int querybook(){

        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("User",new String[]{"flag","word_book"},"name=?",new String[]{userHelp.getUsername()},
                null,null,null);
        cursor.moveToFirst();
        int flag= cursor.getInt(1);

        cursor.close();
        return flag;
    }

    public int query_learn_num(){

        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("User",new String[]{"flag","word_book","learn_num"},"name=?",new String[]{userHelp.getUsername()},
                null,null,null);
        cursor.moveToFirst();
        int flag= cursor.getInt(2);

        cursor.close();
        return flag;
    }

    public int query(){

        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        Cursor cursor = db.query("User",new String[]{"flag","word_book"},"name=?",new String[]{userHelp.getUsername()},
                null,null,null);
        cursor.moveToFirst();
        int flag= cursor.getInt(0);

        cursor.close();
        return flag;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = null;
            intent = new Intent(InterfaceActivity.this, MainActivity.class);
            startActivity(intent);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}