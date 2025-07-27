package moffy.addonapi;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

public abstract class AddonModule {

    public AddonModule(){
        init();
        DistExecutor.unsafeRunWhenOn(
            Dist.CLIENT,
            () ->
                () -> {
                    initClient();
                }
        );
    }
    
    public void init(){

    }

    @OnlyIn(Dist.CLIENT)
    public void initClient(){

    }

    public void setup(FMLCommonSetupEvent event){
       
    }

    public void enqueueIMC(InterModEnqueueEvent event)
    {

    }

    public void processIMC(InterModProcessEvent event)
    {
        
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        
    }
}
