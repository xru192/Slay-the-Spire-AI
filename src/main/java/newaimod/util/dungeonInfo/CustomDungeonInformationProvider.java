package newaimod.util.dungeonInfo;

import newaimod.util.simulator.CombatSimulator;

public class CustomDungeonInformationProvider implements DungeonInformationProvider {
    private CombatSimulator currentState = null;
    private RelicCollection currentRelics = null;
    private Integer actNumber = null;
    private Integer floorNumber = null;
    private BOSSKEY actBoss = null;
    private COMBAT_KEY combatKey = null;
    private DeckInfo currentDeckInfo = null;

    @Override
    public CombatSimulator getCurrentState() {
        if (currentState == null) {
            throw new RuntimeException();
        }
        return currentState;
    }

    public CustomDungeonInformationProvider provideCurrentState(CombatSimulator currentState) {
        this.currentState = currentState;
        return this;
    }

    @Override
    public RelicCollection getCurrentRelics() {
        if (currentRelics == null) {
            throw new RuntimeException();
        }
        return currentRelics;
    }

    public CustomDungeonInformationProvider provideCurrentRelics(RelicCollection currentRelics) {
        this.currentRelics = currentRelics;
        return this;
    }

    @Override
    public int getActNumber() {
        if (actNumber == null) {
            throw new RuntimeException();
        }
        return actNumber;
    }

    public CustomDungeonInformationProvider provideActNumber(int actNumber) {
        this.actNumber = actNumber;
        return this;
    }

    @Override
    public int getFloorNum() {
        if (floorNumber == null) {
            throw new RuntimeException();
        }
        return floorNumber;
    }

    public CustomDungeonInformationProvider provideFloorNum(int floorNum) {
        this.floorNumber = floorNum;
        return this;
    }

    @Override
    public BOSSKEY getActBoss() {
        if (actBoss == null) {
            throw new RuntimeException();
        }
        return actBoss;
    }

    public CustomDungeonInformationProvider provideActBoss(BOSSKEY actBoss) {
        this.actBoss = actBoss;
        return this;
    }

    @Override
    public COMBAT_KEY getCombatKey() {
        if (combatKey == null) {
            throw new RuntimeException();
        }
        return combatKey;
    }

    public CustomDungeonInformationProvider provideCombatKey(COMBAT_KEY combatKey) {
        this.combatKey = combatKey;
        return this;
    }

    @Override
    public DeckInfo getCurrentDeckInfo() {
        if (currentDeckInfo == null) {
            throw new RuntimeException();
        }
        return currentDeckInfo;
    }

    public CustomDungeonInformationProvider provideCurrentDeckInfo(DeckInfo currentDeckInfo) {
        this.currentDeckInfo = currentDeckInfo;
        return this;
    }
}
