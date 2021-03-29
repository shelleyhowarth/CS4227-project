package com.example.cs4227_project.products.abstractFactoryPattern;

public class FactoryProducer {
    public static AbstractFactory getFactory(boolean female){
        if(female) {
            return new WomensFactory();
        } else {
            return new MensFactory();
        }
    }
}
