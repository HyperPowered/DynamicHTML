package net.hyperpowered.dynamichtml.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.hyperpowered.dynamichtml.DynamicHTML;
import net.hyperpowered.dynamichtml.options.CacheOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class DynamicDocument {

    @Getter
    private String name;
    @Getter
    private Map<String, String> languages = new HashMap<>();
    @Getter
    @Setter
    private CacheOptions cacheOptions = new CacheOptions();
    private String cached = null;
    private Long lastCache = 0L;


    public DynamicDocument(String name){
        this.name = name;
    }

    public DynamicDocument(String name, CacheOptions cacheOptions) {
        this.name = name;
        this.cacheOptions = cacheOptions;
    }

    public void addLanguage(String builder, String language){
        languages.put(language, builder);
    }

    public String process(ProcessHandler handler){
        if(cacheOptions.isCachable()){
            if((lastCache + cacheOptions.getInvalidateInMilliseconds()) > System.currentTimeMillis()){
                return cached;
            }
        }
        Map<String, String> replace = new HashMap<>();
        List<OptionalKey> optionals = new ArrayList<>();
        Map<String, MultipleComponent<?>> multiples = new HashMap<>();
        String language = handler.handler(replace, optionals, multiples);
        return build(replace, optionals, multiples, language);
    }

    public String build(Map<String, String> arguments,
                          List<OptionalKey> optionals,
                          Map<String, MultipleComponent<?>> multipleComponent, String language) {
        StringBuilder fhtml = new StringBuilder(languages.get(language));
        for (String k : DynamicHTML.getDocuments().keySet()) {
            String placeholder = "@{" + k + "}";
            int index = fhtml.indexOf(placeholder);
            if (index != -1) {
                DynamicDocument d = DynamicHTML.getDocument(k);
                if (d != this) {
                    fhtml.replace(index, index + placeholder.length(), d.build(arguments, optionals, multipleComponent, language))
                            .toString();
                }
            }
        }
        for (String k : arguments.keySet()) {
            String placeholder = "${" + k + "}";
            int index = fhtml.indexOf(placeholder);
            while (index != -1) {
                fhtml.replace(index, index + placeholder.length(), arguments.get(k));
                index = fhtml.indexOf(placeholder, index + arguments.get(k).length());
            }
        }

        for (OptionalKey op : optionals) {
            String placeholder = "$[" + op.getKey() + "]";
            int index = fhtml.indexOf(placeholder);
            while (index != -1) {
                fhtml.replace(index, index + placeholder.length(), op.build());
                index = fhtml.indexOf(placeholder, index + op.build().length());
            }
        }

        for (String k : multipleComponent.keySet()) {
            String placeholder = "$(" + k + ")";
            int index = fhtml.indexOf(placeholder);
            while (index != -1) {
                fhtml.replace(index, index + placeholder.length(), multipleComponent.get(k).build());
                index = fhtml.indexOf(placeholder, index + multipleComponent.get(k).build().length());
            }
        }
        String finalHtml = fhtml.toString();
        if(cacheOptions.isCachable()){
            lastCache = System.currentTimeMillis();
            cached = finalHtml;
        }
        return finalHtml;
    }

}
