package newaimod.util.simulator.potions;

import newaimod.util.simulator.CombatSimulator;
import newaimod.util.simulator.SimpleMonster;
import newaimod.util.simulator.potions.potions.FillerPotion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A PotionCollection represents the bag of potions possessed by the player during a run.
 */
public class PotionCollection {

    private boolean hasExpandedCapacity;
    private boolean hasDoubledEffects;
    public CombatSimulator simulator;  // the simulator potions can be used in, null if not in combat
    private int capacity;
    private AbstractSimplePotion[] potions;

    public PotionCollection(CombatSimulator simulator, boolean hasExpandedCapacity, boolean hasDoubledEffects) {
        this.hasExpandedCapacity = hasExpandedCapacity;
        this.hasDoubledEffects = hasDoubledEffects;
        this.simulator = simulator;
        capacity = hasExpandedCapacity ? 5 : 3;
        potions = new AbstractSimplePotion[capacity];
        Arrays.fill(potions, new FillerPotion());
    }

    public PotionCollection(PotionCollection that, CombatSimulator simulator) {
        hasExpandedCapacity = that.hasExpandedCapacity;
        hasDoubledEffects = that.hasDoubledEffects;
        this.simulator = simulator;
        capacity = that.capacity;
        potions = Arrays.copyOf(that.potions, that.potions.length);
    }

    public void addPotion(AbstractSimplePotion potion, int index) {
        if (index >= capacity || index < 0) {
            throw new RuntimeException("Tried to add potion to non-existing slot (" + index + ")");
        }
        if (!(potions[index] instanceof FillerPotion)) {
            throw new RuntimeException("Tried to replace an existing potion");
        }

        potions[index] = potion;
    }

    public void discardPotion(int index) {
        if (potions[index] instanceof FillerPotion) {
            throw new RuntimeException("Tried to discard missing potion.");
        }
        potions[index] = new FillerPotion();
    }

    public void usePotion(int index, SimpleMonster target) {
        AbstractSimplePotion potion = potions[index];
        if (potions[index] instanceof FillerPotion) {
            throw new RuntimeException("Tried to use missing potion");
        }

        if (potion.targetsOne && target == null) {
            throw new RuntimeException("Missing target for potion");
        }

        if (potion.requiresCombat && simulator == null) {
            throw new RuntimeException("Missing simulator. Probably tried to use combat potion outside of combat");
        }

        if (hasDoubledEffects) {
            potion.useWithEffectDoubled(target);
        } else {
            potion.use(target);
        }
        potions[index] = new FillerPotion();
    }

    public void enableExpandedCapacity() {
        hasExpandedCapacity = true;
        capacity = 5;
        AbstractSimplePotion[] newPotions = new AbstractSimplePotion[capacity];
        Arrays.fill(newPotions, new FillerPotion());
        System.arraycopy(potions, 0, newPotions, 0, potions.length);
        potions = newPotions;
    }

    public void enableDoubledEffects() {
        hasDoubledEffects = true;
    }

    public String[] getPotionIds() {
        return (String[]) Arrays.stream(potions).map(potion -> potion.potionId).toArray();
    }

    public boolean hasPotion(String id) {
        return Arrays.stream(potions).anyMatch(potion -> potion.potionId.equals(id));
    }

    public int indexOf(String id) {
        for (int i = 0; i < capacity; i++) {
            if (potions[i].potionId.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private boolean potionBagsEqual(AbstractSimplePotion[] bag1, AbstractSimplePotion[] bag2) {
        List<String> sortedList1 = Arrays.stream(bag1)
                .map(potion -> potion.potionId)
                .sorted()
                .collect(Collectors.toList());

        List<String> sortedList2 = Arrays.stream(bag2)
                .map(potion -> potion.potionId)
                .sorted()
                .collect(Collectors.toList());

        // Compare the sorted lists
        return sortedList1.equals(sortedList2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PotionCollection that = (PotionCollection) o;
        return hasExpandedCapacity == that.hasExpandedCapacity && hasDoubledEffects == that.hasDoubledEffects && capacity == that.capacity && potionBagsEqual(potions, that.potions);
    }

    @Override
    public int hashCode() {
        List<String> sortedPotionIds = Arrays.stream(potions)
                .map(potion -> potion.potionId)
                .sorted()
                .collect(Collectors.toList());
        return Objects.hash(hasExpandedCapacity, hasDoubledEffects, capacity, sortedPotionIds);
    }
}
