package CardAugments.cardmods.rare;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class JankMod extends AbstractAugment {
    public static final String ID = CardAugmentsMod.makeID(JankMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    boolean modMagic;

    @Override
    public void onInitialApplication(AbstractCard card) {
        boolean hasDamage = card.baseDamage > 0;
        boolean hasBlock = card.baseBlock > 0;
        int d = card.baseDamage;
        int b = card.baseBlock;
        int m = card.baseMagicNumber;
        if (hasDamage && hasBlock) {
            card.baseDamage = m;
            card.baseBlock = d;
            card.baseMagicNumber = b;
            card.magicNumber = card.baseMagicNumber;
        } else if (hasDamage) {
            card.baseDamage = m;
            card.baseMagicNumber = d;
            card.magicNumber = card.baseMagicNumber;
        } else {
            card.baseBlock = m;
            card.baseMagicNumber = b;
            card.magicNumber = card.baseMagicNumber;
        }
    }

    @Override
    public boolean validCard(AbstractCard card) {
        return (card.baseDamage > 0 || card.baseBlock > 0) && cardCheck(card, c -> doesntDowngradeMagic() && c.baseMagicNumber > 0);
    }

    @Override
    public String getPrefix() {
        return TEXT[0];
    }

    @Override
    public String getSuffix() {
        return TEXT[1];
    }

    @Override
    public AugmentRarity getModRarity() {
        return AugmentRarity.RARE;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new JankMod();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
