/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.persist;

import java.util.Observable;

/**
 * Runnable Implmentation which implements the Observer Pattern to notify view elements
 * 
 * @author Bryan Beck
 * @since 21.11.2012
 */
public abstract class RunnableImpl extends Observable implements Runnable{
    /**
     * The number of total elements for this task
     */
    private final int totalElements;
    
    /**
     * 
     * @param totalElements The number of total elements for this task
     */
    public RunnableImpl(int totalElements){
        super();
        this.totalElements = totalElements;
    }
//    /**
//     * @param observer The observer to notify
//     */
//    public RunnableImpl(Observer observer){
//        this.addObserver(observer);
//    }

    /**
     * @return the totalElements
     */
    public int getTotalElements() {
        return totalElements;
    }

//    /**
//     * @param totalElements the totalElements to set
//     */
//    public void setTotalElements(int totalElements) {
//        this.totalElements = totalElements;
//    }
}
