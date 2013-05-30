package com.noveogroup.clap.web;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author
 */
@Named
@SessionScoped
public class TestSessionBean implements Serializable {

    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(final int counter) {
        this.counter = counter;
    }

    public void increment() {
        counter++;
    }
}