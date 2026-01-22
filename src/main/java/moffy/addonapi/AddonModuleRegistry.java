package moffy.addonapi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

public final class AddonModuleRegistry {
    public static final AddonModuleRegistry INSTANCE = new AddonModuleRegistry();

    private Set<AddonModuleProvider> providers;
    private Map<ResourceLocation, AddonModule> loadedModules;
    private Map<ResourceLocation, ForgeConfigSpec.BooleanValue> compats;
    
    public AddonModuleRegistry(){
        loadedModules = new HashMap<>();
        compats = new HashMap<>();
        providers = new HashSet<>();
    }

    public void LoadModule(AddonModuleProvider provider, @Nullable ForgeConfigSpec.Builder configBuilder){

        providers.add(provider);
        provider.registerRawModules();
        Set<RawAddonModule> rawModules = provider.getRawAddonModules();

        if(configBuilder != null){
            configBuilder.comment("Provided by AddonAPI:", "Module Options").push("modules");
            for(RawAddonModule rawAddonModule : rawModules){
                if(!rawAddonModule.isMandatory()){
                    compats.put(rawAddonModule.getName(), configBuilder.define(rawAddonModule.getLabel(), true));
                }
            }
        }

        for(RawAddonModule rawAddonModule : rawModules){
            if(rawAddonModule.isMandatory() || rawAddonModule.isModsLoaded()){
                LazyOptional<AddonModule> addonModuleOptional = rawAddonModule.loadNewModule();
                if(addonModuleOptional.isPresent()){
                    AddonModule addonModule = addonModuleOptional.orElseThrow(IllegalStateException::new);

                    loadedModules.put(rawAddonModule.getName(), addonModule);
                    addonModule.init(provider.getContext());
                    DistExecutor.unsafeRunWhenOn(
                            Dist.CLIENT,
                            () -> () -> {
                                addonModule.initClient(provider.getContext());
                            }

                    );
                }
            } 
        }
    }

    public Map<ResourceLocation, AddonModule> getLoadedModules(){
        return this.loadedModules;
    }

    Map<ResourceLocation, ForgeConfigSpec.BooleanValue> getCompatSettings() {
        return compats;
    }

    Set<AddonModuleProvider> getProviders() {
        return providers;
    }
}
