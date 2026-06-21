package moffy.addonapi;

import moffy.addonapi.modules.ClientAddonModule;
import moffy.addonapi.modules.CommonAddonModule;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import javax.annotation.Nullable;
import java.util.Optional;

public final class RawAddonModule {
    private ResourceLocation name;
    private final String label;

    @Nullable
    private final Class<? extends CommonAddonModule> commonHandlerClass;

    @Nullable
    private final Class<? extends ClientAddonModule> clientHandlerClass;

    private final String[] requiredModIds;
    private final boolean mandatory;
    private final int priority;

    public RawAddonModule(ResourceLocation name, String label, Class<? extends CommonAddonModule> commonHandlerClass, Class<? extends ClientAddonModule> clientHandlerClass, String[] requiredModIds, int priority, boolean mandatory){
        this.name = name;
        this.label = label;
        this.commonHandlerClass = commonHandlerClass;
        this.clientHandlerClass = clientHandlerClass;
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

    @Nullable
    public Class<? extends CommonAddonModule> getCommonHandlerClass() {
        return commonHandlerClass;
    }

    @Nullable
    public Class<? extends ClientAddonModule> getClientHandlerClass() {
        return clientHandlerClass;
    }

    public String[] getRequiredModIds() {
        return requiredModIds;
    }

    public String getLabel() {
        return label;
    }

    Optional<CommonAddonModule> loadNewCommonModule(){
        if(this.commonHandlerClass != null){
            try{
                CommonAddonModule t = commonHandlerClass.getDeclaredConstructor().newInstance();
                AddonAPI.LOGGER. info("Loaded common module {}", this.getName());
                return Optional.of(t);
            }catch(Exception e){
                AddonAPI.LOGGER.error("Common module {} loading failed", getName().toString());
                AddonAPI.LOGGER.error("reason:", e);
            }
        }
        return Optional.empty();
    }

    Optional<ClientAddonModule> loadNewClientModule(){
        if(this.clientHandlerClass != null){
            try{
                ClientAddonModule t = clientHandlerClass.getDeclaredConstructor().newInstance();
                AddonAPI.LOGGER.info("Loaded client module {}", this.getName());
                return Optional.of(t);
            }catch(Exception e){
                AddonAPI.LOGGER.error("Client module {} loading failed", getName().toString());
                AddonAPI.LOGGER.error("reason:", e);
            }
        }
        return Optional.empty();
    }

    public boolean isModsLoaded() {
        for(String modId: this.requiredModIds){
            if(!ModList.get().isLoaded(modId))return false;
        }
        return true;
    }

    public static Builder builder(ResourceLocation name, String label, String[] requiredModIds){
        return new Builder(name, label, requiredModIds);
    }

    public static final class Builder{
        private final ResourceLocation name;
        private final String label;
        private Class<? extends CommonAddonModule> commonHandlerClass;
        private Class<? extends ClientAddonModule> clientHandlerClass;
        private final String[] requiredModIds;
        private boolean mandatory = false;
        private int priority = 0;

        public Builder(ResourceLocation name, String label, String[] requiredModIds){
            this.name = name;
            this.label = label;
            this.commonHandlerClass = null;
            this.clientHandlerClass = null;
            this.requiredModIds = requiredModIds;
        }

        public Builder setCommonModule(Class<? extends CommonAddonModule> commonModuleClass){
            this.commonHandlerClass = commonModuleClass;
            return this;
        }

        public Builder setClientModule(Class<? extends ClientAddonModule> clientModuleClass){
            this.clientHandlerClass = clientModuleClass;
            return this;
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
            return new RawAddonModule(name, label, commonHandlerClass, clientHandlerClass, requiredModIds, priority, mandatory);
        }
    }
}
