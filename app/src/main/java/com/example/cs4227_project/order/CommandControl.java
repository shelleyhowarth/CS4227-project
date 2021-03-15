package com.example.cs4227_project.order;

public class CommandControl {
    Command command;

    public CommandControl(){

    }

    public void setCommand(Command command){
        this.command = command;
    }

    public void executeCommand(){
        command.execute();
    }
}
