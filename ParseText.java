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
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public final class ParseText {

    private ParseText() {
    }

    private static final List<String> insignificant = new ArrayList<>();

    public static void populateList() {
        insignificant.add("the");
        insignificant.add("a");
        insignificant.add("an");
        insignificant.add("and");
        insignificant.add("with");
        insignificant.add("or");
        insignificant.add("of");
        insignificant.add("for");
        insignificant.add("this");
        insignificant.add("in");
        insignificant.add("is");
        insignificant.add("that");
        insignificant.add("it");
        insignificant.add("as");
        insignificant.add("to");
        insignificant.add("such");
        insignificant.add("than");
        insignificant.add("then");
    }

    public static LinkedHashMap<String, Integer> findKeywords(String path) throws IOException {

        LinkedHashMap<String, Integer> keywords = new LinkedHashMap<>();

        if (path.endsWith("txt")) {
            FileReader doc = new FileReader(path);
            BufferedReader text = new BufferedReader(doc);
            StringBuffer buffer = new StringBuffer("");
            String line = null;

            if (text.ready()) {
                while ((line = text.readLine()) != null) {
                    buffer.append(line);
                    String[] words = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().split(" "); //replace all non-alphabet characters, makes all words lower case, splits words by space
                    for (String word : words) {
                        if (!word.isEmpty() && !insignificant.contains(word)) {
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
            File file;
            WordExtractor extractor;
            try {
                file = new File(path);
                FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                HWPFDocument doc = new HWPFDocument(fis);
                extractor = new WordExtractor(doc);
                String fileText = extractor.getText();
                String[] words = fileText.replaceAll("[^a-zA-Z]", " ").toLowerCase().split(" ");
                for (String word : words) {
                    if (!word.isEmpty() && !insignificant.contains(word)) {
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

        //System.out.println(sortedMap);
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
        
        String savepath = "C:/Users/TJ/Desktop/" + filename.substring(0, filename.length() - 4) + "_wordcloud.png";
        
        wordCloud.writeToFile(savepath);
        
        File image = new File(savepath);
        Desktop dt = Desktop.getDesktop();
        dt.open(image);
    }
}
