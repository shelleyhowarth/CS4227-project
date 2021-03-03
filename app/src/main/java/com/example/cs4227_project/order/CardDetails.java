package com.example.cs4227_project.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CardDetails {
    String cardNum;
    String cardName;
    String cvv;
    String expiryDate;

    public CardDetails(){

    }

    public CardDetails(String cardNum, String cardName,String cvv, String date){
        this.cardNum = cardNum;
        this.cardName = cardName;
        this.cvv = cvv;
        this.expiryDate = date;
    }


    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}