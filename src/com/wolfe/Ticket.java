/**
 * Created by Jeremy on 10/5/2016.
 */
package com.wolfe;

import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import static com.wolfe.TicketManager.printAllTickets;

public class Ticket {

    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;

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

    public int getTicketID() {
        return ticketID;
    }

    protected int getPriority() {
        return priority;
    }

    public String toString(){
        return("ID= " + this.ticketID + " Issued: " + this.description + " Priority: "
                + this.priority + " Reported by: "
                + this.reporter + " Reported on: " + this.dateReported);
    }


    protected static void deleteTicket(LinkedList<Ticket> ticketQueue) {
        printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        Scanner deleteScanner = new Scanner(System.in);
        System.out.println("Enter ID of ticket to delete");
        int deleteID = deleteScanner.nextInt();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }
        if (found == false) {
            System.out.println("Ticket ID not found, no ticket deleted");
            //TODO – re-write this method to ask for ID again if not found
        }

        printAllTickets(ticketQueue);  //print updated list

    }


}

