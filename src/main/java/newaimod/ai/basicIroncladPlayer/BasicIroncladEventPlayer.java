package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.CurseOfTheBell;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import communicationmod.ChoiceScreenUtils;
import newaimod.ai.AbstractEventAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class BasicIroncladEventPlayer extends AbstractEventAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladEventPlayer.class.getName());

    @Override
    public void executeNeowEvent() {
        // in the 2-choice version, choose max hp over 1hp combats
        // with 4 choices choose one of the non-drawback rewards
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        if (choices.size() == 2) {
            choose("max hp +8");
        } else {
            if (choices.contains("remove a card from your deck")) {
                choose("remove a card from your deck");
            } else if (choices.contains("obtain a random common relic")) {
                choose("obtain a random common relic");
            } else if (choices.contains("obtain 100 gold")) {
                choose("obtain 100 gold");
            } else if (choices.contains("upgrade a card")) {
                choose("upgrade a card");
            } else if (choices.contains("max hp +8")) {
                choose("max hp +8");
            } else if (choices.contains("choose a card to obtain")) {
                choose("choose a card to obtain");
            } else if (choices.contains("obtain 3 random potions")) {
                choose("obtain 3 random potions");
            } else if (choices.contains("enemies in your next three combats have 1 hp")) {
                choose("enemies in your next three combats have 1 hp");
            } else if (choices.contains("obtain a random rare card")) {
                choose("obtain a random rare card");
            } else if (choices.contains("choose a colorless card to obtain")) {
                choose("choose a colorless card to obtain");
            } else if (choices.contains("transform a card")) {
                choose("transform a card");
            } else {
                choose(choices.get(0)); // choose one of the non-drawback ones
            }
        }
    }

    @Override
    public void executeAccursedBlacksmith() {
        // upgrade if possible, leave otherwise (never relic/curse)
        tryChoose("forge", "leave");
    }

    @Override
    public void executeAddict() {
        // pay for relic if possible, leave otherwise (never relic/curse)
        tryChoose("offer gold", "leave");
    }

    @Override
    public void executeBackToBasics() {
        // upgrade strikes and defends if there are at least 3 to be upgraded, remove card otherwise
        int toUpgrade = 0;
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if ((card.isStarterStrike() || card.isStarterDefend()) && !card.upgraded) {
                toUpgrade += 1;
            }
        }
        if (toUpgrade > 2) {
            choose("simplicity");
        } else {
            choose("elegance");
        }
    }

    @Override
    public void executeBeggar() {
        // pay to remove card if possible, leave otherwise
        tryChoose("offer gold", "leave");
    }

    @Override
    public void executeBigFish() {
        // gain 5 max HP unless current health is below 40 (never relic/curse)
        if (AbstractDungeon.player.currentHealth < 40) {
            choose("banana");
        } else {
            choose("donut");
        }
    }

    @Override
    public void executeColosseum() {
        // always escape
        choose("cowardice");
    }

    @Override
    public void executeCursedTome() {
        // never read the book
        choose("leave");
    }

    @Override
    public void executeDeadAdventurer() {
        // never search the adventurer
        choose("leave");
    }

    @Override
    public void executeDesigner(boolean adjustmentUpgradesOne, boolean cleanUpRemovesCards) {
        logger.info("Adjustment Upgrades One: " + adjustmentUpgradesOne + ", Clean Up removes: " + cleanUpRemovesCards);
        // preference with removable curse: Cheapest card removal - Adjustments - Punch
        // without: Adjustments - Clean Up - Punch

        if (countRemovableCurses() > 0) {
            if (cleanUpRemovesCards) {
                tryChoose("clean up", "adjustments", "punch");
            } else {
                tryChoose("full service", "adjustments", "punch");
            }
        } else {
            tryChoose("adjustments", "clean up", "punch");
        }
    }

    @Override
    public void executeDrugDealer() {
        // always get a Mutagenic Strength
        choose("ingest mutagens");
    }

    @Override
    public void executeDuplicator() {
        // never duplicate card
        choose("leave");
    }

    @Override
    public void executeFaceTrader() {
        // lose hp to gain gold with more than 60 health, leave otherwise (never trade faces)
        if (AbstractDungeon.player.currentHealth > 60) {
            choose("touch");
        } else {
            choose("leave");
        }
    }

    @Override
    public void executeFalling() {
        // lose the attack if possible, skill otherwise
        tryChoose("strike", "land");
    }

    @Override
    public void executeForgottenAltar() {
        // sacrifice if healthy, exchange idol if possible, desecrate if very weak, sacrifice otherwise
        int health = AbstractDungeon.player.currentHealth;
        if (health > 65) {
            choose("sacrifice");
        } else if (health < 40) {
            tryChoose("offer: golden idol", "desecrate");
        } else {
            tryChoose("offer: golden idol", "sacrifice");
        }
    }

    @Override
    public void executeFountainOfCurseRemoval() {
        // always remove curses
        tryChoose("drink", "leave");
    }

    @Override
    public void executeGhosts() {
        // never take apparitions
        choose("refuse");
    }

    @Override
    public void executeGoldenIdolEvent(boolean isFirstDecision) {
        // take the idol and take damage if above 60 health
        if (isFirstDecision) {
            if (AbstractDungeon.player.currentHealth > 60) {
                choose("take");
            } else {
                choose("leave");
            }
        } else {
            choose("smash");
        }
    }

    @Override
    public void executeGoldShrine() {
        // always take gold for no curse
        choose("pray");
    }

    @Override
    public void executeGoldenWing() {
        // remove a card if above 40 health, get gold if possible, leave otherwise
        if (AbstractDungeon.player.currentHealth > 40) {
            choose("pray");
        } else {
            tryChoose("destroy", "leave");
        }
    }

    @Override
    public void executeKnowingSkull() {
        // always leave instantly
        choose("how do i leave?");
    }

    @Override
    public void executeSssserpent() {
        // always refuse gold/curse
        choose("disagree");
    }

    @Override
    public void executeLivingWall() {
        // always remove card
        choose("forget");
    }

    @Override
    public void executeMaskedBandits() {
        // Fight only if player has 50 health
        if (AbstractDungeon.player.currentHealth < 50) {
            choose("pay");
        } else {
            choose("fight!");
        }
    }

    @Override
    public void executeGremlinMatchGame() {
        // keep picking the first cards until the event ends
        ChoiceScreenUtils.makeEventChoice(0);
    }

    @Override
    public void executeMindBloom() {
        // always fight act 1 boss
        choose("i am war");
    }

    @Override
    public void executeMushrooms() {
        // take the fight unless below 25 hp
        if (AbstractDungeon.player.currentHealth < 25) {
            choose("eat");
        } else {
            choose("stomp");
        }
    }

    @Override
    public void executeMysteriousSphere() {
        // never take fight for relic
        choose("leave");
    }

    @Override
    public void executeNloth() {
        // never exchange relic
        choose("leave");
    }

    @Override
    public void executeNest() {
        // never get the dagger
        choose("smash and grab");
    }

    @Override
    public void executeNoteForYourself() {
        // never swap card
        choose("ignore");
    }

    @Override
    public void executePurificationShrine() {
        // always remove card
        choose("pray");
    }

    @Override
    public void executeScrapOoze() {
        // never go for the relic
        choose("leave");
    }

    @Override
    public void executeSecretPortal() {
        // never jump to the boss
        choose("leave");
    }

    @Override
    public void executeSensoryStone() {
        // always go for 1 card reward
        ChoiceScreenUtils.makeEventChoice(0);
    }

    @Override
    public void executeShiningLight() {
        // upgrade cards with more than 60 health
        if (AbstractDungeon.player.currentHealth > 60) {
            choose("enter");
        } else {
            choose("leave");
        }
    }

    @Override
    public void executeCleric() {
        // heal if below 40 health, remove if possible otherwise
        if (AbstractDungeon.player.currentHealth < 40) {
            tryChoose("heal", "leave");
        } else {
            tryChoose("purify", "leave");
        }
    }

    @Override
    public void executeTheJoust() {
        // always bet on the murderer
        choose("murderer");
    }

    @Override
    public void executeTheLibrary() {
        // always heal
        choose("sleep");
    }

    @Override
    public void executeTheMausoleum() {
        // never risk curse for relic
        choose("leave");
    }

    @Override
    public void executeMoaiHead() {
        // take heal for minus max hp if below 30 health, otherwise sell idol if possible
        if (AbstractDungeon.player.currentHealth < 30) {
            choose("jump inside");
        } else {
            tryChoose("offer: golden idol", "leave");
        }
    }

    @Override
    public void executeWomanInBlue() {
        // never buy potions
        choose("leave");
    }

    @Override
    public void executeTombRedMask() {
        if (AbstractDungeon.player.gold < 50) {
            tryChoose("don the red mask", "offer: " + AbstractDungeon.player.gold + " gold");
        } else {
            tryChoose("don the red mask", "leave");
        }
    }

    @Override
    public void executeTransmogrifier() {
        // always do not transform a card
        choose("leave");
    }

    @Override
    public void executeUpgradeShrine() {
        // always upgrade a card if possible
        tryChoose("pray", "leave");
    }

    @Override
    public void executeVampires() {
        // refuse bites
        choose("refuse");
    }

    @Override
    public void executeWeMeetAgain() {
        // never get relic
        choose("attack");
    }

    @Override
    public void executeWindingHalls() {
        // take madness with enough health, take curse only if very low
        if (AbstractDungeon.player.currentHealth > 40) {
            choose("embrace madness");
        } else if (AbstractDungeon.player.currentHealth < 10) {
            choose("focus");
        } else {
            choose("retrace your steps");
        }
    }

    @Override
    public void executeGoopPuddle() {
        // take gold only with at least 40 health
        if (AbstractDungeon.player.currentHealth < 40) {
            choose("leave it");
        } else {
            choose("gather gold");
        }
    }

    public int countRemovableCurses() {
        int count = 0;

        String[] permCurses = {AscendersBane.ID, CurseOfTheBell.ID, Necronomicurse.ID};
        HashSet<String> permCurseSet = new HashSet<>(Arrays.asList(permCurses));
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.type == AbstractCard.CardType.CURSE) {
                if (!permCurseSet.contains(card.cardID)) {
                    count += 1;
                }
            }
        }
        return count;
    }

}
