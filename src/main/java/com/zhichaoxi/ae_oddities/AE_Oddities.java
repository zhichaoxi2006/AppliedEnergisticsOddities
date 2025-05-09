package com.zhichaoxi.ae_oddities;

import appeng.api.AECapabilities;
import appeng.api.networking.IInWorldGridNodeHost;
import com.mojang.logging.LogUtils;
import com.zhichaoxi.ae_oddities.blocks.entity.MEStorageExposerBlockEntity;
import com.zhichaoxi.ae_oddities.compat.MekCompat;
import com.zhichaoxi.ae_oddities.init.AEOBlockEntities;
import com.zhichaoxi.ae_oddities.init.AEOBlocks;
import com.zhichaoxi.ae_oddities.init.AEOItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AE_Oddities.MODID)
public class AE_Oddities
{
    public static final String MODID = "ae_oddities";

    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean MekLoaded = false;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> AEO_TAB = CREATIVE_MODE_TABS.register("example", () -> CreativeModeTab.builder()
            //Set the title of the tab. Don't forget to add a translation!
            .title(Component.translatable("itemGroup." + MODID))
            //Set the icon of the tab.
            .icon(() -> new ItemStack(AEOBlocks.ME_STORAGE_EXPOSER.asItem()))
            //Add your items to the tab.
            .displayItems((params, output) -> {
                output.accept(AEOBlocks.ME_STORAGE_EXPOSER);
            })
            .build()
    );

    public AE_Oddities(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        AEOBlockEntities.DR.register(modEventBus);
        AEOBlocks.DR.register(modEventBus);
        AEOItems.DR.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(AE_Oddities::initUpgrades);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void initUpgrades(FMLCommonSetupEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        if (ModList.get().isLoaded("mekanism")) {
            MekLoaded = true;
        }
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents
    {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (var type : AEOBlockEntities.DR.getEntries()) {
                event.registerBlockEntity(
                        AECapabilities.IN_WORLD_GRID_NODE_HOST, type.get(), (be, context) -> (IInWorldGridNodeHost) be);
            }

            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                    AEOBlockEntities.ME_STORAGE_EXPOSER.get(), MEStorageExposerBlockEntity::getItemHandler);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                    AEOBlockEntities.ME_STORAGE_EXPOSER.get(), MEStorageExposerBlockEntity::getFluidHandler);

            if (MekLoaded) {
                MekCompat.registerCapabilities(event);
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
