package documentanalyzer;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class DocumentAnalyzer {

    public static void main(String[] args) throws IOException {
        ParseText.populateList();
        DocumentDatabase db = new DocumentDatabase();
        DocumentView view = new DocumentView();
        
        view.addWordCloudListener(new DocumentController.WordCloudListener());
        view.addOpenDocListener(new DocumentController.OpenDocListener());
        
        //db.addDocument("C:/Users/TJ/Desktop/test.doc");
        //db.addDocument("C:/Users/TJ/Desktop/test.txt");
        //String path = "C:/Users/TJ/Desktop/test.doc";
        
        //ParseText.populateList();
        //LinkedHashMap<String, Integer> keywords = ParseText.findKeywords(path);
        //System.out.println(keywords);
        //String[] sortedKeywords = ParseText.sortByFrequency(keywords);
        //System.out.println(Arrays.toString(sortedKeywords));
        //ParseText.createWordCloud(sortedKeywords, path);
    }
}
