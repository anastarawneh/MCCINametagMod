package me.anastarawneh.mccinametagmod.mixin;

import me.anastarawneh.mccinametagmod.util.UnicodeChars;
import net.minecraft.client.font.BitmapFont;
import net.minecraft.client.font.Font;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BitmapFont.Loader.class)
public class BitmapFontLoaderMixin {
    @Shadow @Final private Identifier file;

    @Shadow @Final private int[][] codepointGrid;

    @Inject(method = "load", at = @At("HEAD"))
    public void load(ResourceManager resourceManager, CallbackInfoReturnable<Font> cir) {
        StringBuilder builder = new StringBuilder();
        for (int point : codepointGrid[0]) { builder.appendCodePoint(point); }
        String character = builder.toString();
        if (!Objects.equals(file.getNamespace(), "mcc")) return;

        switch (file.getPath()) {
            case "_fonts/medals.png" -> UnicodeChars.MedalUnicode = !Objects.equals(UnicodeChars.MedalUnicode, "") ? character : UnicodeChars.MedalUnicode;
            case "_fonts/health_bar/black_border/2_0.png" -> UnicodeChars.HealthBar2_0 = !Objects.equals(UnicodeChars.HealthBar2_0, "") ? character : UnicodeChars.HealthBar2_0;
            case "_fonts/health_bar/black_border/1_5.png" -> UnicodeChars.HealthBar1_5 = !Objects.equals(UnicodeChars.HealthBar1_5, "") ? character : UnicodeChars.HealthBar1_5;
            case "_fonts/health_bar/black_border/1_0.png" -> UnicodeChars.HealthBar1_0 = !Objects.equals(UnicodeChars.HealthBar1_0, "") ? character : UnicodeChars.HealthBar1_0;
            case "_fonts/health_bar/black_border/0_5.png" -> UnicodeChars.HealthBar0_5 = !Objects.equals(UnicodeChars.HealthBar0_5, "") ? character : UnicodeChars.HealthBar0_5;
            case "_fonts/health_bar/black_border/0_0.png" -> UnicodeChars.HealthBar0_0 = !Objects.equals(UnicodeChars.HealthBar0_0, "") ? character : UnicodeChars.HealthBar0_0;
            case "_fonts/health_bar/black_border/end_number/8-0.png" -> UnicodeChars.HealthNum8_0 = !Objects.equals(UnicodeChars.HealthNum8_0, "") ? character : UnicodeChars.HealthNum8_0;
            case "_fonts/health_bar/black_border/end_number/7-5.png" -> UnicodeChars.HealthNum7_5 = !Objects.equals(UnicodeChars.HealthNum7_5, "") ? character : UnicodeChars.HealthNum7_5;
            case "_fonts/health_bar/black_border/end_number/7-0.png" -> UnicodeChars.HealthNum7_0 = !Objects.equals(UnicodeChars.HealthNum7_0, "") ? character : UnicodeChars.HealthNum7_0;
            case "_fonts/health_bar/black_border/end_number/6-5.png" -> UnicodeChars.HealthNum6_5 = !Objects.equals(UnicodeChars.HealthNum6_5, "") ? character : UnicodeChars.HealthNum6_5;
            case "_fonts/health_bar/black_border/end_number/6-0.png" -> UnicodeChars.HealthNum6_0 = !Objects.equals(UnicodeChars.HealthNum6_0, "") ? character : UnicodeChars.HealthNum6_0;
            case "_fonts/health_bar/black_border/end_number/5-5.png" -> UnicodeChars.HealthNum5_5 = !Objects.equals(UnicodeChars.HealthNum5_5, "") ? character : UnicodeChars.HealthNum5_5;
            case "_fonts/health_bar/black_border/end_number/5-0.png" -> UnicodeChars.HealthNum5_0 = !Objects.equals(UnicodeChars.HealthNum5_0, "") ? character : UnicodeChars.HealthNum5_0;
            case "_fonts/health_bar/black_border/end_number/4-5.png" -> UnicodeChars.HealthNum4_5 = !Objects.equals(UnicodeChars.HealthNum4_5, "") ? character : UnicodeChars.HealthNum4_5;
            case "_fonts/health_bar/black_border/end_number/4-0.png" -> UnicodeChars.HealthNum4_0 = !Objects.equals(UnicodeChars.HealthNum4_0, "") ? character : UnicodeChars.HealthNum4_0;
            case "_fonts/health_bar/black_border/end_number/3-5.png" -> UnicodeChars.HealthNum3_5 = !Objects.equals(UnicodeChars.HealthNum3_5, "") ? character : UnicodeChars.HealthNum3_5;
            case "_fonts/health_bar/black_border/end_number/3-0.png" -> UnicodeChars.HealthNum3_0 = !Objects.equals(UnicodeChars.HealthNum3_0, "") ? character : UnicodeChars.HealthNum3_0;
            case "_fonts/health_bar/black_border/end_number/2-5.png" -> UnicodeChars.HealthNum2_5 = !Objects.equals(UnicodeChars.HealthNum2_5, "") ? character : UnicodeChars.HealthNum2_5;
            case "_fonts/health_bar/black_border/end_number/2-0.png" -> UnicodeChars.HealthNum2_0 = !Objects.equals(UnicodeChars.HealthNum2_0, "") ? character : UnicodeChars.HealthNum2_0;
            case "_fonts/health_bar/black_border/end_number/1-5.png" -> UnicodeChars.HealthNum1_5 = !Objects.equals(UnicodeChars.HealthNum1_5, "") ? character : UnicodeChars.HealthNum1_5;
            case "_fonts/health_bar/black_border/end_number/1-0.png" -> UnicodeChars.HealthNum1_0 = !Objects.equals(UnicodeChars.HealthNum1_0, "") ? character : UnicodeChars.HealthNum1_0;
            case "_fonts/health_bar/black_border/end_number/0-5.png" -> UnicodeChars.HealthNum0_5 = !Objects.equals(UnicodeChars.HealthNum0_5, "") ? character : UnicodeChars.HealthNum0_5;
            case "_fonts/health_bar/black_border/disabled_2_0/0_0.png" -> UnicodeChars.DisabledHealthBar2_0 = !Objects.equals(UnicodeChars.DisabledHealthBar2_0, "") ? character : UnicodeChars.DisabledHealthBar2_0;
            case "_fonts/health_bar/black_border/disabled_1_0/1_0.png" -> UnicodeChars.DisabledHealthBar1_0 = !Objects.equals(UnicodeChars.DisabledHealthBar1_0, "") ? character : UnicodeChars.DisabledHealthBar1_0;
            case "_fonts/health_bar/black_border/disabled_1_0/0_0.png" -> UnicodeChars.DisabledHealthBar1_0Hit = !Objects.equals(UnicodeChars.DisabledHealthBar1_0Hit, "") ? character : UnicodeChars.DisabledHealthBar1_0Hit;
            case "_fonts/team_flag_big.png" -> UnicodeChars.TeamFlagBig = !Objects.equals(UnicodeChars.TeamFlagBig, "") ? character : UnicodeChars.TeamFlagBig;
            case "_fonts/team_flag.png" -> UnicodeChars.TeamFlag = !Objects.equals(UnicodeChars.TeamFlag, "") ? character : UnicodeChars.TeamFlag;
            case "_fonts/ranks_long/rank_1.png" -> UnicodeChars.ChampRank = !Objects.equals(UnicodeChars.ChampRank, "") ? character : UnicodeChars.ChampRank;
            case "_fonts/ranks_long/rank_2.png" -> UnicodeChars.GrandChampRank = !Objects.equals(UnicodeChars.GrandChampRank, "") ? character : UnicodeChars.GrandChampRank;
            case "_fonts/ranks_long/rank_3.png" -> UnicodeChars.GrandChampRoyaleRank = !Objects.equals(UnicodeChars.GrandChampRoyaleRank, "") ? character : UnicodeChars.GrandChampRoyaleRank;
            case "_fonts/ranks_long/creator.png" -> UnicodeChars.CreatorRank = !Objects.equals(UnicodeChars.CreatorRank, "") ? character : UnicodeChars.CreatorRank;
            case "_fonts/ranks_long/contestant.png" -> UnicodeChars.ContestantRank = !Objects.equals(UnicodeChars.ContestantRank, "") ? character : UnicodeChars.ContestantRank;
            case "_fonts/ranks_long/mod.png" -> UnicodeChars.ModRank = !Objects.equals(UnicodeChars.ModRank, "") ? character : UnicodeChars.ModRank;
            case "_fonts/ranks_long/noxcrew.png" -> UnicodeChars.NoxcrewRank = !Objects.equals(UnicodeChars.NoxcrewRank, "") ? character : UnicodeChars.NoxcrewRank;
            case "_fonts/ranks_short/basic.png" -> UnicodeChars.BasicIcon = !Objects.equals(UnicodeChars.BasicIcon, "") ? character : UnicodeChars.BasicIcon;
            case "_fonts/ranks_short/rank_1.png" -> UnicodeChars.ChampIcon = !Objects.equals(UnicodeChars.ChampIcon, "") ? character : UnicodeChars.ChampIcon;
            case "_fonts/ranks_short/rank_2.png" -> UnicodeChars.GrandChampIcon = !Objects.equals(UnicodeChars.GrandChampIcon, "") ? character : UnicodeChars.GrandChampIcon;
            case "_fonts/ranks_short/rank_3.png" -> UnicodeChars.GrandChampRoyaleIcon = !Objects.equals(UnicodeChars.GrandChampRoyaleIcon, "") ? character : UnicodeChars.GrandChampRoyaleIcon;
            case "_fonts/ranks_short/vip.png" -> UnicodeChars.CreatorIcon = !Objects.equals(UnicodeChars.CreatorIcon, "") ? character : UnicodeChars.CreatorIcon;
            case "_fonts/ranks_short/contestant.png" -> UnicodeChars.ContestantIcon = !Objects.equals(UnicodeChars.ContestantIcon, "") ? character : UnicodeChars.ContestantIcon;
            case "_fonts/ranks_short/mod.png" -> UnicodeChars.ModIcon = !Objects.equals(UnicodeChars.ModIcon, "") ? character : UnicodeChars.ModIcon;
            case "_fonts/ranks_short/noxcrew.png" -> UnicodeChars.NoxcrewIcon = !Objects.equals(UnicodeChars.NoxcrewIcon, "") ? character : UnicodeChars.NoxcrewIcon;
            case "_fonts/fishing_level/0.png" -> UnicodeChars.FishingLevel0 = !Objects.equals(UnicodeChars.FishingLevel0, "") ? character : UnicodeChars.FishingLevel0;
            case "_fonts/fishing_level/0_big.png" -> UnicodeChars.FishingLevel0Big = !Objects.equals(UnicodeChars.FishingLevel0Big, "") ? character : UnicodeChars.FishingLevel0Big;
            case "_fonts/fishing_level/1.png" -> UnicodeChars.FishingLevel1 = !Objects.equals(UnicodeChars.FishingLevel1, "") ? character : UnicodeChars.FishingLevel1;
            case "_fonts/fishing_level/1_big.png" -> UnicodeChars.FishingLevel1Big = !Objects.equals(UnicodeChars.FishingLevel1Big, "") ? character : UnicodeChars.FishingLevel1Big;
            case "_fonts/fishing_level/2.png" -> UnicodeChars.FishingLevel2 = !Objects.equals(UnicodeChars.FishingLevel2, "") ? character : UnicodeChars.FishingLevel2;
            case "_fonts/fishing_level/2_big.png" -> UnicodeChars.FishingLevel2Big = !Objects.equals(UnicodeChars.FishingLevel2Big, "") ? character : UnicodeChars.FishingLevel2Big;
            case "_fonts/fishing_level/3.png" -> UnicodeChars.FishingLevel3 = !Objects.equals(UnicodeChars.FishingLevel3, "") ? character : UnicodeChars.FishingLevel3;
            case "_fonts/fishing_level/3_big.png" -> UnicodeChars.FishingLevel3Big = !Objects.equals(UnicodeChars.FishingLevel3Big, "") ? character : UnicodeChars.FishingLevel3Big;
            case "_fonts/fishing_level/4.png" -> UnicodeChars.FishingLevel4 = !Objects.equals(UnicodeChars.FishingLevel4, "") ? character : UnicodeChars.FishingLevel4;
            case "_fonts/fishing_level/4_big.png" -> UnicodeChars.FishingLevel4Big = !Objects.equals(UnicodeChars.FishingLevel4Big, "") ? character : UnicodeChars.FishingLevel4Big;
            case "_fonts/fishing_level/5.png" -> UnicodeChars.FishingLevel5 = !Objects.equals(UnicodeChars.FishingLevel5, "") ? character : UnicodeChars.FishingLevel5;
            case "_fonts/fishing_level/5_big.png" -> UnicodeChars.FishingLevel5Big = !Objects.equals(UnicodeChars.FishingLevel5Big, "") ? character : UnicodeChars.FishingLevel5Big;
            case "_fonts/fishing_level/6.png" -> UnicodeChars.FishingLevel6 = !Objects.equals(UnicodeChars.FishingLevel6, "") ? character : UnicodeChars.FishingLevel6;
            case "_fonts/fishing_level/6_big.png" -> UnicodeChars.FishingLevel6Big = !Objects.equals(UnicodeChars.FishingLevel6Big, "") ? character : UnicodeChars.FishingLevel6Big;
            case "_fonts/fishing_level/7.png" -> UnicodeChars.FishingLevel7 = !Objects.equals(UnicodeChars.FishingLevel7, "") ? character : UnicodeChars.FishingLevel7;
            case "_fonts/fishing_level/7_big.png" -> UnicodeChars.FishingLevel7Big = !Objects.equals(UnicodeChars.FishingLevel7Big, "") ? character : UnicodeChars.FishingLevel7Big;
            case "_fonts/fishing_level/8.png" -> UnicodeChars.FishingLevel8 = !Objects.equals(UnicodeChars.FishingLevel8, "") ? character : UnicodeChars.FishingLevel8;
            case "_fonts/fishing_level/8_big.png" -> UnicodeChars.FishingLevel8Big = !Objects.equals(UnicodeChars.FishingLevel8Big, "") ? character : UnicodeChars.FishingLevel8Big;
            case "_fonts/fishing_level/9.png" -> UnicodeChars.FishingLevel9 = !Objects.equals(UnicodeChars.FishingLevel9, "") ? character : UnicodeChars.FishingLevel9;
            case "_fonts/fishing_level/9_big.png" -> UnicodeChars.FishingLevel9Big = !Objects.equals(UnicodeChars.FishingLevel9Big, "") ? character : UnicodeChars.FishingLevel9Big;
            case "_fonts/fishing_level/10.png" -> UnicodeChars.FishingLevel10 = !Objects.equals(UnicodeChars.FishingLevel10, "") ? character : UnicodeChars.FishingLevel10;
            case "_fonts/fishing_level/10_big.png" -> UnicodeChars.FishingLevel10Big = !Objects.equals(UnicodeChars.FishingLevel10Big, "") ? character : UnicodeChars.FishingLevel10Big;
        }
    }
}
