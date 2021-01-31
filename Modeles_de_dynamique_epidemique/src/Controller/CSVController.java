package Controller;

import com.opencsv.CSVWriter;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class CSVController {

    CSVController(){}

    public static void donwloadData(String filename, XYSeriesCollection dataset) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename));
        List<String[]> rows = new LinkedList<String[]>();

        // Init Titles
        List firstline = dataset.getSeries(0).getItems();
        String[] cat = new String[dataset.getSeries(0).getItemCount()];
        for (int j = 0; j < dataset.getSeries(0).getItemCount(); j++) {
            cat[j] = firstline.get(j).toString().replace("[", "");
            List<String> items = new ArrayList<String>(Arrays.asList(cat[j].split(", ")));
            cat[j] = items.get(0);
        }
        rows.add(cat);

        // Insert values
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            List values = dataset.getSeries(i).getItems();
            String[] tmp = new String[dataset.getSeries(i).getItemCount()];

            for (int j = 0; j < dataset.getSeries(i).getItemCount(); j++) {
                tmp[j] = values.get(j).toString().replace("]", "");
                List<String> items = new ArrayList<String>(Arrays.asList(tmp[j].split(", ")));
                tmp[j] = items.get(1);
            }
            rows.add(tmp);
        }
        csvWriter.writeAll(rows);
        csvWriter.close();
    }
}