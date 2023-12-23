package net.hyperpowered.dynamichtml.model;

import java.util.List;
import java.util.Map;

public interface ProcessHandler {

    public String handler(Map<String, String> replace, List<OptionalKey> optionals, Map<String, MultipleComponent<?>> multiples);
}
