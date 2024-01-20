package net.hyperpowered.dynamichtml.utils;

import net.hyperpowered.dynamichtml.DynamicHTML;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoaderUtils {

    public static List<String> getFilesFromFolderInClasspath(String folderPath) {
        List<String> fileList = new ArrayList<>();

        // Obtém o ClassLoader atual
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Obtém a URL da pasta no classpath
        URL folderUrl = classLoader.getResource(folderPath);

        if (folderUrl != null) {
            try {
                // Converte a URL para um caminho de arquivo
                Path folderPathInFileSystem = Paths.get(folderUrl.toURI());

                // Lista todos os arquivos na pasta
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPathInFileSystem)) {
                    for (Path path : directoryStream) {
                        fileList.add(path.getFileName().toString());
                    }
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return fileList;
    }

    public static void addAllFiles(String folderPath, DynamicHTML instance){
        for (String s : getFilesFromFolderInClasspath(folderPath)) {
            String[] arg = s.split("\\.");
            if(arg.length == 1){
                addAllFiles(folderPath+"/"+s, instance);
            } else{
                instance.loadDocumentFromClasspath(folderPath+"/"+arg[0], "default", "/"+folderPath+"/"+s);
            }
        }
    }

}
