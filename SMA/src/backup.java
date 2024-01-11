//import java.io.*;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.opencsv.exceptions.CsvException;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import com.opencsv.CSVReader;
//
//public class CSVProcessing {
//
//    public static void main(String[] args) {
//        String csvFile = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\ServiceMonitoringApp\\SMA\\member_details.csv"; // Path file
//        String excelFile = "members1.xlsx"; // Excel file to be generated
//
//        try {
//            processCSV(csvFile, excelFile);
//            System.out.println("CSV data processed and exported to Excel successfully.");
//        } catch (IOException | com.opencsv.exceptions.CsvException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void processCSV(String csvFile, String excelFile) throws IOException, com.opencsv.exceptions.CsvException {
//        Workbook workbook = new XSSFWorkbook();
////        Workbook invalidWorkbook = new XSSFWorkbook();
//
//        try (CSVReader reader = new CSVReader(new FileReader(csvFile))){
//            List<String[]> csvData = reader.readAll();
//            ;
//            for (String[] row : csvData) {
//                String gender = row[4]; //  gender is in the 5th column (index 4)
//
//                // Validating ID number, mobile number, and email address
//                if (isValidId(row[0]) && isValidMobileNumber(row[2]) && isValidEmail(row[3])) {
////                    Sheet validdata=workbook.getSheet("Valid Data");
////                    if (validdata == null){
////                        validdata=workbook.createSheet("Valid Data");
////                    }
////                    writeRow(validdata,row);
//                    if (gender != null && !gender.trim().isEmpty()) {
//                        String sanitizedGender =  gender.trim().replaceAll("[^a-zA-Z0-9-_]", "");;
//                        Sheet genderSheet = workbook.getSheet(sanitizedGender);
//                        if (genderSheet == null) {
//                            genderSheet = workbook.createSheet(sanitizedGender);
//                        }
//                        writeRow(genderSheet, row);
//                    }
//                } else   {
//                    Sheet invalidSheet = workbook.getSheet("Invalid Data");
//                    if (invalidSheet == null) {
//                        invalidSheet = workbook.createSheet("Invalid Data");
//                    }
//                    writeRow(invalidSheet, row);
//                }            }
//
//            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
//                workbook.write(outputStream);
//            }
//
////            try (FileOutputStream invalidOutputStream = new FileOutputStream("invalid" + excelFile)) {
////                invalidWorkbook.write(invalidOutputStream);
////            }
//
//        } finally {
//            workbook.close();
////            invalidWorkbook.close();
//        }
//    }
//
//    public static boolean isValidId(String id) {
//        // validation  for mobile numbers
//        return id != null && id.matches("\\d{8}");
//
//    }
//
//    public static boolean isValidMobileNumber(String number) {
//        // validation  for mobile numbers
//        return number!= null && number.matches("\\d{9}");
//    }
//
//    public static boolean isValidEmail(String email) {
//        //  validation  for email addresses
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//
//        Pattern pattern = Pattern.compile(emailRegex);
//        Matcher matcher = pattern.matcher(email);
//
//        return matcher.matches();
//
//    }
//
//    public static void writeRow(Sheet sheet, String[] rowData) {
//        int rowNum = sheet.getLastRowNum();
//        Row row = sheet.createRow(rowNum == -1 ? 0 : rowNum + 1);
//        for (int i = 0; i < rowData.length; i++) {
//            row.createCell(i).setCellValue(rowData[i]);
//        }
//    }
//}
//
//
