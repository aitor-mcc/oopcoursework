package uk.ac.ucl.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * JSONWriter is responsible for writing DataFrame data to a file in JSON format.
 * The output JSON is well-formed and represents data as an array of objects,
 * where each object represents a row with column names as keys.
 */
public class JSONWriter {
    
    /**
     * Writes the DataFrame to a JSON file.
     * 
     * @param dataFrame The DataFrame to write
     * @param filename The path to the output file
     * @throws IOException If an I/O error occurs during writing
     */
    public void writeToFile(DataFrame dataFrame, String filename) throws IOException {
        String json = toJSON(dataFrame);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
        }
    }
    
    /**
     * Converts a DataFrame to a JSON string.
     * 
     * @param dataFrame The DataFrame to convert
     * @return A well-formed JSON string representation of the DataFrame
     */
    public String toJSON(DataFrame dataFrame) {
        if (dataFrame == null) {
            return "[]";
        }
        
        ArrayList<String> columnNames = dataFrame.getColumnNames();
        if (columnNames.isEmpty()) {
            return "[]";
        }
        
        int rowCount = dataFrame.getRowCount();
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < rowCount; i++) {
            json.append("  {\n");
            
            for (int j = 0; j < columnNames.size(); j++) {
                String columnName = columnNames.get(j);
                String value = dataFrame.getValue(columnName, i);
                
                json.append("    ");
                json.append(escapeJSON(columnName));
                json.append(": ");
                json.append(escapeJSON(value));
                
                if (j < columnNames.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  }");
            if (i < rowCount - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]");
        return json.toString();
    }
    
    /**
     * Escapes a string value for JSON format.
     * Handles special characters like quotes, backslashes, and control characters.
     * 
     * @param value The string to escape
     * @return The escaped string wrapped in quotes
     */
    private String escapeJSON(String value) {
        if (value == null) {
            return "null";
        }
        
        StringBuilder escaped = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (c < ' ') {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
                    break;
            }
        }
        escaped.append("\"");
        return escaped.toString();
    }
}
