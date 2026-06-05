package moffy.addonapi.modules;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public interface ClientAddonModule extends AddonModule{

    default void initClient(IEventBus eventBus, ModContainer container){

    }

    default void clientSetup(FMLClientSetupEvent event)
    {

    }

    default Dist getDist(){
        return Dist.CLIENT;
    }
}
