/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package georgii.proyecto2evaluacion_georgiisytnik.vista;

import com.mongodb.client.*;
import georgii.proyecto2evaluacion_georgiisytnik.dao.FoodDao;
import georgii.proyecto2evaluacion_georgiisytnik.dao.FoodDaoImpl;
import georgii.proyecto2evaluacion_georgiisytnik.dao.SlimeDao;
import georgii.proyecto2evaluacion_georgiisytnik.dao.SlimeDaoImpl;
import georgii.proyecto2evaluacion_georgiisytnik.modelo.Slime;
import org.bson.Document;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Interfaz gráfica JFrame para trabajar con MongoDB (Slimes y Food)
 * Convierte el ejemplo por consola en una aplicación visual con más posibilidades.
 */
public class SlimeMongoApp extends JFrame {

    private static final String MONGO_URI = "mongodb://localhost:27017";

    private JTextArea outputArea;
    private JComboBox<String> slimeTypeCombo;
    private JButton btnSpecialSlimes;
    private JButton btnSlimesWithFood;
    private JButton btnCountSlimes;
    private JButton btnCountFood;

    private SlimeDao slimeDao;
    private FoodDao foodDao;

    public SlimeMongoApp() {
        setTitle("Slime & Food Manager (MongoDB)");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initMongo();
        initUI();
    }

    private void initMongo() {
        MongoClient client = MongoClients.create(MONGO_URI);
        MongoDatabase database = client.getDatabase("SlimeAndFood");
        slimeDao = new SlimeDaoImpl(database);
        foodDao = new FoodDaoImpl(database);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        JLabel title = new JLabel("Gestión de Slimes y Comida (MongoDB)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Consultas"));

        slimeTypeCombo = new JComboBox<>(new String[]{"special", "hostile", "docile", "harmful"});
        
        

        btnSpecialSlimes = new JButton("Slimes por tipo");
        btnSlimesWithFood = new JButton("Slimes con su comida");
        btnCountSlimes = new JButton("Contar slimes por tipo");
        btnCountFood = new JButton("Contar comida por tipo");

        leftPanel.add(new JLabel("Tipo de slime:"));
        leftPanel.add(slimeTypeCombo);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnSpecialSlimes);
        leftPanel.add(btnSlimesWithFood);
        leftPanel.add(btnCountSlimes);
        leftPanel.add(btnCountFood);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado"));
        mainPanel.add(scroll, BorderLayout.CENTER);

        btnSpecialSlimes.addActionListener(e -> showSlimesByType());
        btnSlimesWithFood.addActionListener(e -> showSlimesWithFoods());
        btnCountSlimes.addActionListener(e -> countSlimes());
        btnCountFood.addActionListener(e -> countFoods());
    }

    private void showSlimesByType() {
        outputArea.setText("");
        String type = (String) slimeTypeCombo.getSelectedItem();
        List<Slime> slimes = slimeDao.findByType(type);
        outputArea.append("Slimes de tipo '" + type + "'\n\n");
        slimes.forEach(s -> outputArea.append(s + "\n"));
    }

    private void showSlimesWithFoods() {
        outputArea.setText("Slimes con toda su comida:\n\n");
        List<Document> docs = slimeDao.findSlimesWithFoods();
        for (Document d : docs) {
            outputArea.append(d.toJson() + "\n\n");
        }
    }

    private void countSlimes() {
        outputArea.setText("Cantidad de slimes por tipo:\n\n");
        Map<String, Integer> map = slimeDao.countSlimesByType();
        map.forEach((k, v) -> outputArea.append(k + " -> " + v + "\n"));
    }

    private void countFoods() {
        outputArea.setText("Cantidad de comida por tipo:\n\n");
        Map<String, Integer> map = foodDao.countFoodsByType();
        map.forEach((k, v) -> outputArea.append(k + " -> " + v + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new SlimeMongoApp().setVisible(true);
        });
    }
}
