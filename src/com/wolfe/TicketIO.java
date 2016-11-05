/*
 *  Lab 9 - Support Ticket GUI
 *
 *
 *   This IO module was adapted from:
 *   Lab 7 Advanced - Ticket Manager
  *  Jeremy Wolfe 10/05/2016
  *
  *  Base code from Week 7 slides
  *
  *  This program supports a trouble ticket application used by
  *  a tech support person.
  *
  *
 */
package com.wolfe;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketIO {

    static LinkedList<Ticket> ticketQueue;
    static LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();


    // ******* convert ticketQueue to Vector *************
    static Vector<Ticket> getOpenTickets() {

        ticketQueue = readOpenTicket();

        Vector<Ticket> tickets = new Vector<>();

        for (int j = 0; j < ticketQueue.size(); j++) {

            Ticket t = ticketQueue.get(j);
            tickets.add(t);

        }

        return tickets;
    }

    // this method reads the saved (from last program execution) ticket data file
    // and populates the LinkedLink object.
    // Note that the first read gets an integer representing the next ticketID to use.
    private static LinkedList<Ticket> readOpenTicket() {

        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();

        File f = new File("open_tickets.ser");
        if(f.exists()) {
            ObjectInputStream objectinputstream = null;
            try {
                FileInputStream streamIn = new FileInputStream("open_tickets.ser");
                objectinputstream = new ObjectInputStream(streamIn);

                // read and set the static ticketID first
                int tempID = (int) objectinputstream.readObject();
                // System.out.println("in readOpenTicket. tempID = " + tempID);
                Ticket.setStaticTicketIDCounter(tempID);

                // load the ticketQueue from serialized file object
                // unchecked cast??? not sure what this means, will continue to research
                ticketQueue = (LinkedList<Ticket>) objectinputstream.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(objectinputstream != null){
                    try {
                        objectinputstream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ticketQueue;
        } else {
            // if no saved tickets on file, create an empty LinketLink
            ticketQueue = new LinkedList<Ticket>();
            return ticketQueue;
        }
    }


    // ******* convert vectors to ticketQueues  *************
    static void writeTickets(Vector<Ticket> openTicketV, Vector<Ticket> resolvedTicketV) {

        ticketQueue.clear();
        resolvedTickets.clear();

        for (int i = 0; i < openTicketV.size(); i++) {

            Ticket t = openTicketV.get(i);
            ticketQueue.add(t);
            System.out.println("added ticket to open queue = " + t);

        }

        for (int i = 0; i < resolvedTicketV.size(); i++) {

            Ticket t = resolvedTicketV.get(i);
            resolvedTickets.add(t);
            System.out.println("added ticket to resolved queue = " + t);

        }

        writeOpenTickets(ticketQueue);
        writeClosedTickets(resolvedTickets);
    }




    // write ticketQueue list to file
    private static void writeOpenTickets(LinkedList<Ticket> ticketQueue) {
        // adapted from: http://stackoverflow.com/questions/17293991/how-to-write
        // -and-read-java-serialized-objects-into-a-file
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try{
            //fout = new FileOutputStream("open_tickets.ser", true);
            fout = new FileOutputStream("open_tickets.ser");
            oos = new ObjectOutputStream(fout);

            // save the current static ticketID
            oos.writeObject(Ticket.getStaticTicketIDCounter());

            // write out a serialized ticketQueue
            oos.writeObject(ticketQueue);
            System.out.println("Saving ticket information to file \"open_tickets.ser\"");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(oos  != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // write out resolved tickets to a resolved ticket file with the file name appended with the current
    // date. Data will be appended to the file for multiple program executions on the same day.
    private static void writeClosedTickets(LinkedList<Ticket> resolvedTickets) {

        // jump out if no resolved tickets to write out
        if (resolvedTickets.size() == 0) {
            return;
        }

        // adapted from: http://stackoverflow.com/questions/17293991/how-to-write
        // -and-read-java-serialized-objects-into-a-file
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try{

            // https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
            Date dateClose = new Date();
            String f = "Resolved_tickets_as_of_" + dateFormat.format(dateClose);

            // System.out.println("resolved filename = " + f);
            fout = new FileOutputStream(f, true);
            oos = new ObjectOutputStream(fout);

            oos.writeObject(resolvedTickets);
            System.out.println("Saving resolved ticket information to file \"Resolved_tickets....ser\"");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(oos  != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


} // end class TicketIO


