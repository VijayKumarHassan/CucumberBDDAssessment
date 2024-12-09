package Steps;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.*;

public class CSVFiles {

    // Read CSV files
    public Map<String, String[]> readFile(String fileName) throws IOException {
        Map<String, String[]> dataMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            try {
                String[] header = reader.readNext();
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
            String[] row;
            while (true) {
                try {
                    if (!((row = reader.readNext()) != null)) break;
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                String id = row[1];
                dataMap.put(id, row);
            }
        }
        return dataMap;
    }

    // Calculate Price
    public List<String[]> mergeFiles(Map<String, String[]> file1Data, Map<String, String[]> file2Data) {
        List<String[]> mergedData = new ArrayList<>();
        for (String id : file1Data.keySet()) {
            String[] file1Row = file1Data.get(id);
            String[] file2Row = file2Data.get(id);

            if (file2Row != null) {
                String[] mergedRow = new String[5]; // ID, PositionID, ISIN, Price
                mergedRow[0] = id; // ID
                mergedRow[1] = file2Row[1]; // PositionID
                mergedRow[2] = file1Row[3]; // ISIN from File1
                double unitPrice = Double.parseDouble(file1Row[4]); // UnitPrice from File1
                int quantity = Integer.parseInt(file2Row[2]); // Quantity from File2
                mergedRow[3] = file2Row[2];
                mergedRow[4] = String.valueOf(quantity * unitPrice); // Calculate Price

                mergedData.add(mergedRow);
            }
        }
        return mergedData;
    }

    // Write Data to output CSV file
    public void writeOutputCSV(String outputFileName, List<String[]> data) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFileName))) {
            // Write header
            writer.writeNext(new String[]{"ID", "PositionID", "ISIN", "Quantity", "TotalPrice"});
            // Write data
            writer.writeAll(data);
        }
    }
}

