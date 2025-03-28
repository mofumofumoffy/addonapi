package moffy.addonapi;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;

public abstract class AddonModuleProvider {
    private Set<RawAddonModule> rawAddonModules = new HashSet<>();

    public abstract void registerRawModules();

    public void addRawModule(ResourceLocation name, String label, Supplier<? extends AddonModule> moduleClassSupplier, String[] requiredModIDs){
        this.rawAddonModules.add(new RawAddonModule(name, label, moduleClassSupplier, requiredModIDs));
    }

    public void addRawModule(ResourceLocation name, String label, Supplier<? extends AddonModule> moduleClassSupplier, String[] requiredModIDs, boolean mandatory){
        this.rawAddonModules.add(new RawAddonModule(name, label, moduleClassSupplier, requiredModIDs, mandatory));
    }

    Set<RawAddonModule> getRawAddonModules() {
        return this.rawAddonModules;
    }

    public abstract String getModId();
}
