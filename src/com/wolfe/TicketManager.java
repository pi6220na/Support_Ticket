/*
 *   Lab 7 Advanced - Ticket Manager
  *  Jeremy Wolfe 10/05/2016
  *
  *  Base code from Week 7 slides
  *
  *  This program supports a support ticket application used by
  *  a tech support person.
  *
  *     TODO add more to description
 */
package com.wolfe;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketManager {

    private static Scanner scanner;   //Global scanner used for all input

    public static void main(String[] args) {

        LinkedList<Ticket> ticketQueue;
        LinkedList<Ticket> resolvedTickets = new LinkedList<Ticket>();

        scanner = new Scanner(System.in);

        ticketQueue = readOpenTicket();
        if (ticketQueue.size() != 0) {
            updateTicketID(ticketQueue);
        }

        boolean quit = false;
        while (!quit) {

            System.out.println("1. Enter Ticket");
            System.out.println("2. Delete Ticket by ID");
            System.out.println("3. Delete Ticket by Issue");
            System.out.println("4. Search by Name (description)");
            System.out.println("5. Display Active Tickets");
            System.out.println("6. Display Resolved Tickets");
            System.out.println("7. Quit");
            int task = getPositiveIntInput();

            switch (task) {

                case 1: {
                    //Call addTickets, which will let us enter any number of new tickets
                    addTickets(ticketQueue);
                    break;
                }

                case 2: {
                    //delete a ticket by ID
                    printAllTickets(ticketQueue);
                    deleteTicketByID(ticketQueue, resolvedTickets);
                    break;
                }

                case 3: {
                    //delete a ticket by Issue
                    deleteTicketByIssue(ticketQueue, resolvedTickets);
                    break;
                }

                case 4: {
                    // Search by Name
                    searchByName(ticketQueue);
                    break;
                }

                case 5: {
                    // display all open tickets
                    printAllTickets(ticketQueue);
                    break;
                }

                case 6: {
                    // display resolved tickets
                    printResolvedTickets(resolvedTickets);
                    break;
                }

                case 7: {
                    // write tickets to files and quit program
                    writeOpenTickets(ticketQueue);
                    writeClosedTickets(resolvedTickets);
                    System.out.println("Quitting program");
                    quit = true;
                    break;
                }

                default: {
                    //this will happen for 3 or any other selection that is a valid int
                    //TODO Program crashes if you enter anything else - please fix
                    //Default will be print all tickets
                    printAllTickets(ticketQueue);
                    System.out.println("Enter a number from the menu choices");
                }
            }
        }
        scanner.close();
    }


    private static void printResolvedTickets(LinkedList<Ticket> resolvedTickets) {

        System.out.println(" ------- All Resolved tickets ----------");

        for (Ticket t : resolvedTickets) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }

    // ask user for word to search Ticket Description on and display all
    // tickets that contain that word
    private static void searchByName(LinkedList<Ticket> ticketQueue) {

        System.out.println("Please enter a word to search for in Ticket description: ");
        String searchFor = getStringInput();

        for (Ticket item : ticketQueue) {
            String tempDesc = item.getDescription();
            // System.out.println("searching item " + item.getTicketID() + " description: " + item.getDescription());
            if (tempDesc.toLowerCase().contains(searchFor.toLowerCase())) {
                System.out.println("Found: " + item);
            }
        }
    }


    private static void deleteTicketByIssue(LinkedList<Ticket> ticketQueue,
                                            LinkedList<Ticket> resolvedQueue) {

        searchByName(ticketQueue);
        deleteTicketByID(ticketQueue, resolvedQueue);

    }


    private static void updateTicketID(LinkedList<Ticket> ticketQueue) {

        /*
        for (Ticket item : ticketQueue) {
            if (item.getTicketID() > Ticket.getStaticTicketIDCounter()) {
                Ticket.setStaticTicketIDCounter(item.getTicketID());
            }
        }
        int bump = Ticket.getStaticTicketIDCounter();
        Ticket.setStaticTicketIDCounter(++bump);
        */

    }

    private static LinkedList<Ticket> readOpenTicket() {

        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();

        File f = new File("open_tickets.ser");
        if(f.exists()) {
            ObjectInputStream objectinputstream = null;
            try {
                FileInputStream streamIn = new FileInputStream("open_tickets.ser");
                objectinputstream = new ObjectInputStream(streamIn);

                int tempID = (int) objectinputstream.readObject();
                System.out.println("in readOpenTicket. tempID = " + tempID);
                Ticket.setStaticTicketIDCounter(tempID);

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
            printAllTickets(ticketQueue);
            return ticketQueue;
        } else {
            ticketQueue = new LinkedList<Ticket>();
            return ticketQueue;
        }
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

            oos.writeObject(Ticket.getStaticTicketIDCounter());

            oos.writeObject(ticketQueue);
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

            System.out.println("resolved filename = " + f);
            fout = new FileOutputStream(f, true);
            oos = new ObjectOutputStream(fout);

            oos.writeObject(resolvedTickets);
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


    // original code moved from Ticket class
    protected static void deleteTicketByID(LinkedList<Ticket> ticketQueue, LinkedList<Ticket> resolvedQueue) {

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        System.out.println("Enter ID of ticket to delete");
        int deleteID = TicketManager.getPositiveIntInput();

        //Loop over all tickets. Delete the one with this ticket ID
        boolean found = false;

        Scanner sc = new Scanner(System.in);
        Date dateClosed = new Date();

        while (!found) {
                for (Ticket ticket : ticketQueue) {
                    if (ticket.getTicketID() == deleteID) {
                        found = true;

                        // update ticket with resolution info
                        System.out.println("Enter the Ticket resolution:");
                        String res = sc.nextLine();
                        ticket.setResolution(res);
                        ticket.setDateClosed(dateClosed);

                        // remove ticket from active queue and move to resolved queue
                        ticketQueue.remove(ticket);
                        resolvedQueue.add(ticket);
                        System.out.println(String.format("Ticket %d deleted", deleteID));
                        break; //don't need loop any more.
                    }
                }
                if (found == false) {
                    System.out.println("Ticket ID not found");
                    System.out.println("Enter ID of ticket to delete");
                    deleteID = TicketManager.getPositiveIntInput();
                }
            } // end while

        System.out.println("Updated ticket queue:");
        printAllTickets(ticketQueue);  //print updated list

    }

    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }


    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }

    //Validation methods copied from ??? TODO figure out where this came from

    protected static int getPositiveIntInput() {

        while (true) {
            try {
                String stringInput = scanner.nextLine();
                int intInput = Integer.parseInt(stringInput);
                if (intInput >= 0) {
                    return intInput;
                } else {
                    System.out.println("Please enter a positive number");
                    continue;
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please type a positive number");
            }
        }

    }

    protected static double getPositiveDoubleInput() {

        while (true) {
            try {
                String stringInput = scanner.nextLine();
                double doubleInput = Double.parseDouble(stringInput);
                if (doubleInput >= 0) {
                    return doubleInput;
                } else {
                    System.out.println("Please enter a positive number");
                    continue;
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please type a positive number");
            }
        }

    }

    protected static String getStringInput() {

        String entry = scanner.nextLine();
        return entry;

    }

}


