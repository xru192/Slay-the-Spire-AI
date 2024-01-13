package newaimod.util;

import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.ArrayList;

public class MapUtils {
    /**
     * @param map the map to find root nodes for
     * @return nodes at y=0 that the player can choose to start at in the given map.
     */
    public static ArrayList<MapRoomNode> getRootNodes(ArrayList<ArrayList<MapRoomNode>> map) {
        ArrayList<MapRoomNode> roots = new ArrayList<>();
        for (MapRoomNode node : map.get(0)) {
            if (node.hasEdges()) {
                roots.add(node);
            }
        }
        return roots;
    }

    /**
     * Returns the MapRoomNode at the specified coordinates in the given map, or null if it does not exist.
     *
     * @param x   the x-coordinate of the desired node
     * @param y   the y-coordinate of the desired node
     * @param map the map to find the node in
     * @return the node at the specified coordinates in the given map
     */
    public static MapRoomNode getNode(int x, int y, ArrayList<ArrayList<MapRoomNode>> map) {
        try {
            return map.get(y).get(x);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
