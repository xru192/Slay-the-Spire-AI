package newaimod.util.dungeonInfo;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.*;
import newaimod.NewAIMod;
import newaimod.util.CombatUtils;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.SimplePlayer;
import newaimod.util.simulator.monsters.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealDungeonInformationProvider implements DungeonInformationProvider {
    public static final Logger logger = LogManager.getLogger(RealDungeonInformationProvider.class.getName());

    private static final RealDungeonInformationProvider instance = new RealDungeonInformationProvider();

    private RealDungeonInformationProvider() {
    }

    public static RealDungeonInformationProvider getInstance() {
        return instance;
    }


    @Override
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

    @Override
    public int getActNumber() {
        return AbstractDungeon.actNum;
    }

    @Override
    public int getFloorNum() {
        return AbstractDungeon.floorNum;
    }

    @Override
    public DungeonInformationProvider.BOSSKEY getActBoss() {
        switch (AbstractDungeon.bossKey) {
            case MonsterHelper.GUARDIAN_ENC:
                return RealDungeonInformationProvider.BOSSKEY.GUARDIAN;
            case MonsterHelper.HEXAGHOST_ENC:
                return RealDungeonInformationProvider.BOSSKEY.HEXAGHOST;
            case MonsterHelper.SLIME_BOSS_ENC:
                return RealDungeonInformationProvider.BOSSKEY.SLIME_BOSS;
            case MonsterHelper.COLLECTOR_ENC:
                return RealDungeonInformationProvider.BOSSKEY.COLLECTOR;
            case MonsterHelper.AUTOMATON_ENC:
                return RealDungeonInformationProvider.BOSSKEY.AUTOMATON;
            case MonsterHelper.CHAMP_ENC:
                return RealDungeonInformationProvider.BOSSKEY.CHAMP;
            case MonsterHelper.AWAKENED_ENC:
                return RealDungeonInformationProvider.BOSSKEY.AWAKENED_ONE;
            case MonsterHelper.TIME_EATER_ENC:
                return RealDungeonInformationProvider.BOSSKEY.TIME_EATER;
            case MonsterHelper.DONU_DECA_ENC:
                return RealDungeonInformationProvider.BOSSKEY.DONU_DECA;
            default:
                throw new RuntimeException("Unexpected boss");
        }
    }

    @Override
    public DungeonInformationProvider.DeckInfo getCurrentDeckInfo() {
        return new DeckInfo(AbstractDungeon.player.masterDeck.group);
    }

    private SimpleMonster convertMonster(AbstractMonster m) {
        switch (m.id) {
            case GremlinNob.ID:
                return new SimpleGremlinNob((GremlinNob) m);
            case Lagavulin.ID:
                return new SimpleLagavulin((Lagavulin) m);
            case Sentry.ID:
                return new SimpleSentry((Sentry) m);
            case SlimeBoss.ID:
                return new SimpleSlimeBoss((SlimeBoss) m);
            case TheGuardian.ID:
                return new SimpleTheGuardian((TheGuardian) m);
            default:
                return new SimpleMonster(m);
        }
    }
}
