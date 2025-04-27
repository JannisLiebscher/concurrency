package org.example.ex5.problem1;

import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentList {
    private static class Node {
        int value;
        Node next;
        ReentrantLock lock = new ReentrantLock();

        Node(int value) {
            this.value = value;
        }
    }
    private final Node head;

    public ConcurrentList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    public void insert(int value) {
        Node pred = null, curr = null;
        head.lock.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.value < value) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                Node newNode = new Node(value);
                newNode.next = curr;
                pred.next = newNode;
                System.out.println("Inserted: " + value);
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public void delete(int value) {
        Node pred = null, curr = null;
        head.lock.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.value < value) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                if (curr.value == value) {
                    pred.next = curr.next;
                    System.out.println("Deleted: " + value);
                }
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }


}
