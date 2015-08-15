/**
 * Copyright 2014 University of Sydney, Australia.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.edu.sydney.Curracurrong.scheduler;

import java.util.Enumeration;
import java.util.Vector;
import au.edu.sydney.Curracurrong.runtime.TimeTriggered;

/**
 * An unbounded priority queue based on a priority heap. The interface of this
 * class is based on the Java 5 implementation of PriorityQueue and
 * PriorityBlockingQueue. It is thread-safe.
 * 
 * @author Akon Dey (akon.dey@sydney.edu.au), University of Sydney
 * @author Vasvi Kakkad (vasvi.kakkad@sydney.edu.au), University of Sydney
 * @author Bernhard Scholz (bernhard.scholz@sydney.edu.au), University of Sydney
 */
public class PriorityBlockingQueue{
    private static final int DEFAULT_INTIAL_CAPACITY = 11;

    private final Vector heap;

    /**
     * Creates a PriorityBlockingQueue with the specified initial capacity that
     * orders its elements according to their natural ordering (using
     * Comparable).
     *
     * @param initialCapacity the initial capacity for this priority queue.
     */
    public PriorityBlockingQueue(int initialCapacity) {
        if (initialCapacity < 1)
                throw new IllegalArgumentException("The initial capacity must be > 1. Actual: " + initialCapacity + ".");
        heap = new Vector(initialCapacity);
    }

    /**
     * Creates a PriorityBlockingQueue with the default initial capacity (
     * {@link #DEFAULT_INTIAL_CAPACITY}) that orders its elements according to
     * their natural ordering (using Comparable).
     */
    public PriorityBlockingQueue() {
        this(DEFAULT_INTIAL_CAPACITY);
    }

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection.
     */
    public synchronized int size() {
        return heap.size();
    }

    /**
     * Removes all elements from the priority queue. The queue will be empty
     * after this call returns.
     */
    public synchronized void clear() {
        heap.removeAllElements();
    }

    /**
     * Adds the specified element to this queue.
     *
     * @param c the element
     */
    public synchronized void add(Object c) {
        if (c == null)
            throw new NullPointerException("Cannot insert null object");
        heap.addElement(c); // Store item in next position
        if (size() > 1) {
            int i = heap.size() - 1;
            while (i > 0) {
                //compare item to parents and swap if necessary until in
                //correct position. This is to maintain the heap's min property.
                int parent = (i - 1) / 2;
                if(compareElements(heap.elementAt(i), heap.elementAt(parent)) < 0) {
                //if (((Comparable) heap.elementAt(i)).compareTo(heap.elementAt(parent)) < 0) {
                    Object temp = heap.elementAt(i);
                    heap.setElementAt(heap.elementAt(parent), i);
                    heap.setElementAt(temp, parent);
                    i = parent;
                }
                else
                    break;
            }
        }
        notifyAll();
    }

    /**
     * Inserts the specified element into this priority queue.
     *
     * @param o Adds the specified element to this queue.
     */
    public synchronized void offer(Object o) {
        add(o);
    }

    /**
     * Removes a single instance of the specified element from this queue, if it
     * is present.
     *
     * @param o element to be removed from this collection, if present.
     * @return true if the collection contained the specified element.
     */
    public synchronized boolean remove(Object o) {
        int i = heap.indexOf(o);
        if (i == -1)
                return false;
        restoreHeap(i);
        return true;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, returning null if
     * this queue is empty. Does not block.
     *
     * @return the head of this queue, or null if this queue is empty.
     */
    public synchronized Object peek() {
        return isEmpty() ? null : (Object) heap.firstElement();
    }

    /**
     * Retrieves, but does not remove, the head of this queue. If the queue is
     * empty, this method blocks until a root becomes available.
     *
     * @return the head of this queue.
     */
    public synchronized Object peekBlocking() {
        while (isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        return (Object) heap.firstElement();
    }

    /**
     * Retrieves and removes the head of this queue, or null if this queue is
     * empty. Does not block.
     *
     * @return the head of this queue, or null if this queue is empty.
     */
    public synchronized Object poll() {
        if (isEmpty())
                return null;

        Object ret = (Object) heap.elementAt(0); // The root element to return

        restoreHeap(0); // Fix the heap if the element at position 0 is removed

        return ret;
    }

    /**
     * Retrieves and removes the head of this queue. If the queue is empty, this
     * method blocks until a root becomes available.
     *
     * @return the head of this queue.
     */
    public synchronized Object pollBlocking() {
        while (isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        Object res = (Object) heap.firstElement();
        restoreHeap(0);
        return res;
    }

    /**
     * Returns true if this collection contains no elements.
     *
     * @return true if this collection contains no elements.
     */
    public synchronized boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Restores the heap to a valid state after removal of an element at a given
     * position in the heap array
     *
     * @param pos the position of the element which is to be removed
     */
    private synchronized void restoreHeap(int pos) {
        // Initial children of the element being removed
        int child1 = 2 * pos + 1;
        int child2 = 2 * pos + 2;

        // Loop until structure is normalised
        while (child1 < heap.size()) {
            // Move the smaller child element to fill the empty position
            if((child2 < heap.size()) && compareElements(heap.elementAt(child1), heap.elementAt(child2)) > 0) {
            //if ((child2 < heap.size()) && ((heap.elementAt(child1)).compareTo(heap.elementAt(child2)) > 0)) {
                heap.setElementAt(heap.elementAt(child2), pos);
                pos = child2;
            }
            else {
                heap.setElementAt(heap.elementAt(child1), pos);
                pos = child1;
            }

            // Calculate new children of the empty position
            child1 = 2 * pos + 1;
            child2 = 2 * pos + 2;
        }

        heap.setElementAt(heap.lastElement(), pos); // Fill a potential end gap
        heap.removeElementAt(heap.size() - 1);
    }

    public synchronized String toString() {
        StringBuffer s = new StringBuffer('{');
        for (int i = 0; i < heap.size(); i++) {
                s.append(heap.elementAt(i));
                if (i + 1 < heap.size())
                        s.append(',');
        }
        s.append('}');
        return s.toString();
    }

    /**
     * Returns an Enumeration over the elements in this queue. The Enumeration
     * does not return the elements in any particular order.
     *
     * @return an Enumeration over the elements in this queue.
     */
    public synchronized Enumeration elements() {
        return heap.elements();
    }

    
    public int compareElements(Object obj1, Object obj2) {

        int result = 0;
        if (((TimeTriggered)obj1).getStartTime() > ((TimeTriggered)obj2).getStartTime()) {
            result = 1;
        }else if (((TimeTriggered)obj1).getStartTime() < ((TimeTriggered)obj2).getStartTime()) {
            result = -1;
        } else{
            result = 0;
        }
        return result;

    }

    public boolean contains(TimeTriggered so) {
        if(heap.contains(so)) {
            return true ;
        } else {
            return false;
        }
    }
}