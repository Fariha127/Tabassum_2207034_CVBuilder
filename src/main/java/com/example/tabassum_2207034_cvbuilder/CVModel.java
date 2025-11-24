package com.example.tabassum_2207034_cvbuilder;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;


public class CVModel {
    public int id;
    public String fullName;
    public String email;
    public String phone;
    public String address;
    public List<Education> educations = new ArrayList<>();
    public List<Experience> experiences = new ArrayList<>();
    public List<String> projects = new ArrayList<>();
    public List<String> skills = new ArrayList<>();
    public Image profileImage;

    // Observable properties for UI binding
    public IntegerProperty idProperty() { 
        return new SimpleIntegerProperty(id); 
    }
    
    public StringProperty fullNameProperty() { 
        return new SimpleStringProperty(fullName); 
    }
    
    public StringProperty emailProperty() { 
        return new SimpleStringProperty(email); 
    }

    // Simple JSON conversion methods
    public String educationsToJson() {
        if (educations.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < educations.size(); i++) {
            Education e = educations.get(i);
            sb.append("{");
            sb.append("\"degree\":\"").append(escapeJson(e.degree)).append("\",");
            sb.append("\"institution\":\"").append(escapeJson(e.institution)).append("\",");
            sb.append("\"year\":\"").append(escapeJson(e.year)).append("\",");
            sb.append("\"details\":\"").append(escapeJson(e.details)).append("\"");
            sb.append("}");
            if (i < educations.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void educationsFromJson(String json) {
        educations.clear();
        if (json == null || json.isEmpty() || json.equals("[]")) return;
        String[] items = json.substring(1, json.length() - 1).split("},\\{");
        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            Education e = new Education();
            String[] fields = item.split("\",\"");
            for (String field : fields) {
                String[] kv = field.split("\":\"");
                if (kv.length == 2) {
                    String key = kv[0].replace("\"", "");
                    String value = unescapeJson(kv[1].replace("\"", ""));
                    switch (key) {
                        case "degree" -> e.degree = value;
                        case "institution" -> e.institution = value;
                        case "year" -> e.year = value;
                        case "details" -> e.details = value;
                    }
                }
            }
            educations.add(e);
        }
    }

    public String experiencesToJson() {
        if (experiences.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < experiences.size(); i++) {
            Experience e = experiences.get(i);
            sb.append("{");
            sb.append("\"title\":\"").append(escapeJson(e.title)).append("\",");
            sb.append("\"company\":\"").append(escapeJson(e.company)).append("\",");
            sb.append("\"duration\":\"").append(escapeJson(e.duration)).append("\",");
            sb.append("\"details\":\"").append(escapeJson(e.details)).append("\"");
            sb.append("}");
            if (i < experiences.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void experiencesFromJson(String json) {
        experiences.clear();
        if (json == null || json.isEmpty() || json.equals("[]")) return;
        String[] items = json.substring(1, json.length() - 1).split("},\\{");
        for (String item : items) {
            item = item.replace("{", "").replace("}", "");
            Experience e = new Experience();
            String[] fields = item.split("\",\"");
            for (String field : fields) {
                String[] kv = field.split("\":\"");
                if (kv.length == 2) {
                    String key = kv[0].replace("\"", "");
                    String value = unescapeJson(kv[1].replace("\"", ""));
                    switch (key) {
                        case "title" -> e.title = value;
                        case "company" -> e.company = value;
                        case "duration" -> e.duration = value;
                        case "details" -> e.details = value;
                    }
                }
            }
            experiences.add(e);
        }
    }

    public String projectsToJson() {
        if (projects.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < projects.size(); i++) {
            sb.append("\"").append(escapeJson(projects.get(i))).append("\"");
            if (i < projects.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void projectsFromJson(String json) {
        projects.clear();
        if (json == null || json.isEmpty() || json.equals("[]")) return;
        String content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) return;
        String[] items = content.split("\",\"");
        for (String item : items) {
            projects.add(unescapeJson(item.replace("\"", "")));
        }
    }

    public String skillsToJson() {
        if (skills.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < skills.size(); i++) {
            sb.append("\"").append(escapeJson(skills.get(i))).append("\"");
            if (i < skills.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public void skillsFromJson(String json) {
        skills.clear();
        if (json == null || json.isEmpty() || json.equals("[]")) return;
        String content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) return;
        String[] items = content.split("\",\"");
        for (String item : items) {
            skills.add(unescapeJson(item.replace("\"", "")));
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }

    private String unescapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\\"", "\"")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\\\", "\\");
    }

    // Create from database data
    public static CVModel fromCVData(DatabaseManager.CVData data) {
        CVModel model = new CVModel();
        model.id = data.id;
        model.fullName = data.fullName;
        model.email = data.email;
        model.phone = data.phone != null ? data.phone : "";
        model.address = data.address != null ? data.address : "";
        model.educationsFromJson(data.educationsJson);
        model.experiencesFromJson(data.experiencesJson);
        model.projectsFromJson(data.projectsJson);
        model.skillsFromJson(data.skillsJson);
        return model;
    }

    public static class Education {
        public String degree = "";
        public String institution = "";
        public String year = "";
        public String details = "";
    }

    public static class Experience {
        public String title = "";
        public String company = "";
        public String duration = "";
        public String details = "";
    }
}