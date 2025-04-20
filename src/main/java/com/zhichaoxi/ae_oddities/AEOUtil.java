package com.zhichaoxi.ae_oddities;

import appeng.items.tools.powered.WirelessTerminalItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class AEOUtil {

    public static @Nullable ItemStack findWirelessTerminal(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            Item item = stack.getItem();
            if (item instanceof WirelessTerminalItem) {
                return stack;
            }
        }
        return null;
    }
}
