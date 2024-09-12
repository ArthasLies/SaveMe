package crossydupe;

public class DoublyLinkedList {
    Node head;
    private Node tail;
    private Node current; // Track the current node
    private int size;

    public DoublyLinkedList() {
        head = null;
        tail = null;
        current = null;
        size = 0;
    }

    public void add(Node node) {
        if (head == null) {
            head = node;
            tail = node;
            current = head; // Set the current node to the head node
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    public Node getPreviousDeathPoint() {
        if (current != null && current.prev != null) {
            current = current.prev;
            return current;
        }
        return null;
    }

    public Node getNextDeathPoint() {
        if (current != null && current.next != null) {
            current = current.next;
            return current;
        }
        return null;
    }

    public Node getDeathPointByIndex(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    public void removeNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            return; 
        }

        Node nodeToRemove = getDeathPointByIndex(index);
        if (nodeToRemove == null) {
            return; 
        }

        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            head = nodeToRemove.next; 
        }

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            tail = nodeToRemove.prev; 
        }

        if (current == nodeToRemove) {
            current = nodeToRemove.next != null ? nodeToRemove.next : nodeToRemove.prev;
        }

        size--;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
        current = null;
        size = 0;
    }
}
