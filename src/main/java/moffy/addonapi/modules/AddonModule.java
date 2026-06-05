package moffy.addonapi.modules;

import net.neoforged.api.distmarker.Dist;

import javax.annotation.Nullable;

public interface AddonModule {
    @Nullable Dist getDist();
}
