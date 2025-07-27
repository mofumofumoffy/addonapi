package moffy.addonapi;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AddonAPI.MODID)
public class AddonAPI {
    public static final String MODID = "addonapi";

    public AddonAPI(){
        this(FMLJavaModLoadingContext.get());
    }

    public AddonAPI(FMLJavaModLoadingContext context){
        context.getModEventBus().addListener(this::setup);
        context.getModEventBus().addListener(this::enqueueIMC);
        context.getModEventBus().addListener(this::processIMC);
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

    @OnlyIn(Dist.CLIENT)    
    private void clientSetup(final FMLClientSetupEvent event){
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules()){
            module.clientSetup(event);
        }
    }
}
