package newaimod.AI.BasicIroncladPlayer;

import communicationmod.ChoiceScreenUtils;
import newaimod.AI.AbstractCombatMovePicker;
import newaimod.AI.AutoPlayer;
import newaimod.AI.BasicIroncladPlayer.BasicIroncladCombatMovePicker.BasicIroncladCombatMovePicker;
import newaimod.AI.AbstractEventAutoPlayer;

public class BasicIroncladPlayer implements AutoPlayer {

    private final AbstractEventAutoPlayer eventPlayer;
    private final BasicIroncladChestPlayer chestPlayer;
    private final BasicIroncladRestPlayer restPlayer;
    private final BasicIroncladCardRewardPlayer cardRewardPlayer;
    private final BasicIroncladCombatRewardPlayer combatRewardPlayer;
    private final BasicIroncladMapPlayer mapPlayer;
    private final BasicIroncladBossRewardPlayer bossRewardPlayer;
    private final BasicIroncladGridPlayer gridPlayer;
    private final BasicIroncladHandSelectPlayer handSelectPlayer;
    private final AbstractCombatMovePicker combatPlayer;

    public BasicIroncladPlayer() {
        eventPlayer = new BasicIroncladEventPlayer();
        chestPlayer = new BasicIroncladChestPlayer();
        restPlayer = new BasicIroncladRestPlayer();
        cardRewardPlayer = new BasicIroncladCardRewardPlayer();
        combatRewardPlayer = new BasicIroncladCombatRewardPlayer();
        mapPlayer = new BasicIroncladMapPlayer();
        bossRewardPlayer = new BasicIroncladBossRewardPlayer();
        gridPlayer = new BasicIroncladGridPlayer();
        handSelectPlayer = new BasicIroncladHandSelectPlayer();
        combatPlayer = new BasicIroncladCombatMovePicker();
    }

    @Override
    public void playEvent() {
        eventPlayer.execute();
    }

    @Override
    public void playChest() {
        chestPlayer.execute();
    }

    @Override
    public void playShop() {
        ChoiceScreenUtils.pressConfirmButton();
    }

    @Override
    public void playRest() {
        restPlayer.execute();
    }

    @Override
    public void playCardReward() {
        cardRewardPlayer.execute();
    }

    @Override
    public void playCombatReward() {
        combatRewardPlayer.execute();
    }

    @Override
    public void playMap() {
        mapPlayer.execute();
    }

    @Override
    public void playBossReward() {
        bossRewardPlayer.execute();
    }

    @Override
    public void playGrid() {
        gridPlayer.execute();
    }

    @Override
    public void playHandSelect() {
        handSelectPlayer.execute();
    }

    @Override
    public CombatMove pickCombatMove() {
        return combatPlayer.pickMove();
    }
}
