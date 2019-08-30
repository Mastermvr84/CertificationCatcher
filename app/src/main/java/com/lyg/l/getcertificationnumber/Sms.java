package com.lyg.l.getcertificationnumber;

/**
 * Created by L on 2016-01-17.
 */
public class Sms {
    private String tel;
    private String text;

    public Sms(){

    }
    public Sms(String tel, String text){
        this.tel = tel;
        this.text = text;
    }
    public String getTel(){
        return tel;
    }
    public String getText(){
        return text;
    }
    public void setTel(String tel){
        this.tel = tel;
    }
    public void setText(String text){
        this.text = text;
    }
    @Override
    public String toString(){
        return "SMS{" +
                "tel='" + tel +
                ", text='" + text +
                '}';
    }
}
