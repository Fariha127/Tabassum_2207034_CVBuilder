package com.example.tabassum_2207034_cvbuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cvbuilder.db";
    private static DatabaseManager instance;

    private DatabaseManager() {
        initDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void initDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS cv_records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT,
                address TEXT,
                educations_json TEXT,
                experiences_json TEXT,
                projects_json TEXT,
                skills_json TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertCV(String fullName, String email, String phone, String address,
                        String educationsJson, String experiencesJson,
                        String projectsJson, String skillsJson) {
        String sql = """
            INSERT INTO cv_records (full_name, email, phone, address,
                                   educations_json, experiences_json, projects_json, skills_json)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, educationsJson);
            pstmt.setString(6, experiencesJson);
            pstmt.setString(7, projectsJson);
            pstmt.setString(8, skillsJson);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateCV(int id, String fullName, String email, String phone, String address,
                           String educationsJson, String experiencesJson,
                           String projectsJson, String skillsJson) {
        String sql = """
            UPDATE cv_records
            SET full_name = ?, email = ?, phone = ?, address = ?,
                educations_json = ?, experiences_json = ?, projects_json = ?, skills_json = ?
            WHERE id = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, educationsJson);
            pstmt.setString(6, experiencesJson);
            pstmt.setString(7, projectsJson);
            pstmt.setString(8, skillsJson);
            pstmt.setInt(9, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCV(int id) {
        String sql = "DELETE FROM cv_records WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CVData> getAllCVs() {
        List<CVData> cvList = new ArrayList<>();
        String sql = "SELECT * FROM cv_records ORDER BY created_at DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cvList.add(extractCVData(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cvList;
    }

    public CVData getCVById(int id) {
        String sql = "SELECT * FROM cv_records WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCVData(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CVData extractCVData(ResultSet rs) throws SQLException {
        CVData data = new CVData();
        data.id = rs.getInt("id");
        data.fullName = rs.getString("full_name");
        data.email = rs.getString("email");
        data.phone = rs.getString("phone");
        data.address = rs.getString("address");
        data.educationsJson = rs.getString("educations_json");
        data.experiencesJson = rs.getString("experiences_json");
        data.projectsJson = rs.getString("projects_json");
        data.skillsJson = rs.getString("skills_json");
        data.createdAt = rs.getTimestamp("created_at");
        return data;
    }

    public static class CVData {
        public int id;
        public String fullName;
        public String email;
        public String phone;
        public String address;
        public String educationsJson;
        public String experiencesJson;
        public String projectsJson;
        public String skillsJson;
        public Timestamp createdAt;
    }
}
