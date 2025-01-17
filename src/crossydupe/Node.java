package crossydupe;

public class Node {
    private int x;
    private int y;
    public Node next;
    public Node prev;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.next = null;
        this.prev = null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
