package com.wolfe;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseAdapter;
import java.util.Date;
import java.util.Vector;

/**
 *
 * Created by Jeremy on 11/1/2016.
 */
public class TicketGUI extends JFrame {
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
//    private JScrollPane openTicketPane;
//    private JScrollPane resolvedTicketPane;


    Vector<Ticket> openTicketVector;
    Vector<Ticket> resolvedTicketVector;


    //Models for the JComboBox and JTables.
    OpenTicketTableModel openTicketTableModel;
    ResolvedTicketTableModel resolvedTicketTableModel;
    DefaultComboBoxModel<Integer> priorityCBComboModel;


    public TicketGUI() {

        super("Trouble App");

        openTicketVector = TicketIO.getOpenTickets();    // Read all data from a file
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
        openTicket.getColumnModel().getColumn(0).setPreferredWidth(35);
        openTicket.getColumnModel().getColumn(1).setPreferredWidth(35);

        resolvedTicket.getColumnModel().getColumn(0).setPreferredWidth(35);
        resolvedTicket.getColumnModel().getColumn(1).setPreferredWidth(35);

        // center data in first two table columns
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

                // add a new ticket after validating reporter name and description entered
                int priorInput = (Integer) priorityCB.getSelectedItem();   // cast is bad... need alternative
                String reportInput = reporterTF.getText();
                // Validation. Make sure user has entered a name.
                if (reportInput == null || reportInput.length() == 0) {
                    JOptionPane.showMessageDialog(TicketGUI.this, "Please enter a reporter name before adding ticket");
                    return;
                }

                String descripInput = descriptionTF.getText();
                if (descripInput == null || descripInput.length() == 0) {
                    JOptionPane.showMessageDialog(TicketGUI.this, "Please enter a description before adding ticket");
                    return;
                }

                Date dateOpened = new Date();
                Ticket t = new Ticket(descripInput, priorInput, reportInput, dateOpened);

                reporterTF.setText("");
                descriptionTF.setText("");

                openTicketVector.add(t);
                openTicketTableModel.fireTableDataChanged();

            }
        });


        deleteOpenTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // get selected row to be deleted and validate a resolution was entered
                int row = openTicket.getSelectedRow();
                System.out.println("deleting: row = " + row);
                if (row <= 0) {
                    JOptionPane.showMessageDialog(TicketGUI.this, "Please select a ticket before deleting");
                    return;
                }
                Ticket t = openTicketVector.get(row);

                String resolu = resolutionTextField.getText();
                if (resolu == null || resolu.length() == 0) {
                    JOptionPane.showMessageDialog(TicketGUI.this, "Please enter a resolution before deleting ticket");
                    return;
                }

                // add deleted ticket to resolvedticketvector
                t.setResolution(resolu);
                t.setDateClosed(new Date());
                resolvedTicketVector.add(t);

                // delete ticket from open ticket vector
                openTicketVector.remove(row);

                openTicketTableModel.fireTableDataChanged();
                resolvedTicketTableModel.fireTableDataChanged();

                resolutionTextField.setText("");

            }
        });


        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Clara's code from Lake GUI program
                if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(TicketGUI.this, "Are you sure you want to save and exit?", "Exit?", JOptionPane.OK_CANCEL_OPTION)) {

                    TicketIO.writeTickets(openTicketVector, resolvedTicketVector);

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

                TicketIO.writeTickets(openTicketVector, resolvedTicketVector);
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
