package net.hyperpowered.model;

import net.hyperpowered.DynamicHTML;

import java.util.Map;

public class Document {

    private StringBuilder html;

    public Document(StringBuilder builder){
        this.html = builder;
    }

    public String process(Map<String, String> arguments, Map<String,
            OptionalKey> optionals, Map<String, MultipleComponent> multipleComponent){
        String fhtml = html.toString();
        for(String k : DynamicHTML.getDocuments().keySet()){
            if(fhtml.contains("@{" + k + "}")) {
                Document d = DynamicHTML.getDocument(k);
                if (d != this) {
                    fhtml = fhtml.replace("@{" + k + "}", d.process(arguments, optionals, multipleComponent));
                }
            }
        }
        for(String k : arguments.keySet()){
            fhtml = fhtml.replace("${"+k+"}", arguments.get(k));
        }
        for (String k : optionals.keySet()){
            fhtml = fhtml.replace("$["+k+"]", optionals.get(k).build());
        }
        for (String k : multipleComponent.keySet()) {
            fhtml = fhtml.replace("$("+k+")", multipleComponent.get(k).build());
        }
        return fhtml;
    }
}
