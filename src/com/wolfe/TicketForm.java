package com.wolfe;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        configureCombo();



        // Create model for JTables
        openTicketTableModel = new OpenTicketTableModel(openTicketVector);   //Provide Vector of Open Tickets
        resolvedTicketTableModel = new ResolvedTicketTableModel(resolvedTicketVector);


        //Configure each component to use its model
        openTicket.setModel(openTicketTableModel);
        resolvedTicket.setModel(resolvedTicketTableModel);


        // set column width JTables first two columns... doesn't seem to be working
        openTicket.getColumnModel().getColumn(0).setPreferredWidth(10);
        openTicket.getColumnModel().getColumn(1).setPreferredWidth(10);
        resolvedTicket.getColumnModel().getColumn(0).setPreferredWidth(10);
        resolvedTicket.getColumnModel().getColumn(1).setPreferredWidth(10);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        openTicket.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        openTicket.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        resolvedTicket.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        resolvedTicket.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );

        Integer[] priority = { 1,2,3,4,5 };

        JComboBox<Integer> prior = new JComboBox<Integer>(priority);
//        prior.addActionListener(this);


        // listeners here


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

                // Clara's code from Lake GUI program
                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(TicketForm.this, "Are you sure you want to save and exit?", "Exit?", JOptionPane.OK_CANCEL_OPTION)) {

                    TicketManager.writeTickets(openTicketVector, resolvedTicketVector);

                    //http://stackoverflow.com/questions/258099/how-to-close-a-java-swing-application-from-the-code
                    Container frame = quitButton.getParent();
                    do
                        frame = frame.getParent();
                    while (!(frame instanceof JFrame));
                    ((JFrame) frame).dispose();

                    System.exit(0);

                }

            }
        });

        // close app and window from exit "X" button on window title bar
        //https://www.clear.rice.edu/comp310/JavaResources/frame_close.html
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {

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
