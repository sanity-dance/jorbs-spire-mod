package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.powers.SnappedPower;


// At the end of turn 7 deal 5 damage to all enemies and 2 damage to yourself for every clarity bonus you have. You cannot be affected by memories or clarities for the remainder of the fight.
public class SnapAction extends AbstractGameAction {
    private final int ENEMY_DAMAGE_PER_CLARITY = 6;
    private final int PLAYER_DAMAGE_PER_CLARITY = 3;

    public SnapAction(AbstractCreature target) {
        this.target = target;
    }

    public void update() {
        if (target.hasPower(SnappedPower.POWER_ID)) {
            isDone = true;
            return;
        }

        int numClarities = MemoryManager.forPlayer(target).countCurrentClarities();
        int enemyDamage = ENEMY_DAMAGE_PER_CLARITY * numClarities;
        int targetDamage = PLAYER_DAMAGE_PER_CLARITY * numClarities;

        // addToTop is important for Trauma effect ordering
        // Note that the group of addToTops actually executes in reverse order
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(target, (AbstractCreature)null, new SnappedPower(target)));
        AbstractDungeon.actionManager.addToTop(
                new LoseHPAction(target, target, targetDamage, AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToTop(
                new DamageAllEnemiesAction(target, DamageInfo.createDamageMatrix(enemyDamage, true), DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_LIGHT));

        AbstractDungeon.actionManager.addToTop(new VFXAction(new BorderFlashEffect(Color.DARK_GRAY.cpy())));

        CardCrawlGame.sound.playA("MONSTER_SNECKO_GLARE", -0.3F);

        if (target instanceof Wanderer) {
            Wanderer wanderer = (Wanderer) target;
            wanderer.setAnimation(wanderer.postSnapAnimation);
        }

        MemoryManager.forPlayer(target).snap();

        AbstractDungeon.actionManager.addToBottom(new ExhumeCardsAction(SelfExhumeFields.selfExhumeOnSnap::get));

        isDone = true;
    }
}



