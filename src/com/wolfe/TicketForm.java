package com.wolfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.Date;
import java.util.Vector;

/**
 * Created by myrlin on 11/1/2016.
 */
public class TicketForm extends JFrame {
    private JPanel rootPanel;
    private JTable resolvedTicket;
    private JTable openTicket;
    private JTextField openTicketsTextField;
    private JTextField resolvedTicketsTextField;
    private JButton deleteTicketButton;
    private JTextField reporterTF;
    private JTextField descriptionTF;
    private JButton addTicketButton;
    private JComboBox priorityCB;


    Vector<Ticket> openTicketVector;
    Vector<Ticket> resolvedTicketVector;


    //Models for the JComboBox and JTables.
    OpenTicketTableModel openTicketTableModel;
    ResolvedTicketTableModel resolvedTicketTableModel;
    DefaultComboBoxModel<Integer> priorityCBComboModel;


    public TicketForm() {

        super("Trouble App");

        openTicketVector = TicketManager.getOpenTickets();    // Read all data from a file

        setContentPane(rootPanel);
        setPreferredSize(new Dimension(1000, 600));   //Set preferred size before call to pack()
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        configureCombo();


        // Create model for best times JTable
        openTicketTableModel = new OpenTicketTableModel(openTicketVector);   //Provide Vector of Open Tickets

        //Configure each component to use its model
        openTicket.setModel(openTicketTableModel);


        Integer[] priority = { 1,2,3,4,5 };

        JComboBox<Integer> prior = new JComboBox<Integer>(priority);
//        prior.addActionListener(this);


        priorityCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prior.addActionListener(this);

            }
        });


        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int priorInput = (Integer) priorityCB.getSelectedItem();   // cast is bad... need alternative
                String reportInput = reporterTF.getText();
                String descripInput = descriptionTF.getText();
                Date dateOpened = new Date();

                Ticket t = new Ticket(descripInput, priorInput, reportInput, dateOpened);

                openTicketVector.add(t);

                openTicketTableModel.fireTableDataChanged();
            }
        });
    }

    private void configureCombo() {

        for (int x = 1 ; x <= 5 ; x++ ) {
                priorityCB.addItem(x);
        }

    }

}
