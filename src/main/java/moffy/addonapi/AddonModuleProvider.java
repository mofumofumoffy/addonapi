package moffy.addonapi;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public abstract class AddonModuleProvider {
    private final Set<RawAddonModule> rawAddonModules = new HashSet<>();
    private final FMLJavaModLoadingContext context;

    public AddonModuleProvider(FMLJavaModLoadingContext context){
        this.context = context;
    }

    public FMLJavaModLoadingContext getContext() {
        return context;
    }

    public abstract void registerRawModules();

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs){
        this.rawAddonModules.add(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs));
    }

    public void addRawModule(String name, String label, Class<? extends AddonModule> moduleClass, String[] requiredModIDs, boolean mandatory){
        this.rawAddonModules.add(new RawAddonModule(new ResourceLocation(getModId(), name), label, moduleClass, requiredModIDs));
    }

    Set<RawAddonModule> getRawAddonModules() {
        return this.rawAddonModules;
    }

    public abstract String getModId();
}
