package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import newaimod.ai.AutoPlayer;
import newaimod.ai.AutoPlayer.CombatMove.TYPE;
import newaimod.util.dungeonInfo.CustomDungeonInformationProvider;
import newaimod.util.dungeonInfo.DungeonInformationProvider;
import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.cards.ironclad.attacks.SimpleStrike_Red;
import newaimod.util.simulator.cards.ironclad.skills.SimpleDefend_Red;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatingMovePickerTest {

    CombatSimulator state;
    CustomDungeonInformationProvider provider;
    AutoPlayer.CombatMove movePicked;

    @BeforeAll
    static void setUpBeforeAll() {
        SimulatingMovePicker.doLogging = false;
    }

    @Test
    void shouldPreferKillOverBlock() {
        // Setup
        state = new CombatSimulator()
                .withMonster(new SimpleMonster(6, 0, AbstractMonster.Intent.ATTACK, 10, 1))
                .withPlayerEnergy(1)
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false));
        provider = new CustomDungeonInformationProvider()
                .provideCurrentState(state)
                .provideCombatKey(DungeonInformationProvider.COMBAT_KEY.GENERIC_COMBAT);

        // Choose move
        movePicked = new SimulatingMovePicker(provider).pickMove();
        assertTrue(movePicked.type == TYPE.CARD && movePicked.index == 0);
    }

    @Test
    void shouldPreferBlockOverDamage() {
        // Setup
        state = new CombatSimulator()
                .withMonster(new SimpleMonster(7, 0, AbstractMonster.Intent.ATTACK, 10, 1))
                .withPlayerEnergy(1)
                .withPlayerCard(new SimpleStrike_Red(null, 1, false))
                .withPlayerCard(new SimpleDefend_Red(null, 1, false));
        provider = new CustomDungeonInformationProvider()
                .provideCurrentState(state)
                .provideCombatKey(DungeonInformationProvider.COMBAT_KEY.GENERIC_COMBAT);

        // Choose move
        movePicked = new SimulatingMovePicker(provider).pickMove();
        assertTrue(movePicked.type == TYPE.CARD && movePicked.index == 1);
    }
}