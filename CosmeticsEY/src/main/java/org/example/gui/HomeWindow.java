package org.example.gui;
import org.example.init.DataInit;
import org.example.init.TableInit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class HomeWindow extends JFrame {


    public HomeWindow() {
        templateUI();
    }

    private void templateUI() {

        setTitle("EY Cosmetics");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(new JLabel("Welcome to our online beauty store!!"));
        add(mainPanel);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem categoryMenu = new JMenuItem(new AbstractAction("Categories page") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    categoryPanel(mainPanel);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        JMenuItem brandMenu = new JMenuItem(new AbstractAction("Brands page") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    brandsPanel(mainPanel);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        JMenuItem productMenu = new JMenuItem(new AbstractAction("Products page") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    productsPanel(mainPanel);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        JMenuItem searchMenu = new JMenuItem(new AbstractAction("Search page") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    filterPanel(mainPanel);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        menuBar.add(categoryMenu);
        menuBar.add(brandMenu);
        menuBar.add(productMenu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);

    }

    public void categoryPanel(JPanel mainPanel) throws SQLException {
        mainPanel.removeAll();
        CategoryWindow cw = new CategoryWindow(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void brandsPanel(JPanel mainPanel) throws SQLException {
        mainPanel.removeAll();
        BrandWindow bw = new BrandWindow(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void productsPanel(JPanel mainPanel) throws SQLException {
        mainPanel.removeAll();
        ProductWindow pw = new ProductWindow(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void filterPanel(JPanel mainPanel) throws SQLException {
        mainPanel.removeAll();
        FilterWindow fw = new FilterWindow(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    public static void main(String[] args) throws SQLException {
        TableInit.checkForTablesExistence();
        new DataInit().initSampleData();
        HomeWindow hw = new HomeWindow();
        hw.setVisible(true);
    }
}