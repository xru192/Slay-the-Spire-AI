package newaimod.ai.basicIroncladPlayer;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import newaimod.ai.AbstractMapAutoPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BasicIroncladMapPlayer extends AbstractMapAutoPlayer {
    public static final Logger logger = LogManager.getLogger(BasicIroncladMapPlayer.class.getName());

    @Override
    protected Path choosePath(List<Path> paths) {
        Path bestPath = null;
        double bestEval = -1_000;
        for (Path p : paths) {
            double eval = evalPath(p);
            if (eval > bestEval) {
                bestPath = p;
                bestEval = eval;
            }
        }
        
        return bestPath;
    }


    private double evalPath(Path path) {
        int startY = path.roomNodes.get(0).y;
        int rests = 0;
        double eliteEval = 0;
        boolean lowHealth = AbstractDungeon.player.currentHealth < 30;

        for (int i = 0; i < path.rooms.size(); ++i) {
            int y = startY + i;
            boolean early = y < 5;
            boolean mid = 5 <= y && y < 9;

            switch (path.rooms.get(i)) {
                case UNKNOWN:
                    break;
                case MERCHANT:
                    break;
                case REST:
                    ++rests;
                    break;
                case ENEMY:
                    break;
                case ELITE:
                    if (lowHealth) {
                        if (early) {
                            eliteEval -= 2;
                        } else if (mid) {
                            eliteEval -= 1;
                        } else {
                            eliteEval -= 0.5;
                        }
                    } else {
                        if (early) {
                            eliteEval -= 1;
                        } else if (mid) {
                            eliteEval += 1;
                        } else {
                            eliteEval += 1.5;
                        }
                    }

                    break;
                case BURNING_ELITE:
                    break;
                case TREASURE:
                case OTHER:
                    break;
            }
        }

        return rests + eliteEval;

    }
}
