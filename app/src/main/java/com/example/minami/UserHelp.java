package com.example.minami;

public class UserHelp {
    String username;
    MyOpenHelp openHelp;
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username= username;
    }

    public void setOpenHelp(MyOpenHelp openHelp){
        this.openHelp = openHelp;
    }

    public MyOpenHelp getOpenHelp(){
        return openHelp;
    }
}
