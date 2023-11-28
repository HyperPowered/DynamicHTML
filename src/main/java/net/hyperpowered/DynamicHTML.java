package net.hyperpowered;

import lombok.Getter;
import net.hyperpowered.model.Document;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicHTML {

    public static DynamicHTML instance;

    public static DynamicHTML getInstance() {
        return instance;
    }

    public static void setInstance(DynamicHTML instance) {
        DynamicHTML.instance = instance;
    }

    public static Document getDocument(String name){
        return instance.get(name);
    }
    public static Document getDocument(String name, String exclude){
        return getDocuments().entrySet().stream().filter(e -> !e.getKey().equals(exclude)).findFirst().get().getValue();
    }

    public static Map<String, Document> getDocuments(){
        return instance.documents;
    }

    @Getter
    private ConcurrentHashMap<String, Document> documents = new ConcurrentHashMap<>();

    public DynamicHTML(){
        setInstance(this);
    }

    public Document get(String name){
        return documents.get(name);
    }

    public Document loadDocumentFromClasspath(String name, String classpath){
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(this.getClass().getResourceAsStream(classpath));
        while(scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }
        Document document = new Document(builder);
        documents.put(name, document);
        return document;
    }


}
