import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class csvtoexcel {

    public static void main(String[] args) {
        String csvFile = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\ServiceMonitoringApp\\SMA\\member_details.csv"; // Replace with your CSV file path
        String excelFile = "test1.xlsx"; // Excel file to be generated

        try {
            List<String[]> csvData = readCSV(csvFile);
            writeExcel(csvData, excelFile);
            System.out.println("CSV to Excel conversion completed successfully.");
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSV(String csvFile) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(csvFile));
        List<String[]> csvData = reader.readAll();
        reader.close();
        return csvData;
    }

    public static void writeExcel(List<String[]> csvData, String excelFile) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");


        int rowNum = 0;
        for (String[] rowData : csvData) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(colNum++);
                cell.setCellValue(cellData);
            }
        }
        // Write the workbook content to an OutputStream
        try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
            workbook.write(outputStream);
            System.out.println("Excel file created successfully.");
        }
//        try (FileWriter fileWriter = new FileWriter(excelFile)) {
//            workbook.write(fileWriter);
//        }

        workbook.close();
    }
}



