package documentanalyzer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

//document viewer: displays documents in a table
public class DocumentView extends JFrame{
    
    private final JPanel main;
    private final JTable table;
    private final JTextField filterField;
    private final JButton wordCloud, openDoc;
    private final JLabel filter;
    private TableModel model;
    private TableColumnModel columns;
    public TableRowSorter<TableModel> tableFilter;
    private final JScrollPane scrollpane;
    
    public DocumentView() {
        
        DocumentController.addView(this);
        
        //document viewer specifications including font, features, etc.
        this.setTitle("Document Analyzer");
        this.setLocationRelativeTo(null);
        
        main = new JPanel();
        
        filterField = new JTextField();
        filterField.setPreferredSize(new Dimension(300, 25));
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900,550);
        
        model = createTable();
        table = new JTable(model);
        tableFilter = new TableRowSorter<>(model);
        table.setRowSorter(tableFilter);
        columns = createColumns(table);
        
        table.setFont(new Font("Helvetica", Font.PLAIN, 16));
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        scrollpane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollpane.setPreferredSize(new Dimension(800,400));
        
        wordCloud = new JButton("Word Cloud");
        openDoc = new JButton("Open Document");
        
        filter = new JLabel("Filter:");
        
        main.add(filter);
        main.add(filterField);
        main.add(scrollpane);
        main.add(wordCloud);
        main.add(openDoc);
        
        this.add(main);
        
        this.setVisible(true);
    }
    
	//create a table
    private TableModel createTable() {
        TableModel m = new DefaultTableModel() {
			
			//amount of rows
            @Override
            public int getRowCount() {
                return 2;
            }

			//amount of columns
            @Override
            public int getColumnCount() {
                return 2;
            }

			//retrieves documents searched for
            @Override
            public Object getValueAt(int row, int col) {
                try {
                    switch (col) {
                        case 0: return DocumentController.getDocumentName(row);
                        case 1: return DocumentController.getKeywords(row);
                    }
                } catch (IOException ioe) {
                }
                return "";
            }
        };
        
        return m;
    }
	
	//create table columns
    private TableColumnModel createColumns(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(250);
        columnModel.getColumn(1).setPreferredWidth(750);
        
        columnModel.getColumn(0).setHeaderValue("Document");
        columnModel.getColumn(1).setHeaderValue("Keywords");
        
        return columnModel;
    }
    
    public int getSelectedRow() {
        return table.getSelectedRow();
    }
    
    public String getSelectedName() {
        return (String) table.getValueAt(table.getSelectedRow(), 0);
    }
    
    public String[] getSelectedKeywords() {
        String keywords = (String) table.getValueAt(table.getSelectedRow(), 1);
        return keywords.split(", ");
    }
    
    void addWordCloudListener(ActionListener wc) {
        wordCloud.addActionListener(wc);
    }
    
    void addOpenDocListener(ActionListener od) {
        openDoc.addActionListener(od);
    }
}
