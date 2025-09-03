package moffy.addonapi;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public interface AddonModule {
    default void init(FMLJavaModLoadingContext context){

    }

    @OnlyIn(Dist.CLIENT)
    default void initClient(FMLJavaModLoadingContext context){

    }

    default void setup(FMLCommonSetupEvent event){
       
    }

    default void enqueueIMC(InterModEnqueueEvent event)
    {

    }

    default void processIMC(InterModProcessEvent event)
    {
        
    }

    @OnlyIn(Dist.CLIENT)
    default void clientSetup(FMLClientSetupEvent event)
    {
        
    }
}
