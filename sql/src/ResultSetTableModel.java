import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class ResultSetTableModel extends AbstractTableModel {


        private Statement statement;
        private ResultSet resultSet;
        private ResultSetMetaData metaData;
        private int numOfRows;


        private boolean connectedDB = false;

        public ResultSetTableModel (Connection c, String query) throws SQLException, ClassNotFoundException{

            statement = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            connectedDB = true;

        }


        public Class getColumnClass (int column) throws IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }


            try {
                String className = metaData.getColumnClassName(column + 1);

                return Class.forName(className);
            }
            catch(Exception exception) {
                exception.printStackTrace();
            }

            return Object.class;
        }



        public int getRowCount() throws IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }

            return numOfRows;
        }


        public int getColumnCount() throws IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }

            try {
                return metaData.getColumnCount();
            }
            catch (SQLException sqlException){
                sqlException.printStackTrace();
            }
            return 0;
        }


        public String getColumnName(int column) throws IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }


            try {
                return metaData.getColumnName(column + 1);
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            return "";
        }


        public Object getValueAt(int rowIndex, int columnIndex) throws IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }


            try {
                resultSet.next();
                resultSet.absolute(rowIndex + 1);
                return resultSet.getObject(columnIndex + 1);
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return "";
        }



        public void setQuery(String query) throws SQLException, IllegalStateException{

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Datase");
            }


            resultSet = statement.executeQuery(query);


            metaData = resultSet.getMetaData();


            resultSet.last();
            numOfRows = resultSet.getRow();


            updateOpLog("num_queries");


            fireTableStructureChanged();
        }



        public void setUpdate(String query) throws SQLException, IllegalStateException{
            int res;

            if (!connectedDB) {
                throw new IllegalStateException("Not connected to Database");
            }


            res = statement.executeUpdate(query);


            updateOpLog("num_updates");


            fireTableStructureChanged();
        }


        public void updateOpLog(String para) {

            Properties prop = new Properties();
            FileInputStream in_file = null;
            MysqlDataSource dataSource = null;
            Connection connectionToOpLog = null;

            try {
                in_file = new FileInputStream("log.properties");
                prop.load(in_file);

                dataSource = new MysqlDataSource();
                dataSource.setUrl(prop.getProperty("DB_URL"));
                dataSource.setUser(prop.getProperty("DB_USERNAME"));
                dataSource.setPassword(prop.getProperty("DB_PASSWORD"));


                connectionToOpLog = dataSource.getConnection();


                Statement opLogsStatement = connectionToOpLog.createStatement();
                opLogsStatement.executeUpdate("UPDATE operationscount set " + para + " = "  + para +" + 1");


                connectionToOpLog.close();
            }
            catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.exit(1);
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        }


    }