package com.example.cs4227_project.order;

import java.util.Date;

public class CardDetails {
    String cardNum;
    String cardName;
    String cvv;
    Date expiryDate;

    public CardDetails(){

    }

    public CardDetails(String cardNum, String cardName,String cvv, Date date){
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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
