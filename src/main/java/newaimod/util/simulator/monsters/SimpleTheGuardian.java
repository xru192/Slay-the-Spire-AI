package newaimod.util.simulator.monsters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.ModeShiftPower;
import com.megacrit.cardcrawl.powers.SharpHidePower;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.cards.AbstractSimpleCard;

public class SimpleTheGuardian extends SimpleMonster {

    public enum MODE {
        OFFENSIVE,          // if dealt enough damage this turn, will switch to PREDEFENSIVE
        PREDEFENSIVE,       // will switch to DEFENSIVE next turn
        DEFENSIVE           // has sharp hide active
    }

    private final int DEFENSIVE_BLOCK = 20;

    private MODE mode;
    private int modeShiftAmount = -1;    // amount to switch out of OFFENSIVE mode, -1 if not in OFFENSIVE mode
    private int modeShiftThreshold = -1; // health at which this monster switches out of OFFENSIVE mode
    private int sharpHide = -1;  // -1 if inactive

    public SimpleTheGuardian(TheGuardian monster) {
        super(monster);
        if (monster.hasPower(ModeShiftPower.POWER_ID)) {
            mode = MODE.OFFENSIVE;
            modeShiftAmount = monster.getPower(ModeShiftPower.POWER_ID).amount;
            modeShiftThreshold = health - modeShiftAmount;
        } else if (monster.hasPower(SharpHidePower.POWER_ID)) {
            mode = MODE.DEFENSIVE;
            sharpHide = monster.getPower(SharpHidePower.POWER_ID).amount;
        } else {
            mode = MODE.PREDEFENSIVE;
        }
    }

    SimpleTheGuardian(SimpleTheGuardian m, CombatSimulator simulator) {
        super(m, simulator);
        this.mode = m.mode;
        this.modeShiftAmount = m.modeShiftAmount;
        this.sharpHide = m.sharpHide;
    }

    @Override
    public SimpleMonster copy(CombatSimulator simulator) {
        return new SimpleTheGuardian(this, simulator);
    }

    @Override
    protected void onLoseHealth() {
        if (mode == MODE.OFFENSIVE) {
            if (health <= modeShiftThreshold) {
                mode = MODE.PREDEFENSIVE;
                intent = AbstractMonster.Intent.BUFF;
                intentBaseDamage = -1;
                intentHits = -1;
                block = 20;
            }
        }
    }

    @Override
    protected void onUseCard(AbstractSimpleCard card) {
        if (mode == MODE.DEFENSIVE && card.type == AbstractCard.CardType.ATTACK) {
            simulator.player.takeDamage(sharpHide);
        }
    }

    @Override
    public String toString() {
        return "TheGuardian{" +
                "mode=" + mode +
                "health=" + health +
                ", block=" + block +
                ", strength=" + strength +
                ", intent=" + intent +
                ", intentHits=" + intentHits +
                '}';
    }
}
