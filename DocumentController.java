package documentanalyzer;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public final class DocumentController {
    
    static DocumentView dv;
    static DocumentDatabase db;
    
    private DocumentController() {
    }
    
    public static void addView(DocumentView dv) {
        DocumentController.dv = dv;
    }
    
    public static void addDatabase(DocumentDatabase db) {
        DocumentController.db = db;
    }
    
    public static String getDocumentName(int doc_id) {
        String path = db.getDocument(doc_id + 1);
        return new File(path).getName();
    }
    
    public static String getDocument(int doc_id) {
        return db.getDocument(doc_id);
    }
    
    public static String getKeywords(int doc_id) throws IOException{
        String[] keywords = ParseText.sortByFrequency(ParseText.findKeywords(db.getDocument(doc_id + 1)));

        return String.join(", ", keywords);
    }
    
    static class WordCloudListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                ParseText.createWordCloud(dv.getSelectedKeywords(), dv.getSelectedName());
            } catch (IOException ex) {
            }
        }
    }
    
    static class OpenDocListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            File doc = new File(db.getDocument(dv.getSelectedRow() + 1));
            Desktop dt = Desktop.getDesktop();
            try {
                dt.open(doc);
            } catch (IOException ex) {
            }
        }
    }
}
