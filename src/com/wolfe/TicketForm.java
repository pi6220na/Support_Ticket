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
    private JButton deleteOpenTicketButton;
    private JTextField reporterTF;
    private JTextField descriptionTF;
    private JButton addTicketButton;
    private JComboBox priorityCB;
    private JButton quitButton;
    private JTextField resolutionTextField;


    Vector<Ticket> openTicketVector;
    Vector<Ticket> resolvedTicketVector;


    //Models for the JComboBox and JTables.
    OpenTicketTableModel openTicketTableModel;
    ResolvedTicketTableModel resolvedTicketTableModel;
    DefaultComboBoxModel<Integer> priorityCBComboModel;


    public TicketForm() {

        super("Trouble App");

        openTicketVector = TicketManager.getOpenTickets();    // Read all data from a file
        resolvedTicketVector = new Vector<>();

        setContentPane(rootPanel);
        setPreferredSize(new Dimension(1000, 600));   //Set preferred size before call to pack()
        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        configureCombo();



        // Create model for JTables
        openTicketTableModel = new OpenTicketTableModel(openTicketVector);   //Provide Vector of Open Tickets
        resolvedTicketTableModel = new ResolvedTicketTableModel(resolvedTicketVector);


        //Configure each component to use its model
        openTicket.setModel(openTicketTableModel);
        resolvedTicket.setModel(resolvedTicketTableModel);




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
                reporterTF.setText("");
                String descripInput = descriptionTF.getText();
                descriptionTF.setText("");
                Date dateOpened = new Date();

                Ticket t = new Ticket(descripInput, priorInput, reportInput, dateOpened);

                openTicketVector.add(t);

                openTicketTableModel.fireTableDataChanged();
            }
        });


        deleteOpenTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = openTicket.getSelectedRow();
                Ticket t = openTicketVector.get(row);

                // TODO code for getting resolution from user goes here

                String resolu = resolutionTextField.getText();
                resolutionTextField.setText("");

                System.out.println("resolutionTextField = " + resolu);
                t.setResolution(resolu);
                t.setDateClosed(new Date());

                resolvedTicketVector.add(t);

                openTicketVector.remove(row);

                openTicketTableModel.fireTableDataChanged();
                resolvedTicketTableModel.fireTableDataChanged();

            }
        });


        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TicketManager.writeTickets(openTicketVector, resolvedTicketVector);
                System.exit(0);

            }
        });
    } //end Ticketform Class

    private void configureCombo() {

        for (int x = 1 ; x <= 5 ; x++ ) {
                priorityCB.addItem(x);
        }

    }

}
