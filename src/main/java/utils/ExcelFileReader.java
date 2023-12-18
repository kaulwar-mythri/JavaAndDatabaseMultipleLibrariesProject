package utils;

import models.Interview;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExcelFileReader {
    public List<Interview> readExcelFile(String fileName) {
        List<Interview> interviewList = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(file);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();

            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum() == 0 || row.getRowNum() > 3264) {
                    continue;
                }

//                Iterator<Cell> cellIterator = row.cellIterator();
                Interview interview = new Interview();

                interview.setDate(getCellValueAsDate(row.getCell(0)));
                interview.setMonth(getCellValueAsMonth(row.getCell(1)));
                interview.setTeamName(getCellValue(row.getCell(2)));
                interview.setPanelName(getCellValue(row.getCell(3)));
                interview.setRound(getCellValue(row.getCell(4)));
                interview.setSkill(getCellValue(row.getCell(5)));
                interview.setTime(getCellvalueAsTime(row.getCell(6)));
                interview.setCurrLocation(getCellValue(row.getCell(7)));
                interview.setPrefLocation(getCellValue(row.getCell(8)));
                interview.setCandidateName(getCellValue(row.getCell(9)));

                interviewList.add(interview);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return interviewList;
    }

    private static String getCellValueAsDate(Cell cell) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("[d]-MMM-yy");
//            return cell != null ? sdf.parse(cell.getStringCellValue()) : null;
                SimpleDateFormat sdf = new SimpleDateFormat("[d]-MMM-yy");
                return sdf.format(cell.getDateCellValue());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCellValueAsMonth(Cell cell) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("[d]-MMM-yy");
//            return cell != null ? sdf.parse(cell.getStringCellValue()) : null;
            return cell.getStringCellValue() != null ? cell.getStringCellValue() : null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static String getCellvalueAsTime(Cell cell) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return cell.getLocalDateTimeCellValue().format(formatter);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    private static String getCellValue(Cell cell) {
        try {
            if (cell == null) {
                return null;
            }
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                default:
                    return null;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return null;
    }
}

