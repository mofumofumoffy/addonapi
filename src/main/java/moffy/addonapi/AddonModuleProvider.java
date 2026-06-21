package moffy.addonapi;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class AddonModuleProvider {
    private final List<RawAddonModule> rawAddonModules = new ArrayList<>();
    private final IEventBus eventBus;
    private final ModContainer container;

    public AddonModuleProvider(IEventBus eventBus, ModContainer container){
        this.eventBus = eventBus;
        this.container = container;
    }

    public IEventBus getEventBus() {
        return eventBus;
    }

    public ModContainer getContainer() {
        return container;
    }

    public abstract void registerRawModules();

    public void addRawModule(RawAddonModule rawModule){
        this.rawAddonModules.add(rawModule);
    }

    public List<RawAddonModule> getRawAddonModules() {
        return this.rawAddonModules;
    }
}