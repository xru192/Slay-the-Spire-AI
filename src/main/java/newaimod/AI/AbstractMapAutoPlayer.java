package newaimod.AI;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.ChoiceScreenUtils;
import newaimod.util.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An AbstractMapAutoPlayer implementation progresses the game when a map choice must be made.
 */
public abstract class AbstractMapAutoPlayer {
    public static final Logger logger = LogManager.getLogger(AbstractMapAutoPlayer.class.getName());

    /**
     * Progress the game, given that a map choice must be made.
     */
    public final void execute() {
        logger.info("Executing...");
        if (ChoiceScreenUtils.getCurrentChoiceType() != ChoiceScreenUtils.ChoiceType.MAP) return;
        ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
        logger.info("Choosing between: " + choices);
        if (choices.isEmpty()) {
            logger.info("Done: no choices");
            return;
        }

        if (choices.size() == 1) {
            // will handle when there is only one child, like final rest -> boss
            choose(choices.get(0));
            logger.info("Done");
            return;
        }

        MapRoomNode current = AbstractDungeon.getCurrMapNode();
        List<Path> possiblePaths = Path.getPossiblePathsToBoss(current);
        Path choice = choosePath(possiblePaths);
        if (current == choice.start) {
            choose("x=" + choice.roomNodes.get(1).x);
        } else {
            choose("x=" + choice.start.x);
        }

    }

    protected abstract Path choosePath(List<Path> paths);

    public static void choose(String choice) {
        try {
            ArrayList<String> choices = ChoiceScreenUtils.getCurrentChoiceList();
            logger.info("Making choice: " + choice);
            ChoiceScreenUtils.makeMapChoice(choices.indexOf(choice));
        } catch (Exception e) {
            logger.info("Failed to make choice: " + choice + ". Error: " + e.getMessage());
        }
    }

    /**
     * A Path represents a sequence of rooms the player can travel to before fighting the boss. Paths do not include
     * rooms which are not visible on the map (Neow's room at the start of Act I, and EmptyRoom at the start of other
     * acts).
     */
    public static class Path {
        enum ROOMS {
            UNKNOWN, MERCHANT, TREASURE, REST, ENEMY, ELITE, BURNING_ELITE, OTHER
        }

        public MapRoomNode start; // the room this path starts in
        @NotNull
        public MapRoomNode end; // the room this path ends in

        List<MapRoomNode> roomNodes;
        List<ROOMS> rooms;

        public Path(@NotNull MapRoomNode start) {
            this.start = start;
            end = start;
            roomNodes = new ArrayList<>(Collections.singletonList(start));
            rooms = new ArrayList<>(Collections.singletonList(getType(start)));
        }

        public Path(Path path) {
            start = path.start;
            end = path.end;
            roomNodes = new ArrayList<>(path.roomNodes);
            rooms = new ArrayList<>(path.rooms);
        }

        public void travel(MapRoomNode node) {
            assert node != null;
            end = node;
            roomNodes.add(node);
            rooms.add(getType(node));
        }

        private ROOMS getType(MapRoomNode node) {
            String symbol = ReflectionHacks.getPrivate(node.room, AbstractRoom.class, "mapSymbol");
            switch (symbol) {
                case "?":
                    return ROOMS.UNKNOWN;
                case "$":
                    return ROOMS.MERCHANT;
                case "T":
                    return ROOMS.TREASURE;
                case "R":
                    return ROOMS.REST;
                case "M":
                    return ROOMS.ENEMY;
                case "E":
                    if (node.hasEmeraldKey) {
                        return ROOMS.BURNING_ELITE;
                    } else {
                        return ROOMS.ELITE;
                    }
            }
            return ROOMS.OTHER;
        }

        /**
         * Returns all possible Paths to the boss corresponding to the given room. If the given room is visible on the
         * map, the Paths will start with the given room. Otherwise, the Paths will start with one of the rooms at y=0.
         *
         * @param initial room to find paths from
         * @return Paths to the boss
         */
        static List<Path> getPossiblePathsToBoss(MapRoomNode initial) {
            // Compute starting nodes

            List<MapRoomNode> startingNodes;
            // For act 3 you start at y = 15, others -1
            int bossY = AbstractDungeon.map.size();
            assert bossY == 15;
            if (initial.y == -1 || initial.y == bossY) {
                startingNodes = MapUtils.getRootNodes(AbstractDungeon.map);
            } else {
                startingNodes = new ArrayList<>();
                startingNodes.add(initial);
            }

            // BFS to get possible Paths to boss room from starting nodes

            List<Path> possiblePaths = new ArrayList<>();

            Queue<Path> queue = new ArrayDeque<>();
            for (MapRoomNode startNode : startingNodes) {
                queue.add(new Path(startNode));
            }

            while (!queue.isEmpty()) {
                Path p = queue.poll();
                if (p.end.y == bossY - 1) {
                    possiblePaths.add(p);
                    continue;
                }
                for (MapEdge edge : p.end.getEdges()) {
                    MapRoomNode child = MapUtils.getNode(edge.dstX, edge.dstY, AbstractDungeon.map);
                    if (child == null) {
                        continue;
                    }
                    Path childPath = new Path(p);
                    childPath.travel(child);
                    queue.add(childPath);
                }
            }

            return possiblePaths;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "start=" + start +
                    ", end=" + end +
                    ", roomNodes=" + roomNodes +
                    ", rooms=" + rooms +
                    '}';
        }
    }
}
