package moffy.addonapi;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

public class RawAddonModule {
    private ResourceLocation name;
    private final String label;
    private Class<? extends AddonModule> handlerClass;
    private String[] requiredModIds;
    private final boolean mandatory;
    private final int priority;


    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds){
        this(name, label, handlerClass, requiredModIds, 0, false);
    }

    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds, int priority){
        this(name, label, handlerClass, requiredModIds, priority, false);
    }

    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds, boolean mandatory){
        this(name, label, handlerClass, requiredModIds, 0, mandatory);
    }

    public RawAddonModule(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds, int priority, boolean mandatory){
        this.name = name;
        this.label = label;
        this.handlerClass = handlerClass;
        this.requiredModIds = requiredModIds;
        this.priority = priority;
        this.mandatory = mandatory;
    }

    public boolean isMandatory(){
        return this.mandatory;
    }

    public ResourceLocation getName() {
        return name;
    }

    public int getPriority() {
        return priority;
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
            AddonAPI.LOGGER. info("Loaded module {}", this.getName());
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

    public static Builder builder(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds){
        return new Builder(name, label, handlerClass, requiredModIds);
    }

    public static final class Builder{
        private final ResourceLocation name;
        private final String label;
        private final Class<? extends AddonModule> handlerClass;
        private final String[] requiredModIds;
        private boolean mandatory = false;
        private int priority = 0;

        public Builder(ResourceLocation name, String label, Class<? extends AddonModule> handlerClass, String[] requiredModIds){
            this.name = name;
            this.label = label;
            this.handlerClass = handlerClass;
            this.requiredModIds = requiredModIds;
        }

        public Builder mandatory(){
            mandatory = true;
            return this;
        }

        public Builder setPriority(int priority){
            this.priority = priority;
            return this;
        }

        public RawAddonModule build(){
            return new RawAddonModule(name, label, handlerClass, requiredModIds, priority, mandatory);
        }
    }
}
