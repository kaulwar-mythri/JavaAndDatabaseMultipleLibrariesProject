package utils;

import models.Interview;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLQueries extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/exceldata";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Vikas@1964";

    public JFreeChart MaxInterviewsQuery() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT teamName, COUNT(*) as interviewCount FROM interviews WHERE month IN ('Oct-23', 'Nov-23') GROUP BY teamName ORDER BY COUNT(*) DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(query); ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    String category = set.getString("teamName");
                    int value = set.getInt("candidateName");
                    dataset.addValue(value, "Records", category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Team with maximum Interviews in Oct'23 and Nov'23",
                "Team",
                "Interviews Count",
                dataset
        );

        return chart;
    }

    public JFreeChart MinInterviewsQuery() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT teamName, COUNT(*) as interviewCount FROM interviews WHERE month IN ('Oct-23', 'Nov-23') GROUP BY teamName ORDER BY COUNT(*) LIMIT 1";
            try(PreparedStatement statement = connection.prepareStatement(query); ResultSet set = statement.executeQuery()) {
                while(set.next()) {
                    String category = set.getString("teamName");
                    int value = set.getInt("candidateName");
                    dataset.addValue(value, "Records", category);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Team with maximum Interviews in Oct'23 and Nov'23",
                "Team",
                "Interviews Count",
                dataset
        );

        return chart;
    }

    public JFreeChart getTop3Panels(List<Interview> interviewList) {
        Map<String, Long> panelsTointerviewcounts = interviewList.stream().filter(rec -> rec.getMonth().compareTo("Oct-23") >= 0 && rec.getMonth().compareTo("Nov-23") <= 0).collect(Collectors.groupingBy(record -> record.getPanelName(), Collectors.counting()));

        List<String> top3Panels= panelsTointerviewcounts.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        JFreeChart chart = top3PanelsChart(top3Panels);

        return chart;
    }

    private JFreeChart top3PanelsChart(List<String> top3Panels) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(String panel: top3Panels) {
            dataset.addValue(1, "Panels", panel);
        }

        return ChartFactory.createBarChart("Top 3 panels in October and November 2023", "Panel", "Interview Count", dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    public JFreeChart getTop3kills() {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String viewCreationQuery = "CREATE VIEW skills_view AS (SELECT skill, month FROM interviews)";
            try(PreparedStatement statement = connection.prepareStatement(viewCreationQuery)) {
                statement.executeUpdate();
            }

            List<String> top3Skills = getTop3killsList(connection);

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for(String skill: top3Skills) {
                dataset.addValue(1, "Skills", skill);
            }

            return ChartFactory.createBarChart("Top 3 skills in the months October and November", "Skill", "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getTop3killsList(Connection connection) throws SQLException {
        String selectQuery = "SELECT skill, COUNT(*) AS skillCount FROM skills_view WHERE month IN ('Oct-23', 'Nov-23') GROUP BY skill ORDER BY skillCount DESC LIMIT 3";

        List<String> top3 = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(selectQuery); ResultSet set = statement.executeQuery()) {
            while (set.next()) {
                top3.add(set.getString("skill"));
            }
        }
        return top3;
    }

    public JFreeChart getTop3killsForPeakTime() {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String selectQuery = "SELECT skill, COUNT(*) AS skillCount FROM interviews WHERE month IN ('Oct-23', 'Nov-23') AND TIME(time) BETWEEN '17:00:00' AND '18:00:00' GROUP BY skill ORDER BY skillCount DESC LIMIT 3";
            List<String> top3 = new ArrayList<>();

            try (PreparedStatement statement = connection.prepareStatement(selectQuery); ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    top3.add(set.getString("skill"));
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for(String skill: top3) {
                dataset.addValue(1, "Skills", skill);
            }

            return ChartFactory.createBarChart("Top 3 skills in the months October and November", "Skill", "Count", dataset, PlotOrientation.VERTICAL, true, true, false);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}