import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

/**
 * Class: CharacterFrequencyAnalyzer
 * @author John Haley
 * @requires JFreeChart
 *
 * This class was created to examine a file of substituted cipher.txt and analyze the frequency of the character.
 * The class will then create a bar chart, using JFreeChart, to list the frequencies of each character.
 */
public class CharacterFrequencyAnalyzer {

    private HashMap<Character, Double> map = new HashMap<>();
    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private String cipher;

    public CharacterFrequencyAnalyzer(String fileName){
        initCFA(fileName);
    }

    private void initCFA(String fileName){
        this.map = new HashMap<>();
        this.dataset = new DefaultCategoryDataset();
        int charTtl = 0;

        try {
            Path filePath = Path.of(fileName);
            this.cipher = Files.readString(filePath);
            for(char ch : cipher.toCharArray()) {
                map = addValue(ch, map);
                charTtl++;
            }
            final int total = charTtl;
            map.forEach((key, value) -> {
                map.replace(key, ((value / total) * 100));
            });

        }catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        dataset = mapToDataset(map);
        initChart(dataset);
    }

    /**
     * addValue: adds a value to the HashMap
     * @param key - the key for the HashMap
     * @param map - the actual HashMap
     * @return the hashmap with the newly added value
     */
    public HashMap<Character, Double> addValue(Character key, HashMap<Character, Double> map) {
        map.put(key, map.getOrDefault(key, 0.0) + 1);
        return map;
    }

    /**
     * mapToDataset: used to convert a HashMap into a DefaultCategoryDataset
     * @param tmpMap - the map being converted to the Dataset
     * @return the dataset used to create a bar graph.
     */
    public DefaultCategoryDataset mapToDataset(HashMap<Character, Double> tmpMap){
        DefaultCategoryDataset tmpDataset = new DefaultCategoryDataset();
        tmpMap.forEach((key, value) -> tmpDataset.addValue(value, "Frequencies", key.toString()));
        return tmpDataset;
    }

    /**
     * initChart: used to create the environment for the chart.
     * @param tmpDataset
     */
    private void initChart(DefaultCategoryDataset tmpDataset) {
        JFreeChart chart = createChart(tmpDataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);

        int width = 1000;    /* Width of the image */
        int height = 500;   /* Height of the image */
        File BarChart = new File( "CipherText.jpeg" );
        try {
            ChartUtils.saveChartAsJPEG( BarChart , chart , width , height );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * createChart: creates the actual chart.
     * @param dataset
     * @return
     */
    private JFreeChart createChart(CategoryDataset dataset) {

        return ChartFactory.createBarChart(
                "Character Frequency Analysis",
                "",
                "Letter Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    /**
     * getCipher: gives the ciphertext
     * @return ciphertext String
     */
    public String getCipher(){
        return this.cipher;
    }

    /**
     * getKeySet
     * @return a set of characters used as keys for the map
     */
    public Set<Character> getKeySet(){
        return this.map.keySet();
    }
}
