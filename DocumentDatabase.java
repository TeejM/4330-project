package documentanalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.swing.JFileChooser;

public class DocumentDatabase {
    
    String url = "jdbc:mysql://mydbinstance.cat14r8phmlw.us-east-1.rds.amazonaws.com:3306/documents";
    String username = "tjmathews";
    String password = "4330groupproject";
    
    private Statement statement = null;
    private PreparedStatement prepstmt = null;
    private ResultSet rs = null;
    private String directory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    private Connection connection;
    
    public DocumentDatabase() {

        DocumentController.addDatabase(this);
        
        System.out.println("Connecting database...");

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public void addDocument(String path) {
        try {
            String query = "INSERT INTO documents (path) VALUES (?)";
            prepstmt = connection.prepareStatement(query);
            prepstmt.setString (1, path);
            
            prepstmt.execute();
            
            System.out.println(path  + " added to table");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public String getName (int doc_id)
    {
        String name = null;
        try {        
            String query = "SELECT * FROM documents";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                int id = rs.getInt("id");
                if (id == doc_id) {
                    name = rs.getString("name");
                    break;
                }
            }

            return name;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public File getDocument(int doc_id) throws FileNotFoundException, IOException {
        String path = null;
        File file = null;
        FileOutputStream fos;
        byte[] buffer;
        InputStream is;
        try {
            String query = "SELECT * FROM documents";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                if (id == doc_id) {
                    file = new File(directory + name);
                    fos = new FileOutputStream(file);
                    
                    buffer = new byte[1];
                    is = rs.getBinaryStream(3);
                    while(is.read(buffer) > 0)
                    {
                        fos.write(buffer);
                    }
                    fos.close();
                    break;
                }
            }
            return file;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    public void addFolder(String dir_path) throws FileNotFoundException {
        try {
            File dir = new File(dir_path);
            FileInputStream fis;
            File[] dir_list = dir.listFiles();
            
            if (dir_list != null) {
                for(int i = 0; i < dir_list.length; i++) {
                    
                    File file = dir_list[i];
                    fis = new FileInputStream(file);
                    
                    String query = "INSERT INTO documents (name, file) VALUES (?, ?)";
                    prepstmt = connection.prepareStatement(query);
                    prepstmt.setString(1, file.getName());
                    prepstmt.setBinaryStream (2, fis, (int) file.length());

                    prepstmt.execute();

                    System.out.println(file.getName() + " added to table");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    
    //empties the table and resets the auto-increment
    public boolean clearData() {
        
        boolean empty = false;
        try {
            
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
