package moffy.addonapi;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import moffy.addonapi.modules.CommonAddonModule;
import moffy.addonapi.recipe.ModsAvailableCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import java.util.Map;
import java.util.function.Supplier;

@Mod(AddonAPI.MODID)
public class AddonAPI {
    public static final String MODID = "addonapi";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, AddonAPI.MODID);

    public static final Supplier<MapCodec<ModsAvailableCondition>> CONFIG_ENABLED =
            CONDITION_CODECS.register("mods_available", () -> ModsAvailableCondition.CODEC);

    public AddonAPI(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::processIMC);
        modEventBus.addListener(this::gatherData);

        CONDITION_CODECS.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        for(CommonAddonModule module : AddonModuleRegistry.INSTANCE.getLoadedCommonModules().values()){
            module.setup(event);
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        for(CommonAddonModule module : AddonModuleRegistry.INSTANCE.getLoadedCommonModules().values()){
            module.enqueueIMC(event);
        }
    }

    private void processIMC(final InterModProcessEvent event)
    {
        for(CommonAddonModule module : AddonModuleRegistry.INSTANCE.getLoadedCommonModules().values()){
            module.processIMC(event);
        }
    }

    private void gatherData(GatherDataEvent event){
    }

    public static boolean isModuleAvailable(ResourceLocation moduleName){
        Map<ResourceLocation, CommonAddonModule> loadedModules = AddonModuleRegistry.INSTANCE.getLoadedCommonModules();
        Map<ResourceLocation, ModConfigSpec.BooleanValue> compatSettings = AddonModuleRegistry.INSTANCE.getCompatSettings();

        for(var loadedModule : loadedModules.entrySet()){
            if(loadedModule.getKey().equals(moduleName)){
                ModConfigSpec.BooleanValue compatConfig = compatSettings.get(moduleName);
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
