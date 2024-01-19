package org.skyworld.skymonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import com.fasterxml.jackson.annotation.JsonProperty;

class ServiceConfig {
    @JsonProperty("serviceId")
    private int serviceId;

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("serviceHost")
    private String serviceHost;

    @JsonProperty("servicePort")
    private int servicePort;

    @JsonProperty("serviceResourceURI")
    private String serviceResourceURI;

    @JsonProperty("serviceMethod")
    private String serviceMethod;

    @JsonProperty("monitoringInterval")
    private int monitoringInterval;

    @JsonProperty("monitoringIntervalTimeUnit")
    private String monitoringIntervalTimeUnit;

    @JsonProperty("enableFileLogging")
    private boolean enableFileLogging;

    @JsonProperty("fileLoggingInterval")
    private String fileLoggingInterval;

    @JsonProperty("enableLogsArchiving")
    private boolean enableLogsArchiving;

    @JsonProperty("logArchivingInterval")
    private String logArchivingInterval;

    // Add getters and setters

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceResourceUri() {
        return serviceResourceURI;
    }

    public void setServiceResourceUri(String serviceResourceURI) {
        this.serviceResourceURI = serviceResourceURI;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public int getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(int monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    public String getMonitoringIntervalTimeUnit() {
        return monitoringIntervalTimeUnit;
    }

    public void setMonitoringIntervalTimeUnit(String monitoringIntervalTimeUnit) {
        this.monitoringIntervalTimeUnit = monitoringIntervalTimeUnit;
    }

    public boolean isEnableFileLogging() {
        return enableFileLogging;
    }

    public void setEnableFileLogging(boolean enableFileLogging) {
        this.enableFileLogging = enableFileLogging;
    }

    public String getFileLoggingInterval() {
        return fileLoggingInterval;
    }

    public void setFileLoggingInterval(String fileLoggingInterval) {
        this.fileLoggingInterval = fileLoggingInterval;
    }

    public boolean isEnableLogsArchiving() {
        return enableLogsArchiving;
    }

    public void setEnableLogsArchiving(boolean enableLogsArchiving) {
        this.enableLogsArchiving = enableLogsArchiving;
    }

    public String getLogArchivingInterval() {
        return logArchivingInterval;
    }

    public void setLogArchivingInterval(String logArchivingInterval) {
        this.logArchivingInterval = logArchivingInterval;
    }
}


public class SkyMonitorApplication {
    private static final String CONFIG_FILE_PATH = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\Tasks\\sky-monitor\\config.json"; // path

    private static final Logger applicationLogger = Logger.getLogger("ApplicationLogger");

    private static final Logger serverLogger = Logger.getLogger("ServerLogger");


    public static void main(String[] args) {
        // Configure loggers
        applicationLogger.setLevel(Level.INFO);
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        applicationLogger.addHandler(consoleHandler);

        serverLogger.setLevel(Level.INFO);
        serverLogger.addHandler(consoleHandler);

        try {
            List<ServiceConfig> services = loadServicesFromConfig(CONFIG_FILE_PATH);

            System.out.println();

            // Update the path
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


            while (running) {
                System.out.println("Enter command:");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.startsWith("sky-monitor application status")) {
                    getApplicationStatus(input, services);
                    String host = null;
int port = 0;
        
                    String resourceUri = null;
                    checkApplicationStatus(host,port,resourceUri);
                } else if (input.startsWith("sky-monitor server status")) {
                    getServerStatus(input, services);
                } else if (input.equals("sky-monitor service list")) {
                    checkServiceList(services);
                } else if (input.equals("sky-monitor start")) {
                    startMonitoring(services, executorService, applicationLogger, serverLogger);
                } else if (input.equals("sky-monitor stop")) {
                    running = false;
                } else {
                    System.out.println("Invalid command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    *****************************************************************COMMAND CHECKS*****************************************************************//
    private static int extractServiceId(String command) {
        // Extract Service ID from the command
        String[] parts = command.split("\\s+");
        if (parts.length >= 4 && "status".equalsIgnoreCase(parts[2])) {
            try {
                return Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }
        return -1;
    }

    //-----------------------------------------------SKY APPLICATION STATUS---------------------------------------------------------------------------------//
    private static void getApplicationStatus(String command, List<ServiceConfig> services) {
        // Extract Service ID from the command
        int serviceId = 1;
        if (serviceId != 0) {
            System.out.println(serviceId);
            ServiceConfig service = findServiceById(serviceId, services);
            if (service != null) {
                boolean applicationStatus = checkApplicationStatus(service.getServiceHost(), service.getServicePort(), service.getServiceResourceUri());
                System.out.println("Application status for Service ID " + serviceId + ": " + (applicationStatus ? "UP" : "DOWN"));
            } else {
                System.out.println("Service not found");
            }
        } else {
            System.out.println("Invalid command format.");
            System.out.println(serviceId);
        }
    }

    private static boolean checkApplicationStatus(String host, int port, String resourceUri) {
        String url = "http://" + host + ":" + port + resourceUri;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            // Check if the HTTP response code is in the success range (200-299)
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            // An exception occurred, indicating that the application is not reachable
            return false;
        }
    }

    //--------------------------------SKY APPLICATION SERVER STATUS-------------------------------------------------------//
    private static void getServerStatus(String command, List<ServiceConfig> services) {
        // Split the command to get the service ID directly
        String[] parts = command.split("\\s+");
        if (parts.length >= 4 && "status".equalsIgnoreCase(parts[2])) {
            try {
                int serviceId = Integer.parseInt(parts[3]);
                ServiceConfig service = findServiceById(serviceId, services);
                if (service != null) {
                    // Checks if the server is reachable on a specific port
                    boolean isServerUp = isServerReachable(service.getServiceHost(), service.getServicePort());
                    System.out.println("Server status for Service ID " + serviceId + ": " + (isServerUp ? "UP" : "DOWN"));
                } else {
                    System.out.println("Service not found");
                }
            } catch (NumberFormatException e) {
                // Handle parsing error
                System.out.println("Invalid service ID");
            }
        } else {
            System.out.println("Invalid command format.");
        }
    }


    // Helper method to check if the server is reachable on a specific port
    private static boolean isServerReachable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000); // Timeout: 2000 milliseconds
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //----------------------------------------SKY SERVICE LIST-------------------------------------------------------------//
    private static void checkServiceList(List<ServiceConfig> services) {
        // Print the table header
        System.out.format("%-15s%-25s%-30s%-35s%-40s%-45s%n",
                "Service ID",
                "Service Name",
                "Application Status",
                "Application Status Date",
                "Server Status",
                "Server Status Date");
        System.out.println("***************************************************************************************************************************************************************************************************");

        // Print each service in the table
        for (ServiceConfig service : services) {
            // Get application and server status for the service
            boolean applicationStatus = checkApplicationStatus(service.getServiceHost(), service.getServicePort(), service.getServiceResourceUri());
            boolean serverStatus = isServerReachable(service.getServiceHost(), service.getServicePort());

            // Print the service information
            System.out.format("%-15s%-25s%-30s%-35s%-40s%-45s%n",
                    service.getServiceId(),
                    service.getServiceName(),
                    applicationStatus ? "UP" : "DOWN",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    serverStatus ? "UP" : "DOWN",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("**************************************************************************************************************************************************************************************************************************");
        }
    }

    //---------------------------------------SKY START MONITORING----------------------------------------------------------//
    private static void startMonitoring(List<ServiceConfig> services, ScheduledExecutorService executorService, Logger applicationLogger, Logger serverLogger) {
        for (ServiceConfig service : services) {
            executorService.scheduleAtFixedRate(() -> monitorService(service, executorService, applicationLogger, serverLogger),
                    0, service.getMonitoringInterval(), TimeUnit.valueOf(service.getMonitoringIntervalTimeUnit().toUpperCase()));
        }
    }

    private static void monitorService(ServiceConfig service, ScheduledExecutorService executorService, Logger applicationLogger, Logger serverLogger) {
        executorService.scheduleAtFixedRate(() -> {
            boolean applicationStatus = false;
            try {
                applicationStatus = checkServiceStatus(service.getServiceHost(), service.getServicePort(), service.getServiceResourceUri());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boolean serverStatus = false;
            try {
                serverStatus = checkServiceStatus(service.getServiceHost(), service.getServicePort(), service.getServiceResourceUri());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Log the timestamp and status
            logServiceStatus(service, applicationStatus, serverStatus, applicationLogger, serverLogger);

            // Archive logs if required
            if (service.isEnableLogsArchiving()) {
                archiveLogsIfNeeded(service, applicationLogger, serverLogger);
            }

            // Log status to a file based on the configuration
            String logFilepath = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\Tasks\\sky-monitor\\LOGS";
            Path logFilePath = Path.of(logFilepath);
//        if (service.isEnableFileLogging() && shouldLogToFile(logFilePath, service.getFileLoggingInterval())) {
            if (service.isEnableFileLogging()) {
                logStatusToFile(service, applicationStatus, serverStatus);
            }
        }, 0, service.getMonitoringInterval(), TimeUnit.valueOf(service.getMonitoringIntervalTimeUnit().toUpperCase()));
    }

    private static void logStatusToFile(ServiceConfig service, boolean applicationStatus, boolean serverStatus) {
        // Check if the directory exists, create it if not
        String logDirectoryPath = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\Tasks\\sky-monitor\\LOGS";
        Path logDirectory = Path.of(logDirectoryPath);

        if (!Files.exists(logDirectory)) {
            try {
                Files.createDirectories(logDirectory);
                System.out.println("Log directory created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error creating log directory: " + e.getMessage());
                return;
            }
        }


        // Create a log file path based on service and log type
        String logFileName = getLogFileName(service.getServiceId(), "application");
        Path logFilePath = logDirectory.resolve(logFileName);

        // Format the status information
        String logEntry = String.format("Service ID: %d, Application Status: %s, Server Status: %s, Timestamp: %s%n",
                service.getServiceId(),
                applicationStatus ? "UP" : "DOWN",
                serverStatus ? "UP" : "DOWN",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try {
            // Append the log entry to the file
            Files.writeString(logFilePath, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Status logged to file: " + logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to log file: " + e.getMessage());
        }


    }

    private static boolean shouldLogToFile(Path logFilePath, String fileLoggingInterval) {
        try {
            // Check if the log file exists
            if (Files.exists(logFilePath)) {
                // Read the last line of the log file to get the last logged timestamp
//                System.out.printf(String.valueOf(new File(String.valueOf(logFilePath)).exists()));
                String lastLogEntry = Files.readString(logFilePath);
                LocalDateTime lastLogTimestamp = parseTimestampFromLogEntry(lastLogEntry);

                // Calculate the time difference between the last log timestamp and the current time
                LocalDateTime currentTimestamp = LocalDateTime.now();
                long timeDifferenceMinutes = Duration.between(lastLogTimestamp, currentTimestamp).toMinutes();

                // Parse the file logging interval
                long loggingIntervalMinutes = parseLoggingInterval(fileLoggingInterval);

                // Check if it's time to log again
                return timeDifferenceMinutes >= loggingIntervalMinutes;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading log file: " + e.getMessage());
        }

        // Log file doesn't exist or encountered an error, log anyway
        return true;
    }

    private static LocalDateTime parseTimestampFromLogEntry(String logEntry) {
        // Extract timestamp from the log entry (adjust based on your log entry format)
        String timestampString = logEntry.substring(logEntry.lastIndexOf("Timestamp: ") + 12);
        return LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static long parseLoggingInterval(String fileLoggingInterval) {
        try {
            // Parse the file logging interval to minutes (adjust based on your interval format)
            if (fileLoggingInterval.endsWith("m")) {
                return Long.parseLong(fileLoggingInterval.substring(0, fileLoggingInterval.length() - 1));
            } else {
                // Default to minutes if no unit is provided
                return Long.parseLong(fileLoggingInterval);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Handle invalid input gracefully
            throw new IllegalArgumentException("Invalid file logging interval: " + fileLoggingInterval, e);
        }
    }


    //--------------------------------------------------------------------------------------------------------------------//
    private static boolean checkServiceStatus(String host, int port, String resourceUri) throws IOException {
        URL url = new URL("http://" + host + ":" + port + resourceUri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    //log archiving

    private static void logServiceStatus(ServiceConfig service, boolean applicationStatus, boolean serverStatus, Logger applicationLogger, Logger serverLogger) {
//        // Log the application status
//        applicationLogger.info(String.format("Service ID: %d, Application Status: %s, Timestamp: %s",
//                service.getServiceId(), applicationStatus ? "UP" : "DOWN", LocalDateTime.now()));
//
//        // Log the server status
//        serverLogger.info(String.format("Service ID: %d, Server Status: %s, Timestamp: %s",
//                service.getServiceId(), serverStatus ? "UP" : "DOWN", LocalDateTime.now()));


        // Log the status for each service separately
        System.out.println("Service ID: " + service.getServiceId() + ", Service Name: " + service.getServiceName() + ", Application Status: " + applicationStatus + ", Timestamp: " + LocalDateTime.now());
        System.out.println("Service ID: " + service.getServiceId() + ", Service Name: " + service.getServiceName() + ", Server Status: " + serverStatus + ", Timestamp: " + LocalDateTime.now());
    }

    private static void archiveLogsIfNeeded(ServiceConfig service, Logger applicationLogger, Logger serverLogger) {
        File applicationLogFile = new File(getLogFileName(service.getServiceId(), "application"));
        File serverLogFile = new File(getLogFileName(service.getServiceId(), "server"));

        // Archive logs if the log archiving interval is reached
        if (shouldArchiveLogs(applicationLogFile, service.getLogArchivingInterval())) {
            System.out.println("ukweli");
            archiveLogs(applicationLogFile);
        }

        if (shouldArchiveLogs(serverLogFile, service.getLogArchivingInterval())) {
            archiveLogs(serverLogFile);
        }
        if (shouldArchiveLogs(applicationLogFile, service.getLogArchivingInterval())) {
            createArchiveDirectory();  // Add this line to create the archive directory
            archiveLogs(applicationLogFile);
        }
        // Log the last modified timestamps for debugging
        System.out.println("Application Log Last Modified: " + applicationLogFile.lastModified());
        System.out.println("Server Log Last Modified: " + applicationLogFile.lastModified());
//        applicationLogger.info("Application Log Last Modified: " + applicationLogFile.lastModified());
//        serverLogger.info("Server Log Last Modified: " + serverLogFile.lastModified());

    }

    private static void createArchiveDirectory() {
        String archiveDirectoryPath = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\Tasks\\sky-monitor\\ARCHIVEDLOGS";
        File archiveDirectory = new File(archiveDirectoryPath);

        if (!archiveDirectory.exists()) {
            if (archiveDirectory.mkdirs()) {
                System.out.println("Archive directory created successfully.");
            } else {
                System.out.println("Failed to create archive directory.");
            }
        }
    }

    private static String getLogFileName(int serviceId, String logType) {
        return String.format("service_%d_%s.log", serviceId, logType);
    }


    private static boolean shouldArchiveLogs(File logFile, String logArchivingInterval) {
//        System.out.println("Test : " + (!logFile.exists() || logFile.isDirectory()));
//        System.out.println("Test 1 : " + (logFile.isDirectory()));
//        System.out.println("Test 2 : " + (!logFile.exists()));
//        System.out.println("Test 3 : " + (logFile.getAbsolutePath()));
//        if (!logFile.exists() || logFile.isDirectory()) {
        if (logFile.isDirectory()) {
            // Log file doesn't exist or is a directory, no need to archive
            return false;
        }

        // Get the last modified timestamp of the log file
        long lastModifiedTimestamp = logFile.lastModified();

//        if (lastModifiedTimestamp == 0) {
//            System.out.println("Invalid last modified timestamp for " + logFile.getName());
//            return false;
//        }

        // Get the current timestamp
        long currentTimestamp = System.currentTimeMillis();

        // Calculate the time difference in milliseconds
        long timeDifference = currentTimestamp - lastModifiedTimestamp;

        // Parse the log archiving interval
        long archivingIntervalMillis = parseArchivingInterval(logArchivingInterval);

        // Check if the time difference exceeds the archiving interval
        return timeDifference >= archivingIntervalMillis;
    }

    private static long parseArchivingInterval(String logArchivingInterval) {
        try {
            // Parse the log archiving interval to milliseconds
            if (logArchivingInterval.endsWith("s")) {
                return Long.parseLong(logArchivingInterval.substring(0, logArchivingInterval.length() - 1)) * 1000;
            } else if (logArchivingInterval.endsWith("m")) {
                return Long.parseLong(logArchivingInterval.substring(0, logArchivingInterval.length() - 1)) * 60 * 1000;
            } else if (logArchivingInterval.endsWith("h")) {
                return Long.parseLong(logArchivingInterval.substring(0, logArchivingInterval.length() - 1)) * 60 * 60 * 1000;
            } else {
                // Default to milliseconds if no unit is provided
                return Long.parseLong(logArchivingInterval);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            // Handle invalid input gracefully
            throw new IllegalArgumentException("Invalid log archiving interval: " + logArchivingInterval, e);
        }
    }

    private static void archiveLogs(File logFile) {
        // Check if the log file exists
        if (!logFile.exists()) {
            System.out.println("Log file does not exist. Cannot archive.");

//            ::TODO  CREATE FILE IF DOESNT EXIST


            return;
        }

        //  archive directory path
        String archiveDirectoryPath = "C:\\Users\\BKT\\Desktop\\PROJECTS\\JAVA\\Tasks\\sky-monitor\\ARCHIVEDLOGS";

        System.out.println("tuko hapa sasa");

        // Create the archive directory if it doesn't exist
        File archiveDirectory = new File(archiveDirectoryPath);
        if (!archiveDirectory.exists()) {
            if (archiveDirectory.mkdirs()) {
                System.out.println("Archive directory created successfully.");
            } else {
                System.out.println("Failed to create archive directory.");
                return;
            }
        }

        try {
            // Create a zip file with the log file
            String zipFileName = logFile.getName() + ".zip";
            String zipFilePath = archiveDirectoryPath + File.separator + zipFileName;
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath));
                 FileInputStream fileInputStream = new FileInputStream(logFile)) {

                ZipEntry zipEntry = new ZipEntry(logFile.getName());
                zipOutputStream.putNextEntry(zipEntry);

                // Transfer data from the log file to the zip file
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, bytesRead);
                }

                zipOutputStream.closeEntry();
            }

            // Move the zip file to the archive directory
            File archiveZipFile = new File(zipFilePath);
            File destinationZipFile = new File(archiveDirectory, zipFileName);
            archiveZipFile.renameTo(destinationZipFile);

            // Delete the original log file
            logFile.delete();

            System.out.println("Log archived successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error archiving logs: " + e.getMessage());
        }
    }

    //----------------------------------------SKY STOP MONITORING----------------------------------------------------------------------------//
//private static void stopMonitoring(ScheduledExecutorService executorService) {
//    // Stop the executor service gracefully
//}
//------------------------------------------------------------------------------------------------------------------------------------
    private static TimeUnit getTimeUnit(String unit) {
        switch (unit.toUpperCase()) {
            case "SECONDS":
                return TimeUnit.SECONDS;
            case "MINUTES":
                return TimeUnit.MINUTES;
            case "HOURS":
                return TimeUnit.HOURS;
            case "DAYS":
                return TimeUnit.DAYS;
            case "WEEKS":
                return TimeUnit.DAYS; // Adjust as needed
            case "MONTHS":
                return TimeUnit.DAYS; // Adjust as needed
            default:
                throw new IllegalArgumentException("Invalid time unit: " + unit);
        }
    }

    public static ServiceConfig findServiceById(int serviceId, List<ServiceConfig> services) {
        for (ServiceConfig service : services) {
            if (service.getServiceId() == serviceId) {
                return service;
            }
        }
        return null; // Service not found
    }


    private static List<ServiceConfig> loadServicesFromConfig(String configFile) throws IOException {
        File file = new File(configFile);
        String fileExtension = getFileExtension(file);

        List<ServiceConfig> services = null;
        ObjectMapper objectMapper = getMapper(fileExtension);

        try {
//            List test  = ( objectMapper.readValue(file, List.class));
            services = List.of(objectMapper.readValue(file, ServiceConfig[].class));
//            System.out.println("services : "+ test);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return services;
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    private static ObjectMapper getMapper(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "json":
                return new ObjectMapper();
            case "xml":
                return new XmlMapper();
            case "yaml":
                return new YAMLMapper();
            case "ini":
//                return new Configurations().ini(file);
            default:
                throw new IllegalArgumentException("Unsupported file extension: " + fileExtension);
        }
    }


}


