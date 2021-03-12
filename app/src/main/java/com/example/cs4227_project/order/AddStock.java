package com.example.cs4227_project.order;

public class AddStock implements Command{
    private Stock abcStock;

    public AddStock(Stock abcStock){
        this.abcStock = abcStock;
    }

    public void execute() {
        //abcStock.addStock();
    }
}
