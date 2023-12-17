package net.hyperpowered.dynamichtml.model;

import net.hyperpowered.dynamichtml.DynamicHTML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleComponent<T> {

    private List<T> items;
    private String documentName;
    private DocumentHandler<T> handler;

    public MultipleComponent(List<T> items, String documentName, DocumentHandler<T> handler){
        this.items = items;
        this.documentName = documentName;
        this.handler = handler;
    }

    public String build(){
        StringBuilder builder = new StringBuilder();
        DynamicDocument doc = DynamicHTML.getDocument(documentName);
        for (T item : items) {
            Map<String, String> replace = new HashMap<>();
            List<OptionalKey> optionals = new ArrayList<>();
            Map<String, MultipleComponent<?>> multiples = new HashMap<>();
            handler.handler(replace, optionals, multiples, item);
            builder.append(doc.build(replace, optionals, multiples));
            builder.append("\n");
        }
        return builder.toString();
    }

    public interface DocumentHandler<T>{

        void handler(Map<String, String> replace, List<OptionalKey> optionals, Map<String, MultipleComponent<?>> multiples, T item);
    }
}
