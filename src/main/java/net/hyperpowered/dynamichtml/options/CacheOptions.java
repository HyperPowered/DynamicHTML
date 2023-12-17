package net.hyperpowered.dynamichtml.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CacheOptions {

    private boolean cachable = false;
    private long invalidateInMilliseconds = 1000 * 60 * 60;

}
