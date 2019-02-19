package documentanalyzer;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DocumentView extends JFrame{
    
    private final JPanel main;
    private final JTable table;
    private final JTextField filterField;
    private final JButton wordCloud, open;
    private final JLabel filter;
    private TableModel model;
    private TableColumnModel columns;
    public TableRowSorter<TableModel> tableFilter;
    private final JScrollPane scrollpane;
    
    public DocumentView() {
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
        open = new JButton("Open Document");
        
        filter = new JLabel("Filter:");
        
        main.add(filter);
        main.add(filterField);
        main.add(scrollpane);
        main.add(wordCloud);
        main.add(open);
        
        this.add(main);
        
        this.setVisible(true);
    }
    
    private TableModel createTable() {
        TableModel m = new DefaultTableModel() {
            @Override
            public int getRowCount() {
                return 25;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int row, int col) {
                switch (col) {
                    case 0: return "Document " + row;
                    case 1: return "Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6, Keyword7, Keyword8, Keyword9, Keyword10";
                }
                
                return "";
            }
        };
        
        return m;
    }
    private TableColumnModel createColumns(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        
        columnModel.getColumn(0).setPreferredWidth(250);
        columnModel.getColumn(1).setPreferredWidth(750);
        
        columnModel.getColumn(0).setHeaderValue("Document");
        columnModel.getColumn(1).setHeaderValue("Keywords");
        
        return columnModel;
    }
    
}
