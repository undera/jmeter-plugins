package com.adaptive.jmeter.plugins;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Reads CSV/TXT/Excel files with stepcount, time and TPS data
 */
public class CSVThroughputReader {

    /**
     * Read file and return list of throughput entries
     * Supports: .csv, .txt, .xlsx formats
     * CSV/TXT format: stepcount,mm:ss,tps
     * Excel format: Columns A=stepcount, B=time(mm:ss), C=tps
     */
    public static List<CSVThroughputEntry> readFile(String filePath) throws IOException {
        String extension = getFileExtension(filePath).toLowerCase();
        
        switch (extension) {
            case "xlsx":
            case "xls":
                return readExcelFile(filePath);
            case "csv":
            case "txt":
            default:
                return readTextFile(filePath);
        }
    }

    /**
     * Read text-based file (CSV or TXT)
     */
    private static List<CSVThroughputEntry> readTextFile(String filePath) throws IOException {
        List<CSVThroughputEntry> entries = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern timePattern = Pattern.compile("(\\d+):(\\d+)");
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) {
                    System.err.println("Invalid format at line " + lineNumber + ": " + line + 
                        " (expected: stepcount,mm:ss,tps)");
                    continue;
                }

                try {
                    int stepCount = Integer.parseInt(parts[0].trim());
                    String timeStr = parts[1].trim();
                    String tpsStr = parts[2].trim();

                    Matcher matcher = timePattern.matcher(timeStr);
                    if (!matcher.find()) {
                        System.err.println("Invalid time format at line " + lineNumber + ": " + timeStr + 
                            " (expected mm:ss)");
                        continue;
                    }

                    int minutes = Integer.parseInt(matcher.group(1));
                    int seconds = Integer.parseInt(matcher.group(2));
                    int tps = Integer.parseInt(tpsStr);

                    if (seconds >= 60) {
                        System.err.println("Invalid seconds value at line " + lineNumber + ": " + seconds);
                        continue;
                    }

                    entries.add(new CSVThroughputEntry(stepCount, minutes, seconds, tps));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line + " - " + e.getMessage());
                }
            }
        }

        return entries;
    }

    /**
     * Read Excel file (.xlsx or .xls)
     * Expected columns: A=stepcount, B=time(mm:ss), C=tps
     */
    private static List<CSVThroughputEntry> readExcelFile(String filePath) throws IOException {
        List<CSVThroughputEntry> entries = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Pattern timePattern = Pattern.compile("(\\d+):(\\d+)");
            
            for (Row row : sheet) {
                // Skip header row and empty rows
                if (row.getRowNum() == 0 || row.getPhysicalNumberOfCells() == 0) {
                    continue;
                }

                try {
                    Cell stepCountCell = row.getCell(0);
                    Cell timeCell = row.getCell(1);
                    Cell tpsCell = row.getCell(2);

                    if (stepCountCell == null || timeCell == null || tpsCell == null) {
                        continue;
                    }

                    int stepCount = (int) stepCountCell.getNumericCellValue();
                    String timeStr = timeCell.getStringCellValue().trim();
                    int tps = (int) tpsCell.getNumericCellValue();

                    Matcher matcher = timePattern.matcher(timeStr);
                    if (!matcher.find()) {
                        System.err.println("Invalid time format in row " + (row.getRowNum() + 1) + 
                            ": " + timeStr + " (expected mm:ss)");
                        continue;
                    }

                    int minutes = Integer.parseInt(matcher.group(1));
                    int seconds = Integer.parseInt(matcher.group(2));

                    if (seconds >= 60) {
                        System.err.println("Invalid seconds value in row " + (row.getRowNum() + 1) + 
                            ": " + seconds);
                        continue;
                    }

                    entries.add(new CSVThroughputEntry(stepCount, minutes, seconds, tps));
                } catch (Exception e) {
                    System.err.println("Error parsing row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }
        }

        return entries;
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0) {
            return filePath.substring(lastDot + 1);
        }
        return "";
    }

    /**
     * Get target TPS for a given time
     */
    public static int getTargetTpsForTime(List<CSVThroughputEntry> entries, long elapsedTimeMs) {
        int targetTps = 0;
        
        for (CSVThroughputEntry entry : entries) {
            if (elapsedTimeMs >= entry.getTotalTimeMs()) {
                targetTps = entry.getTargetTps();
            } else {
                break;
            }
        }
        
        return targetTps;
    }
}
