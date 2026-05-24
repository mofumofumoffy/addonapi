package moffy.addonapi;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

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
        context.getModEventBus().addListener(this::registerRecipeSerializers);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->()->{
            context.getModEventBus().addListener(this::clientSetup);
        });
    }

    private void registerRecipeSerializers(RegisterEvent event)
    {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS))
        {
            CraftingHelper.register(new ModsAvailableCondition.Serializer());
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules().values()){
            module.setup(event);
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules().values()){
            module.enqueueIMC(event);
        }
    }

    private void processIMC(final InterModProcessEvent event)
    {
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules().values()){
            module.processIMC(event);
        }
    }

    private void gatherData(GatherDataEvent event){
    }

    @OnlyIn(Dist.CLIENT)    
    private void clientSetup(final FMLClientSetupEvent event){
        for(AddonModule module : AddonModuleRegistry.INSTANCE.getLoadedModules().values()){
            module.clientSetup(event);
        }
    }

    public static boolean isModuleAvailable(ResourceLocation moduleName){
        Map<ResourceLocation, AddonModule> loadedModules = AddonModuleRegistry.INSTANCE.getLoadedModules();
        Map<ResourceLocation, ForgeConfigSpec.BooleanValue> compatSettings = AddonModuleRegistry.INSTANCE.getCompatSettings();

        for(var loadedModule : loadedModules.entrySet()){
            if(loadedModule.getKey().equals(moduleName)){
                ForgeConfigSpec.BooleanValue compatConfig = compatSettings.get(moduleName);
                if(compatConfig != null){
                    return compatConfig.get();
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
