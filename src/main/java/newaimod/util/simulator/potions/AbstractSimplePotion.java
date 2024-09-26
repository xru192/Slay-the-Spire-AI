package newaimod.util.simulator.potions;

import newaimod.util.simulator.SimpleMonster;

public abstract class AbstractSimplePotion {
    public PotionCollection potionCollection;
    public final String potionId;
    public final boolean targetsOne;
    public final boolean requiresCombat;

    protected AbstractSimplePotion(PotionCollection collection, String id, boolean targetsOne, boolean requiresCombat) {
        potionCollection = collection;
        potionId = id;
        this.targetsOne = targetsOne;
        this.requiresCombat = requiresCombat;
    }

    public abstract void use(SimpleMonster target);

    public abstract void useWithEffectDoubled(SimpleMonster target);
}
