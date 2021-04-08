package com.example.cs4227_project.order.mementoPattern;

import android.util.Log;

import com.example.cs4227_project.order.commandPattern.Stock;

public class Originator {
    private Stock state;

    public void setState(Stock state){
        Log.d("Memento", "Stock set state " + state.getSizeQuantity());
        this.state = state;
    }

    public Stock getState(){
        Log.d("Memento", "Getting stock state");
        Log.d("Memento", "Stock state " + state.getSizeQuantity());
        return state;
    }

    public Memento saveStateToMemento(){
        Log.d("Memento", "Saving memento state  " + state.getSizeQuantity());
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento){
        Log.d("Memento", "Getting memento state");
        state = memento.getState();
    }
}
