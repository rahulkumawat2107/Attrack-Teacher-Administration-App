package com.example.attrack_database.Pojos;

public class GradePojo {
    private String id,subject,stu_grade_id,score_ut1,score_ut2,score_mid1,score_mid2;

    public String getScore_ut1() {
        return score_ut1;
    }

    public void setScore_ut1(String score_ut1) {
        this.score_ut1 = score_ut1;
    }

    public String getScore_ut2() {
        return score_ut2;
    }

    public void setScore_ut2(String score_ut2) {
        this.score_ut2 = score_ut2;
    }

    public String getScore_mid1() {
        return score_mid1;
    }

    public void setScore_mid1(String score_mid1) {
        this.score_mid1 = score_mid1;
    }

    public String getScore_mid2() {
        return score_mid2;
    }

    public void setScore_mid2(String score_mid2) {
        this.score_mid2 = score_mid2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStu_grade_id() {
        return stu_grade_id;
    }

    public void setStu_grade_id(String stu_grade_id) {
        this.stu_grade_id = stu_grade_id;
    }
}
