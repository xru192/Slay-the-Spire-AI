package newaimod.ai.basicIroncladPlayer.basicIroncladCombatMovePicker;

import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;

public class BasicStateEvaluator implements StateEvaluator {
    public double PHw = 1.0;
    public double AMw = -6;
    public double TMHw = -1.0 / 3;
    public double PSw = 5.0;
    public double PMw = 1.0;
    public double DFBw = 1.0;
    public double ESw = 0.001;
    public double VBw = 1.0 / 3;
    public double CDW0Ew = 0;
    public double CDW1Ew = 0.01;
    public double CDW2Ew = 0.01;
    public double CDW3Ew = 0.01;


    private int vulnerableBonus(CombatSimulator state) {
        int totalBonus = 0;
        for (SimpleMonster m : state.monsterList) {
            if (m.isAlive() && m.health >= 5 && m.vulnerable > 1) {
                int bonus = m.vulnerable == 2 ? 3 : 8;
                totalBonus += bonus;
            }
        }
        return totalBonus;
    }

    private double demonFormBonus(CombatSimulator state) {
        if (state.player.demonForm == 0) {
            return 0;
        }
        double bonus = state.player.demonForm * 5 - 5;
        int monsterHealth = state.getTotalMonsterEffectiveHealth();
        bonus += 0.16 * Math.max(0, Math.min(100, monsterHealth) - 60);
        bonus += 0.16 * Math.max(0, Math.min(250, monsterHealth) - 150);

        return bonus;
    }

    @Override
    public double evaluate(CombatSimulator state) {
        if (state.combatOver()) {
            return 1000;
        }

        state.triggerEndTurnEffects();
        int PH = state.getPlayerHealth();                                    // player health
        int AM = state.countAliveMonsters();                                 // alive monsters
        int TMH = state.getTotalMonsterEffectiveHealth();                    // total monster health
        int PS = state.player.strength;                                      // player strength
        int PM = state.player.metallicize;                                   // player metallicize
        double DFB = demonFormBonus(state);                                  // demon form bonus
        int ES = state.player.getExhaustedSlimed();                          // exhausted Slimed
        int VB = vulnerableBonus(state);                                     // vulnerable bonus
        int CDW0E = state.player.getCardsDrawnWith0Energy();                 // cards drawn with 0+ energy
        int CDW1E = state.player.getCardsDrawnWith1Energy();                 // cards drawn with 1+ energy
        int CDW2E = state.player.getCardsDrawnWith2Energy();                 // cards drawn with 2+ energy
        int CDW3E = state.player.getCardsDrawnWith3Energy();                 // cards drawn with 3+ energy

        double eval = (PH * PHw) + (AM * AMw) + (TMH * TMHw) + (PS * PSw) + (PM * PMw) + (DFB * DFBw) + (ES * ESw) + (VB * VBw) + (CDW0E * CDW0Ew) + (CDW1E * CDW1Ew) + (CDW2E * CDW2Ew) + (CDW3E * CDW3Ew);
        if (PH <= 0) {
            eval -= 1000;
            // if player will die, try to draw cards which can save player
            if (CDW1E > 0) {
                eval += 200;
            }
        }

        return eval;
    }
}
