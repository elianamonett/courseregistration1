package com.example.courseregistration;

public class Student {
    private String name;
    private int id;
    private String course;
    private String priority;

    public Student(String name, int id, String course, String priority) {
        this.name = name;
        this.id = id;
        this.course = course;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}

