package moffy.addonapi;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

public class RawAddonModule {
    private ResourceLocation name;
    private String label;
    private Class<? extends AddonModule> handlerClass;
    private String[] requiredModIds;
    private boolean mandatory;


    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds){
        this(name, label, handlerClass, requiredModIds, false);
    }

    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds, boolean mandatory){
        this.name = name;
        this.label = label;
        this.handlerClass = handlerClass;
        this.requiredModIds = requiredModIds;
        this.mandatory = mandatory;
    }

    public boolean isMandatory(){
        return this.mandatory;
    }

    public ResourceLocation getName() {
        return name;
    }



    public void setName(ResourceLocation name) {
        this.name = name;
    }

    public Class<? extends AddonModule> getHandlerClass() {
        return handlerClass;
    }



    public void setHandlerClass(Class<? extends AddonModule> handlerClass) {
        this.handlerClass = handlerClass;
    }



    public String[] getRequiredModIds() {
        return requiredModIds;
    }

    public void setRequiredModIds(String[] requiredModIds) {
        this.requiredModIds = requiredModIds;
    }

    LazyOptional<AddonModule> loadNewModule(){
        try{
            AddonModule t = handlerClass.getDeclaredConstructor().newInstance();
            return LazyOptional.of(()->t);
        }catch(Exception e){
            AddonAPI.LOGGER.error("Module {} loading failed", getName().toString());
            AddonAPI.LOGGER.error("reason:", e);
            return LazyOptional.empty();
        }
    }

    boolean isModsLoaded() {
        for(String modId: this.requiredModIds){
            if(!ModList.get().isLoaded(modId))return false;
        }
        return true;
    }

    public String getLabel() {
        return label;
    }
}
