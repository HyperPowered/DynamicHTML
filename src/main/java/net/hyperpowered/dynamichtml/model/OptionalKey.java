package net.hyperpowered.dynamichtml.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OptionalKey {

    private String key;
    private String element;
    private boolean condition;

    public String build(){
        return condition ? element : "";
    }
}
