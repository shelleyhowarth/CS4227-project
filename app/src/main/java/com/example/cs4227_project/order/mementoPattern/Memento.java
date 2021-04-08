package com.example.cs4227_project.order.mementoPattern;

import android.util.Log;

import com.example.cs4227_project.order.commandPattern.Stock;

public class Memento {
    private Stock state;

    public Memento(Stock state){
        Log.d("Memento", "Memento state " + state.getSizeQuantity());
        this.state = state;
    }

    public Stock getState(){
        return state;
    }
}
