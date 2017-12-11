package com.neuSep17.ui;

import com.neuSep17.dao.IncentiveImple;
import com.neuSep17.dto.Dealer;
import com.neuSep17.dto.Incentive;
import com.neuSep17.service.IncentiveService;

import java.awt.EventQueue;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ManageIncentivesUI extends IncentiveUI {
    private static JFrame mainFrame;
    private JPanel headerPanel;
    private JPanel footerPanel;
    private JPanel listPanel;
    private JPanel sortPanel;
    private JButton addButton;
    private JLabel[] label;
    private JButton[] editButtons;
    private JButton[] deleteButtons;
    private JPanel[] jp;
    private ImageIcon[] ii;
    private boolean asc = false;
    private String sortoption = "";
    private String searchcontent = "";
    private JLabel[] imageLabel;
    private JButton searchButton;
    private int length;
    public ArrayList<Incentive> incentives;//to save incentives read from file
    public ArrayList<Incentive> Allincentives;
    private static String dealerid;
    private JComboBox jsort;
    private JButton searchby;
    private JLabel sort;
    private JComboBox order;
    private JTextField jtf;
    private JButton viewAll;

    //  private IncentiveImple;
    public ManageIncentivesUI(String dealerid) throws FileNotFoundException {
        this.dealerid = dealerid;
        mainFrame = new JFrame();
        initialize();
        createComponents();
        addComponents();
        addListeners();
        makeThisVisible();
    }

    private void initialize() throws FileNotFoundException {
        IncentiveImple iImple = new IncentiveImple();
        incentives = iImple.getIncentivesForDealer(dealerid);
        Allincentives = incentives;
        length = incentives.size();
        label = new JLabel[length];
        editButtons = new JButton[length];
        deleteButtons = new JButton[length];
        jp = new JPanel[length];
        ii = new ImageIcon[length];
        imageLabel = new JLabel[length];
        mainFrame.setTitle("Incentives Management");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setBackground(Color.lightGray);
        mainFrame.setLayout(new BorderLayout());
    }

    public void createComponents() {
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(2, 1, 10, 10));

        footerPanel = new JPanel();

        listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(length, 1, 10, 10));
    }

    public void addComponents() {
        // Header
        JLabel headerTitle = new JLabel("IncentiveUtilities Lists and Management");
        JPanel title = new JPanel();
        title.add(headerTitle);
        sortPanel = new JPanel();
        sort = new JLabel("Sort by");
        jsort = createComboBox(new String[]{"ID", "Title", "StartDate", "EndDate", "Discount"});
        jtf = new JTextField(5);
        searchButton = new JButton("Search");
        viewAll = new JButton("View All");
        order = createComboBox(new String[]{"asc", "desc"});
        JLabel searchby = new JLabel("Search by ID");
        sortPanel.add(sort);
        sortPanel.add(jsort);
        sortPanel.add(order);
        sortPanel.add(searchby);
        sortPanel.add(jtf);
        sortPanel.add(searchButton);
        sortPanel.add(viewAll);
        headerPanel.add(title);
        headerPanel.add(sortPanel);
        mainFrame.add(headerPanel, BorderLayout.NORTH);


        // Add button
        addButton = createButton("Add New Incentives");
        footerPanel.add(addButton);
        mainFrame.add(footerPanel, BorderLayout.SOUTH);

        //add listpanel,according to sort and search
        displayIncentives(incentives);

        mainFrame.add(listPanel, BorderLayout.CENTER);
    }

    private void displayIncentives(ArrayList<Incentive> incentives) {
        length = incentives.size();
        for (int i = 0; i < length; i++) {
            jp[i] = new JPanel();
            ii[i] = createImageIcon("C:\\Users\\diksh\\Desktop\\NeuSep17-Project-Group1-master-src\\NeuSep17-Project-Group1-master\\picture\\flame.jpg");
            imageLabel[i] = new JLabel(ii[i]);
            imageLabel[i].setBounds(0, 0, 24, 31);
            jp[i].add(imageLabel[i], new Integer(Integer.MIN_VALUE));
            String content = "ID" + " " + incentives.get(i).getID() + ", " + incentives.get(i).getDescription();
            // label[i]=new JLabel("IncentiveUtilities ID"+" "+incentives.get(i).getID()+", "+incentives.get(i).getDescription());
            if (content.length() > 30)
                content = content.substring(0, 23) + "...";
            label[i] = new JLabel(content);
            label[i].setForeground(Color.blue);
            editButtons[i] = new JButton("Edit");
            deleteButtons[i] = new JButton("Delete");

            editButtons[i].setActionCommand("" + i);
            deleteButtons[i].setActionCommand("" + i);

            editButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String num = e.getActionCommand();
                    int index = Integer.parseInt(num);
                    Incentive incentive = incentives.get(index);
                    IncentiveUtilities i = new IncentiveUtilities();
                    i.updateIncentive(incentive);
                    i.EditIncentives(i);

                    mainFrame.dispose();
                    try {
                        ManageIncentivesUI screen = new ManageIncentivesUI(ManageIncentivesUI.dealerid);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            deleteButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String num = e.getActionCommand();
                    int index = Integer.parseInt(num);
                    String incentiveId = incentives.get(index).getID();
                    IncentiveService.deleteAnIncentive(incentiveId);
                }
            });

            jp[i].add(label[i]);
            jp[i].add(editButtons[i]);
            jp[i].add(deleteButtons[i]);
            listPanel.add(jp[i]);
        }
    }

    public void addListeners() {
        jsort.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortoption = (String) jsort.getSelectedItem();
                    boolean isAsc = true;
                    if (order.getSelectedItem().toString().equals("asc"))
                        isAsc = true;
                    else
                        isAsc = false;
                    incentives = selectSortCriteria(incentives, isAsc, sortoption);
                    listPanel.removeAll();
                    listPanel.revalidate();
                    listPanel.repaint();
                    displayIncentives(incentives);
                    listPanel.setVisible(true);
                }
            }
        });

        order.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortoption = (String) jsort.getSelectedItem();
                    boolean isAsc = true;
                    if (order.getSelectedItem().toString().equals("asc"))
                        isAsc = true;
                    else
                        isAsc = false;
                    incentives = selectSortCriteria(incentives, isAsc, sortoption);
                    listPanel.removeAll();
                    listPanel.revalidate();
                    listPanel.repaint();
                    displayIncentives(incentives);
                    listPanel.setVisible(true);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = jtf.getText();
                incentives = IncentiveService.searchIncentives(incentives, text);
                listPanel.removeAll();
                listPanel.revalidate();
                listPanel.repaint();
                displayIncentives(incentives);
                listPanel.setVisible(true);
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                searchButton.setForeground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e) {
                searchButton.setCursor(Cursor.getDefaultCursor());
                searchButton.setForeground(Color.BLACK);
            }
        });

        for (int i = 0; i < length; i++) {
            final JButton editButton = editButtons[i];
            editButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    editButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    editButton.setForeground(Color.GRAY);
                }

                public void mouseExited(MouseEvent e) {
                    editButton.setCursor(Cursor.getDefaultCursor());
                    editButton.setForeground(Color.BLACK);
                }

                @Override
                public void mouseClicked(MouseEvent arg0) {
                    mainFrame.dispose();
                    try {
                        ManageIncentivesUI screen = new ManageIncentivesUI(ManageIncentivesUI.dealerid);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            final JButton deleteButton = deleteButtons[i];
            deleteButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    deleteButton.setForeground(Color.GRAY);
                }

                public void mouseExited(MouseEvent e) {
                    deleteButton.setCursor(Cursor.getDefaultCursor());
                    deleteButton.setForeground(Color.BLACK);
                }

                @Override
                public void mouseClicked(MouseEvent arg0) {
                    mainFrame.dispose();
                    try {
                        ManageIncentivesUI screen = new ManageIncentivesUI(ManageIncentivesUI.dealerid);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        addButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addButton.setForeground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e) {
                addButton.setCursor(Cursor.getDefaultCursor());
                addButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    IncentiveUtilities incentiveUtilities = new IncentiveUtilities();
                    incentiveUtilities.addIncentives();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewAll.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                viewAll.setForeground(Color.GRAY);
            }

            public void mouseExited(MouseEvent e) {
                viewAll.setCursor(Cursor.getDefaultCursor());
                viewAll.setForeground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                displayIncentives(Allincentives);
            }
        });
    }

    private void makeThisVisible() {
        mainFrame.setSize(2000, 2000);//make it smaller and in the middle of the screen
        IncentiveUI ui = new IncentiveUI();
        ui.displayCenter(mainFrame);
        mainFrame.setBackground(Color.white);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageIncentivesUI screen = new ManageIncentivesUI("gmps-bresee");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ArrayList<Incentive> selectSortCriteria(ArrayList<Incentive> incentives, boolean isAsc, String criteria) {
        switch (criteria) {
            case "ID":
                return IncentiveService.sortIncentivesByIncentID(incentives,isAsc);
            case "Title" :
                return IncentiveService.sortIncentivesByTitle(incentives,isAsc);
            case "StartDate":
                return IncentiveService.sortIncentivesByStartDate(incentives,isAsc);
            case "EndDate":
                return IncentiveService.sortIncentivesByEndDate(incentives,isAsc);
        }
        return incentives;
    }
}
