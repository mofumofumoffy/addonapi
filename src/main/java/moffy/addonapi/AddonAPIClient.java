package moffy.addonapi;

import moffy.addonapi.modules.AddonModule;
import moffy.addonapi.modules.ClientAddonModule;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = AddonAPI.MODID, dist = Dist.CLIENT)
public class AddonAPIClient {
    public AddonAPIClient(IEventBus modEventBus){
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event){
        for(ClientAddonModule module : AddonModuleRegistry.INSTANCE.getLoadedClientModules().values()){
            module.clientSetup(event);
        }
    }
}
