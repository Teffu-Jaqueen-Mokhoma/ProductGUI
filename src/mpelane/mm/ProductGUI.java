package mpelane.mm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import mpelane.mm.Product;

public class ProductGUI extends JFrame implements ActionListener {
    private JLabel productIdLabel, productNameLabel, priceLabel;
    private JTextField productIdField, productNameField, priceField;
    private JButton addButton, displayButton;
    private JTextArea displayArea;

    public ProductGUI() {
        setTitle("Product Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));

        productIdLabel = new JLabel("Product ID:");
        productNameLabel = new JLabel("Product Name:");
        priceLabel = new JLabel("Price:");

        productIdField = new JTextField(10);
        productNameField = new JTextField(10);
        priceField = new JTextField(10);

        addButton = new JButton("Add Product");
        addButton.addActionListener(this);
        displayButton = new JButton("Display Products");
        displayButton.addActionListener(this);

        displayArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(productIdLabel);
        inputPanel.add(productIdField);
        inputPanel.add(productNameLabel);
        inputPanel.add(productNameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addProduct();
        } else if (e.getSource() == displayButton) {
            displayProducts();
        }
    }

    private void addProduct() {
        int productId = Integer.parseInt(productIdField.getText());
        String productName = productNameField.getText();
        double price = Double.parseDouble(priceField.getText());

        Product product = new Product(productId, productName, price);

        // Saving the product to a text file
        try (PrintWriter writer = new PrintWriter(new FileWriter("products.txt", true))) {
            writer.println(product.getProductId() + "," + product.getProductName() + "," + product.getPrice());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving product to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        displayArea.append("Product added: \n");
        displayArea.append("ID: " + product.getProductId() + "\n");
        displayArea.append("Name: " + product.getProductName() + "\n");
        displayArea.append("Price: R" + product.getPrice() + "\n\n");
    }

    private void displayProducts() {
        displayArea.setText(""); 
        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int productId = Integer.parseInt(parts[0]);
                String productName = parts[1];
                double price = Double.parseDouble(parts[2]);
                Product product = new Product(productId, productName, price);
                displayArea.append("Product ID: " + product.getProductId() + "\n");
                displayArea.append("Product Name: " + product.getProductName() + "\n");
                displayArea.append("Price: R" + product.getPrice() + "\n\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading products from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProductGUI();
            }
        });
    }
}