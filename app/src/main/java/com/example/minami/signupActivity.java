package com.example.minami;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signupActivity extends AppCompatActivity {
    private EditText et_username ,et_password ,et_passwordagain;
    private Button btn_yes;
    //数据库对象
    private MyOpenHelp myOpenHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_username = findViewById(R.id.signup_et_username);
        et_password =findViewById(R.id.signup_et_password);
        et_passwordagain = findViewById(R.id.signup_et_passwordagain);
        btn_yes =findViewById(R.id.signup_btn_yes);

        myOpenHelp = new MyOpenHelp(this,"MyUser.db",null,1);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String passwordagain =et_passwordagain.getText().toString();
                Intent intent = null;

                query(username);

                if(query(username)==1){
                    Toast.makeText(getApplicationContext(),"该用户名已被注册！",Toast.LENGTH_SHORT).show();
                }else if(query(username)==0){
                    if(password.equals(passwordagain)&&username.length() !=0&&password.length()!=0){

                        dbinsert(username,password);
                        Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
                        //跳转登录界面
                        intent = new Intent(signupActivity.this,MainActivity.class);
                        startActivity(intent);

                    }else if(!password.equals(passwordagain)){
                        Toast.makeText(getApplicationContext(),"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                    }else if(username.length() ==0||password.length()==0){
                        Toast.makeText(getApplicationContext(),"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                    }

                }
                //判断两次输入密码相同以及输入框非空


            }
        });

    }
    private void dbinsert(String username,String password){
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",username);
        values.put("password",password);
        values.put("flag",0);
        values.put("word_book",1);
        values.put("learn_num",10);
        db.insert("User",null,values);
        values.clear();

    }


//    public void dbalter(String en,String alen){
//        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put("en",alen);
//        db.update("Vocabulary",values,"en=?",new String[]{en});
//
//        values.clear();
//    }
    //待修改

//    public void dbdelete(String en){
//        SQLiteDatabase db = myOpenHelp.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        db.delete("Vocabulary","en=?",new String[] {en});
//    }

    private int query(String username){
        int flag=0;
        SQLiteDatabase db = myOpenHelp.getWritableDatabase();

        Cursor cursor = db.query("User",null,null,null,
                    null,null,null);

        if(cursor.moveToFirst()){
            do{
                if(username.equals(cursor.getString(cursor.getColumnIndex("name")))){
                    flag = 1;
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();

        return flag;
    }

}