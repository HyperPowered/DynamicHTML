import net.hyperpowered.dynamichtml.DynamicHTML;
import net.hyperpowered.dynamichtml.model.DynamicDocument;
import net.hyperpowered.dynamichtml.options.LoaderOptions;
import net.hyperpowered.dynamichtml.utils.LoaderUtils;

public class AutomaticLoaderTest {

    public static void main(String[] args) {
        System.out.println(LoaderUtils.getFilesFromFolderInClasspath("default"));
        DynamicHTML d = new DynamicHTML(LoaderOptions.builder().automaticPathLoader(true).pathDefineLanguage(true).pathToLoad("default").build());
        for (DynamicDocument df : DynamicHTML.getDocuments().values()) {
            System.out.println(df.getName() + " Languages: "+df.getLanguages().keySet());
        }
    }
}
