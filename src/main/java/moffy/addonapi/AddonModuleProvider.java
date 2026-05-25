package moffy.addonapi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public abstract class AddonModuleProvider {
    private final List<RawAddonModule> rawAddonModules = new ArrayList<>();
    private final FMLJavaModLoadingContext context;

    public AddonModuleProvider(FMLJavaModLoadingContext context){
        this.context = context;
    }

    public FMLJavaModLoadingContext getContext() {
        return context;
    }

    public abstract void registerRawModules();

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs){
        addRawModule(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs));
    }

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs, int priority){
        addRawModule(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs, priority));
    }

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs, boolean mandatory){
        addRawModule(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs, mandatory));
    }

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs, int priority, boolean mandatory){
        addRawModule(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs, priority, mandatory));
    }

    public void addRawModule(RawAddonModule rawModule){
        this.rawAddonModules.add(rawModule);
    }

    List<RawAddonModule> getRawAddonModules() {
        return this.rawAddonModules;
    }

    public abstract String getModId();
}
