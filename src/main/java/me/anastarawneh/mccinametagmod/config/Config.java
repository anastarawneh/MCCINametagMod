package me.anastarawneh.mccinametagmod.config;

import me.anastarawneh.mccinametagmod.MCCINametagMod;
import me.shedaniel.autoconfig.ConfigData;

@me.shedaniel.autoconfig.annotation.Config(name = MCCINametagMod.MODID)
public class Config implements ConfigData {
    public boolean enabled = true;
}
