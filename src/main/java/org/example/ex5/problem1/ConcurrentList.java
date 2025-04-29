package org.example.ex5.problem1;

import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentList {
    private static class Node extends ReentrantLock {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }
    private final Node head;

    public ConcurrentList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }
    /**
     * Inserts a new value into the linked list.
     *
     * @param value the value to insert
     * @return true if the value was successfully inserted else false
     */
    public boolean insert(int value) {
        Node pred = null, curr = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();

            while (curr.value < value) {
                pred.unlock();
                pred = curr;
                if (curr.next == null) return false;
                curr = curr.next;
                curr.lock();
            }
            Node newNode = new Node(value);
            newNode.next = curr;
            pred.next = newNode;
            System.out.println("Inserted: " + value);
            return true;
        } finally {
            if (curr.isHeldByCurrentThread()) curr.unlock();
            if (pred.isHeldByCurrentThread()) pred.unlock();
        }
    }

    /**
     * Deletes a value from the linked list if present.
     *
     * @param value the value to delete
     * @return true if the value was successfully deleted; false if the value was not found
     */
    public boolean delete(int value) {
        Node pred = null, curr = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();

            while (curr.value < value) {
                pred.unlock();
                pred = curr;
                if (curr.next == null) return false;
                curr = curr.next;
                curr.lock();
            }
            if (curr.value == value) {
                pred.next = curr.next;
                System.out.println("Deleted: " + value);
                return true;
            }
        } finally {
            if (curr.isHeldByCurrentThread()) curr.unlock();
            if (pred.isHeldByCurrentThread()) pred.unlock();
        }
        return false;
    }

    /**
     * Returns a comma-separated string representation of the linked list.
     * Thread safe, but since Hand-Over-Hand Locking is used a consistent Snapshot is not guaranteed
     *
     * @return a string representation of all values in the list
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Node pred = null, curr = null;
        head.lock();
        try {
            builder.append(head.value);
            pred = head;
            curr = pred.next;
            curr.lock();

            while (true) {
                pred.unlock();
                pred = curr;
                builder.append(",");
                builder.append(pred.value);
                if (curr.next == null) return builder.toString();
                curr = curr.next;
                curr.lock();
            }
        } finally {
            assert pred != null;
            if (pred.isLocked()) pred.unlock();
        }
    }


}
