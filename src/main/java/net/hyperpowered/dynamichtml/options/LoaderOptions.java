package net.hyperpowered.dynamichtml.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoaderOptions {

    private boolean automaticPathLoader = false;
    private String pathToLoad = "templates";
}
