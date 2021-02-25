package com.example.cs4125_project.products;

public class FactoryProducer {
    public static AbstractFactory getFactory(boolean female){
        if(female) {
            return new WomensFactory();
        } else {
            return new MensFactory();
        }
    }
}
