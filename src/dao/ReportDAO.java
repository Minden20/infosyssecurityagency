package dao;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Report;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

public class ReportDAO {
    private static final String FILE_PATH = "data/reports.json";
    private final JsonFileHandler fileHandler;

    public ReportDAO() {
        this.fileHandler = new JsonFileHandler();
    }

    public boolean create(Report report) throws IOException {
        List<Report> reports = findAll();
        reports.add(report);
        return saveAll(reports);
    }

    public List<Report> findAll() throws IOException {
        List<Report> reports = new ArrayList<>();

        if (!fileHandler.fileExists(FILE_PATH)) {
            return reports;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            if (content.trim().isEmpty()) {
                return reports;
            }
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);

            for (JsonObject jsonObj : jsonArray) {
                UUID id = UUID.fromString((String) jsonObj.get("id"));
                UUID authorId = UUID.fromString((String) jsonObj.get("authorId"));
                String contentText = (String) jsonObj.get("content");
                String createdAtStr = (String) jsonObj.get("createdAt");
                LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);

                reports.add(new Report(id, authorId, contentText, createdAt));
            }
        } catch (IOException e) {
            System.err.println("Warning: Error parsing Report JSON: " + e.getMessage());
        }

        return reports;
    }

    private boolean saveAll(List<Report> reports) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();

        for (Report report : reports) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.put("id", report.getId().toString());
            jsonObj.put("authorId", report.getAuthorId().toString());
            jsonObj.put("content", report.getContent());
            jsonObj.put("createdAt", report.getCreatedAt().toString());
            jsonArray.add(jsonObj);
        }

        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
        return true;
    }
}
