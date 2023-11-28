package net.hyperpowered.model;

import net.hyperpowered.DynamicHTML;

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
        Document doc = DynamicHTML.getDocument(documentName);
        for (T item : items) {
            builder.append(doc.process(handler.getArgs(item), handler.getOptionals(item), handler.getMultiple(item)));
            builder.append("\n");
        }
        return builder.toString();
    }

    public interface DocumentHandler<T>{
        Map<String, String> getArgs(T item);
        Map<String, OptionalKey> getOptionals(T item);
        Map<String, MultipleComponent> getMultiple(T item);
    }
}
