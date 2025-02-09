package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.util.ReflectionUtils;

public class CardBannerImageRarityPatch {
    public static AbstractCard.CardRarity getBannerRarity(AbstractCard c) {
        return (c instanceof CustomJorbsModCard) ? ((CustomJorbsModCard)c).bannerImageRarity : c.rarity;
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderBannerImage"
    )
    public static class AbstractCard_renderBannerImage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractCard.class.getName()) && fieldAccess.getFieldName().equals("rarity")) {
                        fieldAccess.replace(String.format("{ $_ = %1$s.getBannerRarity($0); }", CardBannerImageRarityPatch.class.getName()));
                    }
                }
            };
        }
    }
}