package CardAugments.cardmods.common;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SurvivorMod extends AbstractAugment {
    public static final String ID = CardAugmentsMod.makeID("SurvivorMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.isEthereal = true;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return block * MAJOR_BUFF;
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return card.cost != -2 && card.baseBlock > 0;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        this.addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return TEXT[0] + cardName + TEXT[1];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (rawDescription.contains(TEXT[4])) {
            return rawDescription.replace(TEXT[4], TEXT[5]);
        }
        return rawDescription + String.format(TEXT[2], 1);
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.COMMON;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SurvivorMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
