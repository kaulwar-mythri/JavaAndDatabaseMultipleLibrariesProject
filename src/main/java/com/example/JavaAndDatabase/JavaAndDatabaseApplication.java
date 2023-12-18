package com.example.JavaAndDatabase;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import models.Interview;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utils.ExcelFileReader;
import utils.SQLQueries;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.List;

@SpringBootApplication
public class JavaAndDatabaseApplication {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mythri";
	private static final String JDBC_USER = "root";
	private static final String JDBC_PASSWORD = "Vikas@1964";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SpringApplication.run(JavaAndDatabaseApplication.class, args);
		ExcelFileReader fileReader = new ExcelFileReader();
		String filePath = "C:\\Users\\kaulwar.mythri\\IdeaProjects\\JavaAndDatabase\\src\\main\\resources\\AccoliteInterviewData.xlsx";
		List<Interview> interviewList = fileReader.readExcelFile(filePath);
		System.out.println(interviewList.size());

		interviewList.parallelStream().forEach(JavaAndDatabaseApplication::insertDataIntoSQLTable);

		generateCharts(interviewList);
	}

	private static void generateCharts(List<Interview> interviewList) {
		String pdfPath = "charts.pdf";
		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			document.open();


			SQLQueries queries = new SQLQueries();
			addChartToPDF(document, "Team with maximum Interviews in October and November", queries.MaxInterviewsQuery());
			addChartToPDF(document, "Team with minimum Interviews in October and November", queries.MinInterviewsQuery());
			addChartToPDF(document, "Top 3 panels in October and November", queries.getTop3Panels(interviewList));
			addChartToPDF(document, "Top 3 skills in October and November", queries.getTop3kills());
			addChartToPDF(document, "Team 3 skills in peak time in October and November", queries.getTop3killsForPeakTime());

//			ChartPanel chartPanel = new ChartPanel(chart);
//			chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
//			setContentPane(chartPanel);
//
//			setSize(700, 500);
//			setLocationRelativeTo(null);
//			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			setVisible(true);
			System.out.println("Path of the generated pdf: " + pdfPath);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(document != null) {
				document.close();
			}
		}
	}

	public static void addChartToPDF(Document document, String title, JFreeChart chart) {
		try {
			document.add(new Paragraph(title));
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 350));
			chartPanel.setChart(chart);

			BufferedImage image = chart.createBufferedImage(600, 400);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			ImageIO.write(image, "png", outputStream);
			Image chartImage = Image.getInstance(outputStream.toByteArray());

			document.add(chartImage);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	private static void insertDataIntoSQLTable(Interview interview) {
		try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			String sql = "INSERT INTO interviews (date, month, teamName, panelName, round, skill, time, currLocation, prefLocation, candidateName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, interview.getDate());
				preparedStatement.setString(2, interview.getMonth());
				preparedStatement.setString(3, interview.getTeamName());
				preparedStatement.setString(4, interview.getPanelName());
				preparedStatement.setString(5, interview.getRound());
				preparedStatement.setString(6, interview.getSkill());
				preparedStatement.setString(7, interview.getTime());
				preparedStatement.setString(8, interview.getCurrLocation());
				preparedStatement.setString(9, interview.getPrefLocation());
				preparedStatement.setString(10, interview.getCandidateName());

				preparedStatement.executeUpdate();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
