package newaimod.AI;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * An AutoPlayer implements functionality to progress the game at any point.
 */
public interface AutoPlayer {

    /**
     * Progresses the game. Precondition: user is currently in an event.
     */
    void playEvent();

    /**
     * Progresses the game. Precondition: user is currently at an unopened chest.
     */
    void playChest();

    /**
     * Progresses the game. Precondition: user is currently at a shop room.
     */
    void playShop();

    /**
     * Progresses the game. Precondition: user is currently at a rest site.
     */
    void playRest();

    /**
     * Progresses the game. Precondition: user is currently choosing between cards.
     */
    void playCardReward();

    /**
     * Progresses the game. Precondition: user is currently .
     */
    void playCombatReward();

    /**
     * Progresses the game. Precondition: user is currently choosing which room to go to.
     */
    void playMap();

    /**
     * Progresses the game. Precondition: user is currently choosing between boss relics.
     */
    void playBossReward();

    /**
     * Progresses the game. Precondition: user is currently selecting from a grid of cards.
     */
    void playGrid();

    /**
     * Progresses the game. Precondition: user is currently selecting from their hand of cards.
     */
    void playHandSelect();

    /**
     * Returns a move to be executed in the current combat.
     */
    CombatMove pickCombatMove();

    /**
     * A CombatMove is an action a player can take during combat which progresses the combat.
     */
    class CombatMove {
        public enum TYPE {CARD, POTION, PASS, DISCARD}

        public TYPE type;
        public int index; // index of the card/potion to be used

        public AbstractMonster target; // target of card/potion to be used

        public CombatMove(TYPE t, int index, AbstractMonster target) {
            type = t;
            this.index = index;
            this.target = target;
        }

        public CombatMove(TYPE t, int index) {
            this(t, index, null);
            if (t != TYPE.PASS && t != TYPE.DISCARD) {
                throw new IllegalArgumentException("Not enough Move information");
            }
        }

        public CombatMove(TYPE t) {
            this(t, -1, null);
            if (t != TYPE.PASS) {
                throw new IllegalArgumentException("Not enough Move information.");
            }
        }

        @Override
        public String toString() {
            if (target == null) {
                return "Move{" +
                        "type=" + type +
                        ", index=" + index +
                        ", target=" + "None" +
                        '}';
            }
            return "Move{" +
                    "type=" + type +
                    ", index=" + index +
                    ", target=" + target.name +
                    '}';
        }

    }

}
