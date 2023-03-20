import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class myFrame extends JPanel implements ActionListener {

    static final long serialVersionUID = 1L;

    JButton Con_Button = new JButton();
    JButton Clear_sql_Button = new JButton();
    JButton Exec_Button = new JButton();
    JButton Clear_wind_Button = new JButton();

    JLabel command_Label = new JLabel();
    JLabel Properties_Label = new JLabel();
    JLabel user_Label = new JLabel();
    JLabel pass_Label = new JLabel();
    JLabel stat_Label = new JLabel();
    JLabel result_Label = new JLabel();
    JLabel details_Label = new JLabel();


    JComboBox<String> prop_combo = new JComboBox();
    JTextField user_field = new JTextField();
    JTextField connection_stat = new JTextField();
    JPasswordField pass_field = new JPasswordField();
    JTextArea sqlCommand_area = new JTextArea(6, 6);


    TableModel table;


    JTextArea textSql = new JTextArea(6, 6);

    Connection connection;
    boolean connected = false;

    ResultSetTableModel tableModel;
    JTable showTable;
    JScrollPane box;


    public myFrame() {
        this.setLayout(null);


        this.add(Con_Button);
        this.add(Clear_sql_Button);
        this.add(Exec_Button);
        this.add(Clear_wind_Button);
        setUpButtons();


        this.add(command_Label);
        this.add(Properties_Label);
        this.add(user_Label);
        //this.add(stat_Label);
        this.add(details_Label);
        this.add(result_Label);
        this.add(pass_Label);
        setUpLabels();


        this.add(prop_combo);
        this.add(user_field);
        this.add(pass_field);
        this.add(sqlCommand_area);
        this.add(connection_stat);
        setUpField();

        setUpTable();
        this.add(box);


        this.setBackground(Color.GRAY);
        Con_Button.addActionListener(this);
        Clear_wind_Button.addActionListener(this);
        Clear_sql_Button.addActionListener(this);
        Exec_Button.addActionListener(this);


    }

    void setUpButtons() {

        Con_Button.setText("Connect to DataBase");
        Con_Button.setBounds(20, 180, 180, 35);

        Clear_sql_Button.setText("Clear SQL Command");
        Clear_sql_Button.setBounds(450, 180, 180, 35);


        Exec_Button.setText("Execute SQL Command");
        Exec_Button.setBounds(650, 180, 180, 35);


        Clear_wind_Button.setText("Clear Result Window");
        Clear_wind_Button.setOpaque(false);
        Clear_wind_Button.setBounds(20, 620, 180, 35);


    }

    void setUpLabels() {

        command_Label.setText("Enter An SQL Command");
        command_Label.setBounds(450, 10, 200, 25);
        command_Label.setFont(new Font("Arial", Font.BOLD, 16));

        Properties_Label.setText("Properties File");
        Properties_Label.setBounds(20, 45, 165, 25);
        Properties_Label.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, 14));


        user_Label.setText("Username");
        user_Label.setBounds(20, 80, 165, 25);
        user_Label.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, 14));


        details_Label.setText("Connection Details");
        details_Label.setBounds(20, 10, 165, 25);
        details_Label.setFont(new Font("Arial", Font.BOLD, 16));


        result_Label.setText("SQL Execution Result Window");
        result_Label.setBounds(20, 270, 800, 25);
        result_Label.setFont(new Font("Arial", Font.BOLD, 16));


        pass_Label.setText("Password");
        pass_Label.setBounds(20, 115, 165, 25);
        pass_Label.setFont(new Font("Arial", Font.LAYOUT_LEFT_TO_RIGHT, 14));


    }

    void setUpField() {

        prop_combo.setBounds(130, 45, 165, 25);

        prop_combo.addItem("root.properties");
        prop_combo.addItem("client.properties");


        user_field.setBounds(100, 80, 165, 25);


        pass_field.setBounds(100, 115, 165, 25);

        sqlCommand_area.setBounds(450, 40, 400, 120);

        connection_stat.setBounds(0, 225, 900, 40);

        connection_stat.setText("   NO CONNECTION NOW");
        connection_stat.setForeground(Color.RED);

        connection_stat.setBackground(Color.BLACK);

    }


    void setUpTable() {


        showTable = new JTable(tableModel);
        box = new JScrollPane(showTable);
        box.setBounds(0, 300, 900, 300);
        box.setOpaque(true);
        box.setBackground(Color.white);


        showTable.setBounds(0, 300, 400, 500);
        showTable.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(textSql, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        textSql.setWrapStyleWord(true);
        textSql.setLineWrap(true);


    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Con_Button) {

            try {
                if (connection != null) {
                    connection.close();
                }
                Properties properties = new Properties();
                FileInputStream in_file = null;
                MysqlDataSource dataSource = null;


                try {
                    in_file = new FileInputStream((String) prop_combo.getSelectedItem());
                    properties.load(in_file);
                    dataSource = new MysqlDataSource();
                    dataSource.setUrl(properties.getProperty("DB_URL"));


                    if (user_field.getText().equals((String) properties.getProperty("DB_USERNAME")) &&
                            pass_field.getText().equals((String) properties.getProperty("DB_PASSWORD"))) {


                        dataSource.setUser((String) properties.getProperty("DB_USERNAME"));
                        dataSource.setPassword((String) properties.getProperty("DB_PASSWORD"));
                        connection = dataSource.getConnection();
                        connection_stat.setForeground(Color.GREEN);
                        connection_stat.setText("CONNECTED TO: " + (String) properties.getProperty("DB_URL"));

                    } else {
                        connection_stat.setForeground(Color.RED);
                        connection_stat.setText("NOT CONNECTED - User Credentials Do Not Match Properties File");
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        if (e.getSource() == Clear_wind_Button) {

            table = new DefaultTableModel();
            showTable.setModel(table);


        }

        if (e.getSource() == Clear_sql_Button) {

            sqlCommand_area.setText("");

        }

        if (e.getSource() == Exec_Button) {
            try {

                showTable.setAutoscrolls(true);
                tableModel = new ResultSetTableModel(connection, sqlCommand_area.getText());


                if (sqlCommand_area.getText().toLowerCase().startsWith("select")) {
                    tableModel.setQuery(sqlCommand_area.getText());
                    showTable.setModel(tableModel);
                } else {
                    tableModel.setUpdate(sqlCommand_area.getText());
                    table = new DefaultTableModel();
                    showTable.setModel(table);
                }
            } catch (SQLException ex) { // catch database error

                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException NotFound) { // catch driver error
                JOptionPane.showMessageDialog(null, "Driver not found", "Driver not found", JOptionPane.ERROR_MESSAGE);
            }

        }


    }


}



