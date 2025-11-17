package com.example.tabassum_2207034_cvbuilder;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;


public class CVModel {
    public String fullName;
    public String email;
    public String phone;
    public String address;
    public List<Education> educations = new ArrayList<>();
    public List<Experience> experiences = new ArrayList<>();
    public List<String> projects = new ArrayList<>();
    public List<String> skills = new ArrayList<>();
    public Image profileImage; // optional, nao implement korte pari

    public static class Education {
        public String degree;
        public String institution;
        public String year;
        public String details;
    }

    public static class Experience {
        public String title;
        public String company;
        public String duration;
        public String details;
    }
}