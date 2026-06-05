package moffy.addonapi.modules;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;

import javax.annotation.Nullable;

public interface CommonAddonModule {
    default void init(IEventBus eventBus, ModContainer container){

    }

    default void setup(FMLCommonSetupEvent event){

    }

    default void enqueueIMC(InterModEnqueueEvent event)
    {

    }

    default void processIMC(InterModProcessEvent event)
    {

    }

    @Nullable
    default Dist getDist(){
        return null;
    }
}
