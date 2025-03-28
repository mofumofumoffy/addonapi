package moffy.addonapi;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;

public class RawAddonModule {
    private ResourceLocation name;
    private String label;
    private Supplier<? extends AddonModule> handlerClassSupplier;
    private String[] requiredModIds;
    private boolean mandatory;


    public RawAddonModule(ResourceLocation name, String label, Supplier<? extends AddonModule> handlerClassSupplier, String[] requiredModIds){
        this(name, label, handlerClassSupplier, requiredModIds, false);
    }

    public RawAddonModule(ResourceLocation name, String label, Supplier<? extends AddonModule> handlerClassSupplier, String[] requiredModIds, boolean mandatory){
        this.name = getCompatLocation(name);
        this.label = label;
        this.handlerClassSupplier = handlerClassSupplier;
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

    public ResourceLocation getCompatLocation(ResourceLocation name){
        return new ResourceLocation(name.getNamespace(), "compat_"+name.getPath());
    }

    public Supplier<? extends AddonModule> getHandlerClassSupplier() {
        return handlerClassSupplier;
    }



    public void setHandlerClassSupplier(Supplier<? extends AddonModule> handlerClassSupplier) {
        this.handlerClassSupplier = handlerClassSupplier;
    }



    public String[] getRequiredModIds() {
        return requiredModIds;
    }

    public void setRequiredModIds(String[] requiredModIds) {
        this.requiredModIds = requiredModIds;
    }

    LazyOptional<AddonModule> loadNewModule(){
        try{
            AddonModule t = handlerClassSupplier.get();
            return LazyOptional.of(()->t);
        }catch(Exception e){
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
