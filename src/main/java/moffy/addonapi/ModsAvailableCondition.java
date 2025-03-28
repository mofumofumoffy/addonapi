package moffy.addonapi;

import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class ModsAvailableCondition implements ICondition{

    private final ResourceLocation name;
    private final ResourceLocation requiredRawModule;

    public ModsAvailableCondition(ResourceLocation name, ResourceLocation requiredRawModule){
        this.name = name;
        this.requiredRawModule = requiredRawModule;
    }

    @Override
    public ResourceLocation getID() {
        return name;
    }

    @Override
    public boolean test(IContext context) {
        Map<ResourceLocation, ForgeConfigSpec.BooleanValue>compatSettings = AddonModuleRegistry.INSTANCE.getCompatSettings();

        Set<AddonModuleProvider> providers = AddonModuleRegistry.INSTANCE.getProviders();

        for(AddonModuleProvider provider : providers){
            for(RawAddonModule rawAddonModule : provider.getRawAddonModules()){
                if(rawAddonModule.getName().equals(requiredRawModule)){
                    ForgeConfigSpec.BooleanValue isAvailable = compatSettings.get(requiredRawModule);
                    if(!rawAddonModule.isModsLoaded() || !isAvailable.get()){
                        return false;
                    }
                }
            }
        }
        
        
        return true;
    }
    
    public static class Serializer implements IConditionSerializer<ModsAvailableCondition>{

        private final ResourceLocation name;

        public Serializer(){
            this.name = new ResourceLocation(AddonAPI.MODID, "mods_available");
        }

        @Override
        public void write(JsonObject json, ModsAvailableCondition value) {
            json.addProperty("required_raw_module", value.requiredRawModule.toString());
        }

        @Override
        public ModsAvailableCondition read(JsonObject json) {
            String requiredRawModulePath = json.get("required_raw_module").getAsString();
            return new ModsAvailableCondition(name, new ResourceLocation(requiredRawModulePath));
        }

        @Override
        public ResourceLocation getID() {
            return name;
        }
    }
}
