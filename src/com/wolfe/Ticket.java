/**
 * Created by Jeremy on 10/5/2016.
 * Implements Serializable - an interface (perhaps considered an Abstract class). This allows the object
 * to be flattened into a byte stream that can be transmitted over a wire or locally.
 * Used in TicketIO to save LinkedList queues (in their entirety) to disk file.
 *
 * This class contains data for a support trouble ticket for use by a tech support person.
 */
package com.wolfe;

import java.util.Date;
import java.io.Serializable;

public class Ticket implements Serializable {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private Date dateClosed;
    private String resolution;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;

    //The ID for each ticket - instance variable. Each Ticket will have it's own ticketID variable
    protected int ticketID;



    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }


    public static int getStaticTicketIDCounter() {
        return staticTicketIDCounter;
    }

    public static void setStaticTicketIDCounter(int staticTicketIDCounter) {
        // System.out.println("in Ticket: set staticID = " + staticTicketIDCounter);
        Ticket.staticTicketIDCounter = staticTicketIDCounter;
    }

    public int getTicketID() {
        return ticketID;
    }

    protected int getPriority() {
        return priority;
    }
    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString(){
        return("ID= " + this.ticketID + " Description: " + this.description + " Priority: "
                + this.priority + " Reported by: "  + this.reporter + " Reported on: "
                + this.dateReported  + " Date Closed: " + this.dateClosed + " Resolution: "
                + this.resolution);
    }

}

