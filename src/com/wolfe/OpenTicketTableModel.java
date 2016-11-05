package com.wolfe;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

/**
 *
 * Created by Jeremy on 11/3/2016.
 */
public class OpenTicketTableModel extends AbstractTableModel {


    private Vector<Ticket> openTicket;

    // Column names, displayed as table headers in the JTable.
    private String[] columnsNames = { "Ticket ID", "Priority", "Reporter", "Description", "Date Reported" } ;

    OpenTicketTableModel(Vector<Ticket> openTickets) {
        openTicket = openTickets;
    }


    @Override
    public int getRowCount() {

        //How many lakeVector? This gives the number of rows.
        return openTicket.size();
    }

    @Override
    public int getColumnCount() {
        //Five columns
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        //Column 0 is for the lake's name

        if (columnIndex == 0) {
            return openTicket.get(rowIndex).getTicketID();
        }

        if (columnIndex == 1) {
            return openTicket.get(rowIndex).getPriority();
        }

        if (columnIndex == 2) {
            return openTicket.get(rowIndex).getReporter();
        }

        if (columnIndex == 3) {
            return openTicket.get(rowIndex).getDescription();
        }

        if (columnIndex == 4) {
            return openTicket.get(rowIndex).getDateReported();
        }

        return null;
    }


    @Override
    public String getColumnName(int col) {

        return columnsNames[col];

    }
}
