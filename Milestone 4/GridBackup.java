
import java.util.ArrayList;

/**
 * Represents the grid corresponding to a warehouse layout of aisles;<br>
 * holds an array of Nodes (intersections);
 * uses ArrayList to calculate shortest path length.
 * @author Roger Glassey
 *version 4  june 2011  array list
 */
public class GridBackup {

    /**
     * Maximum x coordinate
     * used by constructor, recalc, neighbor
     **/
    public static int bigX;
    /**
     * Maximum y coordinate
     * used by constructor, recalc, neighbor
     */
    public static int bigY;
    /**
     * holds the Nodes, indexed by grid coordinates
     */
    public Node nodes[][];
    /**
     *set by setDestination(), used by recalc();
     */
    private Node destination;
    /**
     *larger than any path in the network
     */
    static final int BIG = 99;
    /**
     *used by recalc()
     */
    private ArrayList<Node> list = new ArrayList<Node>();

    /**
     * build the nodes array
    @param  lengthX , lengthy are the size of the array
     */
    GridBackup(int lengthX, int lengthY) {
        // your code here
        nodes = new Node[lengthX][lengthY];
        for (int i = 0; i < lengthX; i++) {
            for (int j = 0; j < lengthY; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        bigX = lengthX;
        bigY = lengthY;
    }

    /**
     *calculate shortest distance from all nodes to destination;<br>
     *uses list, node.  Calls neighbor(),  node.newDistance(),list.add((), list.removeFirst()
     */
    void recalc() {
        //to reset all nodes
        for (int i = 0; i <= bigX; i++) {
            for (int j = 0; j <= bigY; j++) {
                nodes[i][j].reset();
            }
        }//end reset
        destination.newDistance(0);
        list.add(destination);
        while (!list.isEmpty()) {
            Node currentNode = list.remove(0);
            int dist = 1 + currentNode.getDistance();
            for (int direction = 0; direction < 4; direction++) {
                Node neighbor = neighbor(currentNode, direction);
                if (neighbor != null && neighbor.newDistance(dist)) {
                    list.add(neighbor);
                }
            }
        }
    }

    /**
     *returns the neighbor of aNode; the  direction is a number (0,1,2,3) <BR>
     * multiple of 90 degrees from the X axis.  direction 0 is +X, direction 1 is +Y etc
     * If out of bounds, the neighbor is  null
     * @param  node  the node
     * @param direction of the neighbor
     */
    public Node neighbor(Node aNode, int direction) {
        Node node = aNode;
        Node neighbor = null;
        direction %= 4;
        int x = node.getX();
        int y = node.getY();
        if (direction < 0) {
            direction += 4;
        }
        if (direction == 0 && x < bigX) {
            neighbor = nodes[x + 1][y];
        } else if (direction == 2 && x > 0) {
            neighbor = nodes[x - 1][y];
        } else if (direction == 1 && y < bigY) {
            neighbor = nodes[x][y + 1];
        } else if (direction == 3 && y > 0) {
            neighbor = nodes[x][y - 1];
        }
        return neighbor;
    }

    /**
     * sets where the robot is trying to go
     */
    public void setDestination(int x, int y) {
        destination = nodes[x][y];
        recalc();
    }

    public Node getDestination() {
        return destination;
    }
}