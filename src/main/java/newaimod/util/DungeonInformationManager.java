package newaimod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.SimplePlayer;
import newaimod.util.simulator.monsters.SimpleGremlinNob;
import newaimod.util.simulator.monsters.SimpleLagavulin;
import newaimod.util.simulator.monsters.SimpleSlimeBoss;
import newaimod.util.simulator.monsters.SimpleTheGuardian;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The DungeonInformationManager handles requests for information about the state of the dungeon.
 */
public class DungeonInformationManager {
    public static final Logger logger = LogManager.getLogger(DungeonInformationManager.class.getName());

    private static final DungeonInformationManager instance = new DungeonInformationManager();

    private DungeonInformationManager() {
    }

    public static DungeonInformationManager getInstance() {
        return instance;
    }

    /**
     * Returns a CombatSimulator representing the current state of combat. Requires that the player is in combat.
     *
     * @return the current state of combat
     */
    public CombatSimulator getCurrentState() {
        assert NewAIMod.inBattle;
        CombatSimulator currentState = new CombatSimulator();
        SimplePlayer currentPlayer = currentState.player;

        AbstractPlayer p = AbstractDungeon.player;
        currentPlayer.health = p.currentHealth;
        currentPlayer.block = p.currentBlock;
        for (AbstractCard card : p.hand.group) {
            currentPlayer.hand.add(currentState.convertCard(card));
        }
        currentPlayer.energy = CombatUtils.usableEnergy();
        currentPlayer.strength = CombatUtils.amountOfPower(p, StrengthPower.POWER_ID);
        currentPlayer.dexterity = CombatUtils.amountOfPower(p, DexterityPower.POWER_ID);
        currentPlayer.weakened = p.hasPower(WeakPower.POWER_ID);
        currentPlayer.frail = p.hasPower(FrailPower.POWER_ID);
        currentPlayer.metallicize = CombatUtils.amountOfPower(p, MetallicizePower.POWER_ID);
        currentPlayer.demonForm = CombatUtils.amountOfPower(p, DemonFormPower.POWER_ID);
        currentPlayer.entangled = p.hasPower(EntanglePower.POWER_ID);
        currentPlayer.vulnerable = p.hasPower(VulnerablePower.POWER_ID);
        currentPlayer.noDraw = p.hasPower(NoDrawPower.POWER_ID);
        currentPlayer.exhaustedSlimed = 0;
        currentPlayer.cardsDrawnWith0Energy = 0;
        currentPlayer.cardsDrawnWith1Energy = 0;
        currentPlayer.cardsDrawnWith2Energy = 0;
        currentPlayer.cardsDrawnWith3Energy = 0;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                currentState.addMonster(convertMonster(m));
            }
        }

        return currentState;
    }

    private SimpleMonster convertMonster(AbstractMonster m) {
        switch (m.id) {
            case GremlinNob.ID:
                return new SimpleGremlinNob((GremlinNob) m);
            case Lagavulin.ID:
                return new SimpleLagavulin((Lagavulin) m);
            case SlimeBoss.ID:
                return new SimpleSlimeBoss((SlimeBoss) m);
            case TheGuardian.ID:
                return new SimpleTheGuardian((TheGuardian) m);
            default:
                return new SimpleMonster(m);
        }
    }

}
