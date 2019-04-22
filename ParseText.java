package documentanalyzer;

import com.kennycason.kumo.*;
import com.kennycason.kumo.bg.*;
import com.kennycason.kumo.font.scale.*;
import com.kennycason.kumo.nlp.*;
import com.kennycason.kumo.palette.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public final class ParseText {

    private static Set<String> stopWords;
    private static String directory = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
    
    private ParseText() {
    }

    public static void createStopwords() throws FileNotFoundException, IOException {
        stopWords = new LinkedHashSet<>();
        BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
        for(String line;(line = br.readLine()) != null;)
           stopWords.add(line.trim());
        br.close();
    }
    
    public static LinkedHashMap<String, Integer> findKeywords(File file) throws IOException {

        LinkedHashMap<String, Integer> keywords = new LinkedHashMap<>();
        String path = file.getAbsolutePath();
        
        if (path.endsWith("txt")) {
            FileReader doc = new FileReader(file);
            BufferedReader text = new BufferedReader(doc);
            StringBuffer buffer = new StringBuffer("");
            String line = null;

            if (text.ready()) {
                while ((line = text.readLine()) != null) {
                    buffer.append(line);
                    String[] words = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().split(" "); //replace all non-alphabet characters, makes all words lower case, splits words by space
                    for (String word : words) {
                        if (!word.isEmpty() && !stopWords.contains(word)) {
                            Integer freq = keywords.get(word);
                            if (freq == null) {
                                keywords.put(word, 1);
                            } else {
                                keywords.put(word, freq + 1);
                            }
                        }
                    }
                }
                text.close();
            }
        } else if (path.endsWith("doc")) {
            WordExtractor extractor;
            try {
                FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                HWPFDocument doc = new HWPFDocument(fis);
                extractor = new WordExtractor(doc);
                String fileText = extractor.getText();
                String[] words = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().split(" ");
                for (String word : words) {
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        Integer freq = keywords.get(word);
                        if (freq == null) {
                            keywords.put(word, 1);
                        } else {
                            keywords.put(word, freq + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (path.endsWith("docx")) {
            XWPFWordExtractor extractor;
            try {
                FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                XWPFDocument doc = new XWPFDocument(fis);
                extractor = new XWPFWordExtractor(doc);
                String fileText = extractor.getText();
                String[] words = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().split(" ");
                for (String word : words) {
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        Integer freq = keywords.get(word);
                        if (freq == null) {
                            keywords.put(word, 1);
                        } else {
                            keywords.put(word, freq + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return keywords;
    }

    public static String[] sortByFrequency(LinkedHashMap<String, Integer> map) {

        String[] keywords = new String[10];

        LinkedHashMap<String, Integer> sortedMap = map.entrySet().stream()
                .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String[] temp = sortedMap.keySet().toArray(new String[10]);

        System.arraycopy(temp, 0, keywords, 0, 10);

        return keywords;
    }

    public static void createWordCloud(String[] keywords, String path) throws IOException {

        File doc = new File(path);
        String filename = doc.getName();
        
        final FrequencyAnalyzer fa = new FrequencyAnalyzer();
        final List<WordFrequency> wf = fa.load(Arrays.asList(keywords));
        final Dimension dim = new Dimension(400, 400);
        final WordCloud wordCloud = new WordCloud(dim, CollisionMode.RECTANGLE);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new RectangleBackground(dim));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wf);
        
        String savepath = directory + filename.substring(0, filename.length() - 4) + "_wordcloud.png";
        
        wordCloud.writeToFile(savepath);
        
        File image = new File(savepath);
        Desktop dt = Desktop.getDesktop();
        dt.open(image);
    }
    
    //non-functioning
    /* public static void highlight(File file, String[] keywords) throws FileNotFoundException, IOException {
        
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument doc = new XWPFDocument(fis);
        String[] colors = {
            "BLUE", "YELLOW", "RED", "GREEN", "CYAN", "MAGNETA", "DARK_GREEN", "DARK_BLUE", "DARK_YELLOW", "DARK_RED"
        };
        
        for (int i = 0; i < 10; i++)
        {
            for (XWPFParagraph p : doc.getParagraphs())
            {
                List<XWPFRun> runs = p.getRuns();
                for (XWPFRun r : runs)
                {
                    String text = r.getText(0);
                    if (text.contains(keywords[i]))
                    {
                        r.setTextHighlightColor(colors[i]);
                        System.out.println("Highlighted " + r.getText(0));
                    }
                }
            }
        }
        doc.write(new FileOutputStream(file));
    }
    */
}
