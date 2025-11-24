package com.example.tabassum_2207034_cvbuilder;

import javafx.concurrent.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class CVService {
    private final DatabaseManager db = DatabaseManager.getInstance();

    public Task<Integer> createSaveTask(CVModel model) {
        return new Task<>() {
            @Override
            protected Integer call() {
                return db.insertCV(
                    model.fullName,
                    model.email,
                    model.phone,
                    model.address,
                    model.educationsToJson(),
                    model.experiencesToJson(),
                    model.projectsToJson(),
                    model.skillsToJson()
                );
            }
        };
    }

    public Task<Boolean> createUpdateTask(CVModel model) {
        return new Task<>() {
            @Override
            protected Boolean call() {
                return db.updateCV(
                    model.id,
                    model.fullName,
                    model.email,
                    model.phone,
                    model.address,
                    model.educationsToJson(),
                    model.experiencesToJson(),
                    model.projectsToJson(),
                    model.skillsToJson()
                );
            }
        };
    }

    public Task<Boolean> createDeleteTask(int cvId) {
        return new Task<>() {
            @Override
            protected Boolean call() {
                return db.deleteCV(cvId);
            }
        };
    }

    public Task<ObservableList<CVModel>> createLoadAllTask() {
        return new Task<>() {
            @Override
            protected ObservableList<CVModel> call() {
                List<DatabaseManager.CVData> dataList = db.getAllCVs();
                List<CVModel> models = dataList.stream()
                        .map(CVModel::fromCVData)
                        .collect(Collectors.toList());
                return FXCollections.observableArrayList(models);
            }
        };
    }
}
