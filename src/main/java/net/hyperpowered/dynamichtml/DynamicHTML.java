package net.hyperpowered.dynamichtml;

import lombok.Getter;
import lombok.SneakyThrows;
import net.hyperpowered.dynamichtml.model.DynamicDocument;
import net.hyperpowered.dynamichtml.options.LoaderOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicHTML {

    public static DynamicHTML instance;

    public static DynamicHTML getInstance() {
        return instance;
    }

    public static void setInstance(DynamicHTML instance) {
        DynamicHTML.instance = instance;
    }

    public static DynamicDocument getDocument(String name){
        return instance.get(name);
    }
    public static DynamicDocument getDocument(String name, String exclude){
        return getDocuments().entrySet().stream().filter(e -> !e.getKey().equals(exclude)).findFirst().get().getValue();
    }

    public static Map<String, DynamicDocument> getDocuments(){
        return instance.documents;
    }

    @Getter
    private ConcurrentHashMap<String, DynamicDocument> documents = new ConcurrentHashMap<>();
    private ClassLoader classLoader;
    private LoaderOptions loaderOptions;

    public DynamicHTML(){
        this(new LoaderOptions());
    }

    @SneakyThrows
    public DynamicHTML(LoaderOptions loaderOptions){
        setInstance(this);
        this.loaderOptions = loaderOptions;
        this.classLoader = this.getClass().getClassLoader();
        if(loaderOptions.isAutomaticPathLoader()){
            getFilesInClasspath(loaderOptions.getPathToLoad()).forEach(s -> loadDocumentFromClasspath(s.getFileName().toString(), s.toString()));
        }
    }

    public DynamicDocument get(String name){
        return documents.get(name);
    }

    public DynamicDocument loadDocumentFromClasspath(String name, String classpath) {
        StringBuilder builder = new StringBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream(classpath)) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        builder.append(linha).append(System.lineSeparator());
                    }
                }
            } else {
                System.err.println("Arquivo não encontrado no classpath: " + classpath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        DynamicDocument document = new DynamicDocument(builder);
        documents.put(name, document);
        return document;
    }

    public DynamicDocument addFromText(String name, String HTML){
        DynamicDocument document = new DynamicDocument(new StringBuilder(HTML));
        documents.put(name, document);
        return document;
    }

    public static List<Path> getFilesInClasspath(String folder) throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(folder);

        if (resource == null) {
            return Collections.emptyList();
        }

        Path folderPath = Paths.get(resource.toURI());
        return listFilesInFolder(folderPath);
    }

    public static List<Path> listFilesInFolder(Path folderPath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
            List<Path> filesInFolder = new ArrayList<>();
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    filesInFolder.add(path);
                }
            }
            return filesInFolder;
        }
    }

}
