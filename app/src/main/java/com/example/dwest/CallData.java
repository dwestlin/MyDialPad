package com.example.dwest;

public class CallData {
    public String number;
    public String date;
    public String position;

    public CallData(){

    }

    public CallData(String number, String date, String pos){
        this.number = number;
        this.date = date;
        this.position = pos;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String pos) {
        this.position = pos;
    }

}
