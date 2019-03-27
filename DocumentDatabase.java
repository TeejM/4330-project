package documentanalyzer;

import java.io.File;
import java.sql.*;

public class DocumentDatabase {
    
    String url = "jdbc:mysql://localhost:3306/documents";
    String username = "client";
    String password = "password";
    
    private Statement statement = null;
    private PreparedStatement prepstmt = null;
    private ResultSet rs = null;
    
    
    public DocumentDatabase() {

        DocumentController.addDatabase(this);
        
        System.out.println("Connecting database...");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public void addDocument(String path) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            //System.out.println("Connection successful");
            String query = "INSERT INTO documents (path) VALUES (?)";
            prepstmt = connection.prepareStatement(query);
            prepstmt.setString (1, path);
            
            prepstmt.execute();
            
            System.out.println(path  + " added to table");
            
            connection.close();
            
            //System.out.println("Connection closed");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public String getDocument(int doc_id) {
        String path = null;
        //System.out.println(doc_id);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            //System.out.println("Database connected!");
            
            String query = "SELECT * FROM documents";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String filepath = rs.getString("path");
                if (id == doc_id) {
                    path = filepath;
                    break;
                }
            }
            //System.out.println(path);
            return path;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public void addFolder(String dir_path) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            //System.out.println("Connection successful");
            
            File dir = new File(dir_path);
            File[] dir_list = dir.listFiles();
            
            if (dir_list != null) {
                for(int i = 0; i < dir_list.length; i++) {
                    
                    File file = dir_list[i];
                    
                    String query = "INSERT INTO documents (path) VALUES (?)";
                    prepstmt = connection.prepareStatement(query);
                    prepstmt.setString (1, file.getPath());

                    prepstmt.execute();

                    System.out.println(file.getName() + " added to table");
                }
            }
            connection.close();
            
            //System.out.println("Connection closed");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    //empties the table and resets the auto-increment
    public boolean clearData() {
        
        boolean empty = false;
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            //System.out.println("Database connected!");
            
            //statement to delete all records in table "documents"
            String query = "DELETE FROM documents";
            prepstmt = connection.prepareStatement(query);
            prepstmt.execute();

            //reset auto-increment column
            query = "ALTER TABLE documents AUTO_INCREMENT 1";
            prepstmt = connection.prepareStatement(query);
            prepstmt.execute();
            
            //check if table is empty
            query = "SELECT * FROM documents";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            if(rs.next() == false) {
                System.out.println("Table is empty");
                empty = true;
            }
            else
                System.out.println("Table not empty");
            
            return empty;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
