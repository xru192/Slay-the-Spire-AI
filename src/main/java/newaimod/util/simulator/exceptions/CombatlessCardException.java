package newaimod.util.simulator.exceptions;

public class CombatlessCardException extends RuntimeException {
    public CombatlessCardException() {
        super("Failed to use the AbstractSimpleCard: no active combat found.");
    }
}
