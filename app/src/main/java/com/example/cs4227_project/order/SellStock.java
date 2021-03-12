package com.example.cs4227_project.order;

public class SellStock implements Command{
    private Stock abcStock;

    public SellStock(Stock abcStock){
        this.abcStock = abcStock;
    }

    public void execute() {
        //abcStock.sell();
    }
}
