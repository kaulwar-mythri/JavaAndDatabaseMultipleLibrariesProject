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

public class SQLQueries {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mythri";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Vikas@1964";

    public JFreeChart MaxInterviewsQuery() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT teamName, COUNT(*) as interviewCount FROM interviews WHERE month IN ('Oct-23', 'Nov-23') GROUP BY teamName ORDER BY COUNT(*) DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(query); ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    String category = set.getString("teamName");
                    int value = set.getInt("interviewCount");
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
                    int value = set.getInt("interviewCount");
                    dataset.addValue(value, "Records", category);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Team with minimum Interviews in Oct'23 and Nov'23",
                "Team",
                "Interviews Count",
                dataset
        );
        return chart;
    }

    public JFreeChart getTop3Panels(List<Interview> interviewList) {
        Map<String, Integer> panelsTointerviewcounts = interviewList.stream().filter(rec -> rec.getMonth() != null && rec.getMonth().equals("Oct-23") || rec.getMonth() != null && rec.getMonth().equals("Nov-23")).collect(Collectors.groupingBy(record -> record.getPanelName(), Collectors.summingInt(r -> 1)));

        List<Map.Entry<String,Integer>> top3Panels= panelsTointerviewcounts.entrySet().stream().sorted(Map.Entry.<String,Integer>comparingByValue().reversed()).limit(3).collect(Collectors.toList());
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        System.out.println(dataset);

        top3Panels.forEach(entry -> dataset.addValue(entry.getValue(), "Interviews", entry.getKey()));

        return ChartFactory.createBarChart("Top 3 panels in October and November 2023", "Panel", "Interview Count", dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    public JFreeChart getTop3kills() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String viewCreationQuery = "CREATE VIEW my_view AS SELECT skill, month, count(*) as skillCount FROM interviews GROUP BY skill, month";
            String selectQuery = "SELECT skill, COUNT(*) AS skillCount FROM my_view WHERE month IN ('Oct-23', 'Nov-23') GROUP BY skill ORDER BY skillCount DESC LIMIT 3";
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(viewCreationQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (PreparedStatement statement = connection.prepareStatement(selectQuery); ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    String category = set.getString("skill");
                    int value = set.getInt("skillCount");
                    dataset.addValue(value, "Records", category);
                }
            }

            return ChartFactory.createBarChart("Top 3 skills in the months October and November", "Skill", "Skill Count", dataset, PlotOrientation.VERTICAL, true, true, false);

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JFreeChart getTop3killsForPeakTime() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "SELECT skill, COUNT(*) AS skillCount FROM interviews WHERE month IN ('Oct-23', 'Nov-23') AND TIME(time) BETWEEN '17:00:00' AND '18:00:00' GROUP BY skill ORDER BY skillCount DESC LIMIT 3";
            try(PreparedStatement statement = connection.prepareStatement(query); ResultSet set = statement.executeQuery()) {
                while(set.next()) {
                    String category = set.getString("skill");
                    int value = set.getInt("skillCount");
                    dataset.addValue(value, "Records", category);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Top 3 skills in Peak Time (5:00PM to 6:00PM)",
                "Skill",
                "Skill Count",
                dataset
        );
        return chart;
    }
}