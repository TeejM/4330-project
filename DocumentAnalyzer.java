package documentanalyzer;

import java.io.IOException;

public class DocumentAnalyzer {

    public static void main(String[] args) throws IOException {
        ParseText.createStopwords();
        DocumentDatabase db = new DocumentDatabase();
        DocumentView view = new DocumentView();
        
        view.addWordCloudListener(new DocumentController.WordCloudListener());
        view.addOpenDocListener(new DocumentController.OpenDocListener());
    }
}
