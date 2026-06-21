package moffy.addonapi;

import moffy.addonapi.modules.ClientAddonModule;
import moffy.addonapi.modules.CommonAddonModule;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class AddonModuleRegistry {
    public static final AddonModuleRegistry INSTANCE = new AddonModuleRegistry();

    private final Set<AddonModuleProvider> providers;
    private final Map<ResourceLocation, CommonAddonModule> loadedCommonModules;
    private final Map<ResourceLocation, ClientAddonModule> loadedClientModules;
    private final Map<ResourceLocation, ModConfigSpec.BooleanValue> compats;

    public AddonModuleRegistry(){
        loadedCommonModules = new HashMap<>();
        loadedClientModules = new HashMap<>();
        compats = new HashMap<>();
        providers = new HashSet<>();
    }

    public void loadCommonModule(AddonModuleProvider provider, @Nullable ModConfigSpec.Builder configBuilder){
        if(configBuilder != null){
            configBuilder.comment("Provided by AddonAPI:", "Module Options").push("modules");
            for(RawAddonModule rawAddonModule : provider.getRawAddonModules()){
                if(!rawAddonModule.isMandatory()){
                    compats.put(rawAddonModule.getName(), configBuilder.define(rawAddonModule.getLabel(), true));
                }
            }
        }

        loadModule(provider,configBuilder,(rawAddonModule) -> {
            Optional<CommonAddonModule> addonModuleOptional = rawAddonModule.loadNewCommonModule();
            if(addonModuleOptional.isPresent()){
                CommonAddonModule addonModule = addonModuleOptional.get();

                loadedCommonModules.put(rawAddonModule.getName(), addonModule);
                addonModule.init(provider.getEventBus(), provider.getContainer());
            }
        });
    }

    public void loadClientModule(AddonModuleProvider provider, @Nullable ModConfigSpec.Builder configBuilder){
        if(configBuilder != null){
            configBuilder.comment("Provided by AddonAPI:", "Module Options").push("modules");
            for(RawAddonModule rawAddonModule : provider.getRawAddonModules()){
                if(!rawAddonModule.isMandatory()){
                    compats.put(rawAddonModule.getName(), configBuilder.define(rawAddonModule.getLabel(), true));
                }
            }
        }

        loadModule(provider,configBuilder,(rawAddonModule) -> {
            Optional<ClientAddonModule> addonModuleOptional = rawAddonModule.loadNewClientModule();
            if(addonModuleOptional.isPresent()){
                ClientAddonModule addonModule = addonModuleOptional.get();

                loadedClientModules.put(rawAddonModule.getName(), addonModule);
                addonModule.initClient(provider.getEventBus(), provider.getContainer());
            }
        });
    }

    private void loadModule(AddonModuleProvider provider, @Nullable ModConfigSpec.Builder configBuilder, Consumer<RawAddonModule> callback){

        providers.add(provider);
        provider.registerRawModules();
        List<RawAddonModule> rawModules = provider.getRawAddonModules();

        rawModules.sort(Comparator.comparingInt(rawModule -> -rawModule.getPriority()));

        for(RawAddonModule rawAddonModule : rawModules){
            if(rawAddonModule.isMandatory() || rawAddonModule.isModsLoaded()){
                callback.accept(rawAddonModule);
            }
        }
    }

    public Map<ResourceLocation, CommonAddonModule> getLoadedCommonModules(){
        return this.loadedCommonModules;
    }

    public Map<ResourceLocation, ClientAddonModule> getLoadedClientModules(){
        return this.loadedClientModules;
    }

    public Map<ResourceLocation, ModConfigSpec.BooleanValue> getCompatSettings() {
        return compats;
    }

    public Set<AddonModuleProvider> getProviders() {
        return providers;
    }
}
