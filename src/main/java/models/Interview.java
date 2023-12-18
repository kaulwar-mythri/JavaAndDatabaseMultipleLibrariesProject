package models;

import java.util.Date;

public class Interview {
    Date date;
    String month;
    String teamName;
    String panelName;
    String round;
    String skill;
    Date time;
    String currLocation;
    String prefLocation;
    String candidateName;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public String getPrefLocation() {
        return prefLocation;
    }

    public void setPrefLocation(String prefLocation) {
        this.prefLocation = prefLocation;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "date=" + date +
                ", month='" + month + '\'' +
                ", teamName='" + teamName + '\'' +
                ", panelName='" + panelName + '\'' +
                ", round='" + round + '\'' +
                ", skill='" + skill + '\'' +
                ", time=" + time +
                ", currLocation='" + currLocation + '\'' +
                ", prefLocation='" + prefLocation + '\'' +
                ", candidateName='" + candidateName + '\'' +
                '}';
    }
}
