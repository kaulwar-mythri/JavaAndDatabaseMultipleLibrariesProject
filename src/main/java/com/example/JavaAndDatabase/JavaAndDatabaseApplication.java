package com.example.JavaAndDatabase;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.*;

import com.itextpdf.layout.element.Image;
import models.Interview;
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

		try (OutputStream os = new FileOutputStream(pdfPath);
			 PdfWriter writer = new PdfWriter(os);
			 PdfDocument pdfDocument = new PdfDocument(writer);
			 Document document = new Document(pdfDocument)) {


			SQLQueries queries = new SQLQueries();
			JFreeChart chart1 = queries.MaxInterviewsQuery();
			System.out.println(chart1);
			BufferedImage image = chart1.createBufferedImage(700, 500);
			Image itextImage = new Image(ImageDataFactory.create(image, null));

			document.add(itextImage);

			JFreeChart chart2 = queries.MinInterviewsQuery();
			BufferedImage image2 = chart2.createBufferedImage(700, 500);
			Image itextImage2 = new Image(ImageDataFactory.create(image2, null));

			document.add(itextImage2);

			JFreeChart chart5 = queries.getTop3killsForPeakTime();
			BufferedImage image5 = chart5.createBufferedImage(700, 500);
			Image itextImage5 = new Image(ImageDataFactory.create(image5, null));

			document.add(itextImage5);

			JFreeChart chart4 = queries.getTop3kills();
			BufferedImage image4 = chart4.createBufferedImage(700, 500);
			Image itextImage4 = new Image(ImageDataFactory.create(image4, null));

			document.add(itextImage4);

			JFreeChart chart3 = queries.getTop3Panels(interviewList);
			System.out.println(chart3);
			BufferedImage image3 = chart3.createBufferedImage(700, 500);
			Image itextImage3 = new Image(ImageDataFactory.create(image3, null));

			document.add(itextImage3);

			System.out.println("Path of the generated pdf: " + pdfPath);
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
