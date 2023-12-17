package net.hyperpowered.dynamichtml.model;

import net.hyperpowered.dynamichtml.DynamicHTML;
import net.hyperpowered.dynamichtml.options.CacheOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicDocument {

    private StringBuilder html;
    private CacheOptions cacheOptions = new CacheOptions();
    private String cached = null;
    private Long lastCache = 0L;

    public DynamicDocument(StringBuilder builder) {
        this.html = builder;
    }

    public DynamicDocument(StringBuilder builder, CacheOptions cacheOptions) {
        this.html = builder;
        this.cacheOptions = cacheOptions;
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
        handler.handler(replace, optionals, multiples);
        return build(replace, optionals, multiples);
    }

    public String build(Map<String, String> arguments,
                          List<OptionalKey> optionals,
                          Map<String, MultipleComponent<?>> multipleComponent) {
        String fhtml = html.toString();
        for (String k : DynamicHTML.getDocuments().keySet()) {
            String placeholder = "@{" + k + "}";
            int index = fhtml.indexOf(placeholder);
            if (index != -1) {
                DynamicDocument d = DynamicHTML.getDocument(k);
                if (d != this) {
                    fhtml = new StringBuilder(fhtml)
                            .replace(index, index + placeholder.length(), d.build(arguments, optionals, multipleComponent))
                            .toString();
                }
            }
        }

        for (String k : arguments.keySet()) {
            fhtml = fhtml.replace("${" + k + "}", arguments.get(k));
        }

        for (OptionalKey op : optionals) {
            fhtml = fhtml.replace("$[" + op.getKey() + "]", op.build());
        }
        for (String k : multipleComponent.keySet()) {
            fhtml = fhtml.replace("$(" + k + ")", multipleComponent.get(k).build());
        }
        if(cacheOptions.isCachable()){
            lastCache = System.currentTimeMillis();
            cached = fhtml;
        }
        return fhtml;
    }

    interface ProcessHandler{
        void handler(Map<String, String> replace, List<OptionalKey> optionals, Map<String, MultipleComponent<?>> multiples);
    }
}
