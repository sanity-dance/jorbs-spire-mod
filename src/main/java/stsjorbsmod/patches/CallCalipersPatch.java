package stsjorbsmod.patches;

import basemod.devcommands.unlock.Unlock;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import javassist.CtBehavior;
import stsjorbsmod.JorbsMod;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class CallCalipersPatch {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(JorbsMod.makeID("CallCalipers")).TEXT;
    private static boolean calipersWouldBeGoodHereHasBeenCalled = false;

    @SpirePostfixPatch
    public static void Postfix(AbstractCreature __this) {
        if (!calipersWouldBeGoodHereHasBeenCalled && __this.currentBlock >= 80 && __this.isPlayer) {
            calipersWouldBeGoodHereHasBeenCalled = true;
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
    }
}




