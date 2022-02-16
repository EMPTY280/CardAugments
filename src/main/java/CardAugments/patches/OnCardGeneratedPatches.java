package CardAugments.patches;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OnCardGeneratedPatches {
    @SpirePatch2(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class ModifySpawnedCardsPatch {
        @SpirePostfixPatch
        public static void patch(ArrayList<AbstractCard> __result) {
            for (AbstractCard c : __result) {
                rollCardAugment(c);
            }
        }
    }

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "openConfirmationGrid")
    public static class ModifyConfirmScreenCards {
        @SpirePostfixPatch
        public static void patch(CardGroup group) {
            for (AbstractCard c : group.group) {
                rollCardAugment(c);
            }
        }
    }

    @SpirePatch2(clz = GremlinMatchGame.class, method = "initializeCards")
    public static class ModifyMatchGameCards {
        @SpireInsertPatch(locator = Locator.class, localvars = "retVal")
        public static void patch(ArrayList<AbstractCard> retVal) {
            for (AbstractCard c : retVal) {
                rollCardAugment(c);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "getStartCardForEvent");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = NeowReward.class, method = "getRewardCards")
    public static class ModifyNeowRewardCardsPatch {
        @SpirePostfixPatch
        public static void patch(ArrayList<AbstractCard> __result) {
            for (AbstractCard c : __result) {
                rollCardAugment(c);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "initializeStarterDeck")
    public static class ModifyStarterCards {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __instance) {
            if (CardAugmentsMod.modifyStarters) {
                for (AbstractCard c : __instance.masterDeck.group) {
                    rollCardAugment(c);
                }
            }
        }
    }

    public static void rollCardAugment(AbstractCard c) {
        if (AbstractDungeon.miscRng.random(99) < CardAugmentsMod.modProbabilityPercent) {
            applyWeightedCardMod(c, rollRarity());
        }
    }

    public static AbstractAugment.AugmentRarity rollRarity() {
        int roll = AbstractDungeon.miscRng.random(CardAugmentsMod.commonWeight + CardAugmentsMod.uncommonWeight + CardAugmentsMod.rareWeight - 1);
        if (roll < CardAugmentsMod.commonWeight) {
            return AbstractAugment.AugmentRarity.COMMON;
        } else if (roll < CardAugmentsMod.commonWeight + CardAugmentsMod.uncommonWeight) {
            return AbstractAugment.AugmentRarity.UNCOMMON;
        } else {
            return AbstractAugment.AugmentRarity.RARE;
        }
    }

    public static void applyWeightedCardMod(AbstractCard c, AbstractAugment.AugmentRarity rarity) {
        ArrayList<AbstractAugment> validMods = new ArrayList<>();
        switch (rarity) {
            case COMMON:
                validMods.addAll(CardAugmentsMod.commonMods.stream().filter(m -> m.shouldApply(c)).collect(Collectors.toCollection(ArrayList::new)));
                break;
            case UNCOMMON:
                validMods.addAll(CardAugmentsMod.uncommonMods.stream().filter(m -> m.shouldApply(c)).collect(Collectors.toCollection(ArrayList::new)));
                break;
            case RARE:
                validMods.addAll(CardAugmentsMod.rareMods.stream().filter(m -> m.shouldApply(c)).collect(Collectors.toCollection(ArrayList::new)));
                break;
        }
        if (!validMods.isEmpty()) {
            CardModifierManager.addModifier(c, validMods.get(AbstractDungeon.miscRng.random(validMods.size()-1)).makeCopy());
        }
    }
}