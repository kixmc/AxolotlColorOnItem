package com.kixmc.bab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Axolotl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetterAxoBuckets extends JavaPlugin implements Listener {

    private static final char COLOR_CHAR = ChatColor.COLOR_CHAR;
    private static final Pattern hexPattern = Pattern.compile("\\&#([A-Fa-f0-9]{6})");

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        set(e.getItem().getItemStack());
    }

    @EventHandler
    public void onAxoCatch(PlayerBucketEntityEvent e) {
        set(e.getEntityBucket());
    }

    public void set(ItemStack is) {
        if (is.getType() != Material.AXOLOTL_BUCKET) return;

        AxolotlBucketMeta abm = (AxolotlBucketMeta) is.getItemMeta();
        if(abm == null) return;
        if (!abm.hasVariant()) return;

        ItemMeta im = is.getItemMeta();
        im.setLore(Collections.singletonList(colorize(translate(abm.getVariant()))));
        is.setItemMeta(im);
    }

    public String translate(Axolotl.Variant axo) {
        switch (axo) {
            case BLUE:
                return "&f&lRAREST &#b5b7ffBlue";
            case CYAN:
                return "&#b6ddfaCyan mix";
            case GOLD:
                return "&#ffd91bYellow/gold mix";
            case LUCY:
                return "&#ff72e1Pink";
            case WILD:
                return "&#cd9b72Natural brown";
        }
        ;
        return "";
    }

    public static String colorize(String input) {
        try {
            input = ChatColor.translateAlternateColorCodes('&', input);
            if (Bukkit.getVersion().contains("1.20") || Bukkit.getVersion().contains("1.19") || Bukkit.getVersion().contains("1.18") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.16")) input = translateHexColorCodes(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input;
    }

    public static String translateHexColorCodes(String message) {
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer,
                    COLOR_CHAR + "x"
                            + COLOR_CHAR + group.charAt(0)
                            + COLOR_CHAR + group.charAt(1)
                            + COLOR_CHAR + group.charAt(2)
                            + COLOR_CHAR + group.charAt(3)
                            + COLOR_CHAR + group.charAt(4)
                            + COLOR_CHAR + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

}