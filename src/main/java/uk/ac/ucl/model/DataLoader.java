package uk.ac.ucl.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    private static final String COMMA_DELIMITER = ",";
    private DataFrame dataFrame;
    private String errorMessage;
    private String filename;
    /**
     * Creates a DataLoader and loads data from the specified CSV file into a DataFrame.
     * @param filename the path to the CSV file
     */
    public DataLoader(String filename) {
        this.filename = filename;
        this.dataFrame = new DataFrame();
        this.errorMessage = null;

        try {
            loadCSV();
        } catch (IOException e) {
            this.errorMessage = "Error reading file: " + e.getMessage();
            System.err.println(this.errorMessage);
        }
    }

    /**
     * Reads a CSV file and loads its data into the DataFrame.
     * The first row is expected to contain column names.
     * @throws IOException if an I/O error occurs
     */
    private void loadCSV() throws IOException {
        File file = new File(this.filename);

        if (!file.exists()) {
            throw new IOException("File not found: " + this.filename);
        }

        if (!file.canRead()) {
            throw new IOException("Cannot read file: " + this.filename);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the first line to get column names
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("File is empty: " + this.filename);
            }

            String[] columnNames = parseLine(headerLine);

            // Create columns in the DataFrame
            for (String columnName : columnNames) {
                dataFrame.addColumn(new Column(columnName));
            }

            // Read data rows
            String line;
            while ((line = reader.readLine()) != null) {
                processDataRow(line, columnNames);
            }
        }
    }

    /**
     * @param line the CSV line to parse
     * @return array of field values
     */
    private String[] parseLine(String line) {
        // Split by comma, keeping empty strings for consecutive commas
        return line.split(COMMA_DELIMITER, -1);
    }

    /**
     * Processes a data row and adds values to the appropriate columns in the DataFrame.
     * @param line the CSV line containing data
     * @param columnNames the names of the columns
     */
    private void processDataRow(String line, String[] columnNames) {
        String[] values = parseLine(line);
        for (int i = 0; i < columnNames.length; i++) {
            String value = (i < values.length) ? values[i] : "";
            dataFrame.addValue(columnNames[i], value);
        }
    }

    public DataFrame getData() {
        return this.dataFrame;
    }

    public boolean hasError() {
        return this.errorMessage != null;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getFilename() {
        return this.filename;
    }
}

