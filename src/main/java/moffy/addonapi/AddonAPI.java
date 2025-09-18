package moffy.addonapi;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AddonAPI.MODID)
public class AddonAPI {
    public static final String MODID = "addonapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AddonAPI(){
        this(FMLJavaModLoadingContext.get());
    }

    public AddonAPI(FMLJavaModLoadingContext context){
        context.getModEventBus().addListener(this::setup);
        context.getModEventBus().addListener(this::enqueueIMC);
        context.getModEventBus().addListener(this::processIMC);
        context.getModEventBus().addListener(this::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
            context.getModEventBus().addListener(this::clientSetup);
        });
        CraftingHelper.register(new ModsAvailableCondition.Serializer());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules()){
            module.setup(event);
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules()){
            module.enqueueIMC(event);
        }
    }

    private void processIMC(final InterModProcessEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules()){
            module.processIMC(event);
        }
    }

    private void gatherData(GatherDataEvent event){
        //CraftingHelper.register(new ModsAvailableCondition.Serializer());
    }

    @OnlyIn(Dist.CLIENT)    
    private void clientSetup(final FMLClientSetupEvent event){
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules()){
            module.clientSetup(event);
        }
    }
}
