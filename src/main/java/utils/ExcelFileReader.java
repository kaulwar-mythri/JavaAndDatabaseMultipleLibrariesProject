package utils;

import models.Interview;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelFileReader {
    private static final int DATE_COLUMN_INDEX = 0;
    private static final int MONTH_COLUMN_INDEX = 1;
    private static final int TEAM_COLUMN_INDEX = 2;
    private static final int PANEL_COLUMN_INDEX = 3;
    private static final int ROUND_COLUMN_INDEX = 4;
    private static final int SKILL_COLUMN_INDEX = 5;
    private static final int TIME_COLUMN_INDEX = 6;
    private static final int CUR_LOC_COLUMN_INDEX= 7;
    private static final int PREF_LOC_COLUMN_INDEX = 8;
    private static final int CANDIDATE_NAME_COLUMN_INDEX = 9;

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
                interview.setMonth(getCellValueAsString(row.getCell(1)));
                interview.setTeamName(getCellValueAsString(row.getCell(2)));
                interview.setPanelName(getCellValueAsString(row.getCell(3)));
                interview.setRound(getCellValueAsString(row.getCell(4)));
                interview.setSkill(getCellValueAsString(row.getCell(5)));
                interview.setTime(getCellvalueAsTime(row.getCell(6)));
                interview.setCurrLocation(getCellValueAsString(row.getCell(7)));
                interview.setPrefLocation(getCellValueAsString(row.getCell(8)));
                interview.setCandidateName(getCellValueAsString(row.getCell(9)));

                interviewList.add(interview);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return interviewList;
    }

    private static Date getCellValueAsDate(Cell cell) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("[d]-MMM-yy");
//            return cell != null ? sdf.parse(cell.getStringCellValue()) : null;
            return cell != null ? new SimpleDateFormat("[d]-MMM-yy").parse(cell.getStringCellValue()) : null;
        } catch (ParseException | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Date getCellvalueAsTime(Cell cell) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            return cell != null ? sdf.parse(cell.getStringCellValue()) : null;
            return cell != null ? new SimpleDateFormat("HH:mm").parse(cell.getStringCellValue()) : null;
        } catch (ParseException  | IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            return cell != null ? sdf.parse(cell.getStringCellValue()) : null;
            return cell != null ? cell.getStringCellValue() : null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
    }
}

