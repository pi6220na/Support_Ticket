package com.wolfe;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 * Created by myrlin on 11/3/2016.
 */
public class ResolvedTicketTableModel extends AbstractTableModel {


    private Vector<Ticket> resolvedTicket;

    // Column names, displayed as table headers in the JTable.
    private String[] columnsNames = { "Ticket ID", "Priority", "Reporter", "Description", "Date Reported", "Resolution", "Date Closed" } ;

    ResolvedTicketTableModel(Vector<Ticket> resolvedTickets) {
        resolvedTicket = resolvedTickets;
    }


    @Override
    public int getRowCount() {

        //How many lakeVector? This gives the number of rows.
        return resolvedTicket.size();
    }

    @Override
    public int getColumnCount() {
        //seven columns
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        //Column 0 is for the lake's name

        if (columnIndex == 0) {
            return resolvedTicket.get(rowIndex).getTicketID();
        }

        if (columnIndex == 1) {
            return resolvedTicket.get(rowIndex).getPriority();
        }

        if (columnIndex == 2) {
            return resolvedTicket.get(rowIndex).getReporter();
        }

        if (columnIndex == 3) {
            return resolvedTicket.get(rowIndex).getDescription();
        }

        if (columnIndex == 4) {
            return resolvedTicket.get(rowIndex).getDateReported();
        }

        if (columnIndex == 5) {
            return resolvedTicket.get(rowIndex).getResolution();
        }

        if (columnIndex == 6) {
            return resolvedTicket.get(rowIndex).getDateClosed();
        }


        return null;
    }


    @Override
    public String getColumnName(int col) {

        return columnsNames[col];

    }

}
