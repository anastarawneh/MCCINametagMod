package me.anastarawneh.mccinametagmod.config;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;

@me.shedaniel.autoconfig.annotation.Config(name = MCCINametagMod.MODID)
public class Config implements ConfigData {
    public boolean enabled = true;
    public boolean showInInventory = false;
    @Excluded
    public int factionLevel = -1;
}
