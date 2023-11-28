package net.hyperpowered.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OptionalKey {

    private String element;
    private boolean value;

    public String build(){
        if(value){
            return element;
        }
        return "";
    }
}
