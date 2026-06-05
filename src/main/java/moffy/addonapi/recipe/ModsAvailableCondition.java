package moffy.addonapi.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moffy.addonapi.AddonAPI;
import moffy.addonapi.AddonModuleProvider;
import moffy.addonapi.AddonModuleRegistry;
import moffy.addonapi.RawAddonModule;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Map;
import java.util.Set;

public record ModsAvailableCondition(String requiredRawModule) implements ICondition {
    public static final MapCodec<ModsAvailableCondition> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.STRING
                            .fieldOf("required_raw_module")
                            .forGetter(modsAvailableCondition -> modsAvailableCondition.requiredRawModule.toString())
            ).apply(instance, ModsAvailableCondition::new));


    @Override
    public boolean test(IContext context) {
        ResourceLocation requiredRawModuleLoc = ResourceLocation.parse(requiredRawModule);
        Map<ResourceLocation, ModConfigSpec.BooleanValue> compatSettings = AddonModuleRegistry.INSTANCE.getCompatSettings();

        Set<AddonModuleProvider> providers = AddonModuleRegistry.INSTANCE.getProviders();

        for(AddonModuleProvider provider : providers){
            for(RawAddonModule rawAddonModule : provider.getRawAddonModules()){
                if(rawAddonModule.getName().equals(requiredRawModuleLoc)){
                    ModConfigSpec.BooleanValue isAvailable = compatSettings.get(requiredRawModuleLoc);
                    if(!rawAddonModule.isModsLoaded() || !isAvailable.get()){
                        return false;
                    }
                }
            }
        }


        return true;
    }

    public ResourceLocation getRequiredRawModule() {
        return ResourceLocation.parse(requiredRawModule);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
