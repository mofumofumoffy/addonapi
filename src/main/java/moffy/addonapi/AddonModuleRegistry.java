package moffy.addonapi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.LazyOptional;

public final class AddonModuleRegistry {
    public static final AddonModuleRegistry INSTANCE = new AddonModuleRegistry();

    private Set<AddonModuleProvider> providers;
    private Set<AddonModule> loadedModules;
    private Map<ResourceLocation, ForgeConfigSpec.BooleanValue> compats;
    
    public AddonModuleRegistry(){
        loadedModules = new HashSet<>();
        compats = new HashMap<>();
        providers = new HashSet<>();
    }

    public void LoadModule(AddonModuleProvider provider, ForgeConfigSpec.Builder configBuilder){

        providers.add(provider);
        provider.registerRawModules();
        Set<RawAddonModule> rawModules = provider.getRawAddonModules();

        configBuilder.comment("Provided by AddonAPI:", "Other Mod Compat Options").push("compat");
        for(RawAddonModule rawAddonModule : rawModules){
            if(!rawAddonModule.isMandatory()){
                compats.put(rawAddonModule.getName(), configBuilder.define(rawAddonModule.getLabel(), true));
            }
        }

        for(RawAddonModule rawAddonModule : rawModules){
            if(rawAddonModule.isMandatory() || rawAddonModule.isModsLoaded()){
                LazyOptional<AddonModule> addonModuleOptional = rawAddonModule.loadNewModule();
                if(addonModuleOptional.isPresent()){
                    loadedModules.add(addonModuleOptional.orElse(null));
                }
            } 
        }
    }

    public Set<AddonModule> getLoadedModules(){
        return this.loadedModules;
    }

    

    Map<ResourceLocation, ForgeConfigSpec.BooleanValue> getCompatSettings() {
        return compats;
    }

    Set<AddonModuleProvider> getProviders() {
        return providers;
    }
}
