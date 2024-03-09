package net.hyperpowered.dynamichtml.utils;

import net.hyperpowered.dynamichtml.DynamicHTML;
import net.hyperpowered.dynamichtml.options.LoaderOptions;

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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL folderUrl = classLoader.getResource(folderPath);

        if (folderUrl != null) {
            try {
                Path folderPathInFileSystem = Paths.get(folderUrl.toURI());

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

    public static void addAllFiles(String folderPath, DynamicHTML instance, LoaderOptions options) {
        for (String s : getFilesFromFolderInClasspath(folderPath)) {
            String[] arg = s.split("\\.");
            if (arg.length == 1) {
                addAllFiles(folderPath + "/" + s, s, instance, options, true);
            } else {
                instance.loadDocumentFromClasspath(arg[0], "default", "/" + folderPath + "/" + s);
            }
        }
    }

    public static void addAllFiles(String folderPath, String rootPath, DynamicHTML instance, LoaderOptions options, boolean first) {
        for (String s : getFilesFromFolderInClasspath(folderPath)) {
            String[] arg = s.split("\\.");
            if (arg.length == 1) {
                addAllFiles(folderPath + "/" + s, rootPath + "/" + s, instance, options, false);
            } else {
                instance.loadDocumentFromClasspath(options.isPathDefineLanguage() && first ? arg[0] : rootPath + "/" + arg[0], options.isPathDefineLanguage() && first ? rootPath : "default", "/" + folderPath + "/" + s);
            }
        }
    }

}
