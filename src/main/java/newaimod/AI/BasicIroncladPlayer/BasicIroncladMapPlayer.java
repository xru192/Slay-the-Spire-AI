package newaimod.AI.BasicIroncladPlayer;

import newaimod.AI.AbstractMapAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BasicIroncladMapPlayer extends AbstractMapAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladMapPlayer.class.getName());

    @Override
    protected Path choosePath(List<Path> paths) {
        return paths.get(0);
    }
}
