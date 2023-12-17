package utils;

import models.Interview;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        System.out.println(interviewList.size());

        try(InputStream fileInputStream = new FileInputStream(fileName)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            System.out.println(workbook);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();

            //skip first row column names
            if(rowIterator.hasNext())
                rowIterator.next();

            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();
                Interview interview = new Interview();

                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int colIndex = cell.getColumnIndex();

                    switch (colIndex) {
                        case DATE_COLUMN_INDEX:
                            SimpleDateFormat sdf = new SimpleDateFormat("d-MMM-yy");
                            interview.setDate(sdf.format(cell.getDateCellValue()));
                            break;
                        case MONTH_COLUMN_INDEX:
                            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-yy");
                            interview.setMonth(sdf1.format(cell.getDateCellValue()));
                            break;
                        case TEAM_COLUMN_INDEX:
                            interview.setTeamName(cell.getStringCellValue());
                            break;
                        case PANEL_COLUMN_INDEX:
                            interview.setPanelName(cell.getStringCellValue());
                            break;
                        case ROUND_COLUMN_INDEX:
                            interview.setRound(cell.getStringCellValue());
                            break;
                        case SKILL_COLUMN_INDEX:
                            interview.setSkill(cell.getStringCellValue());
                            break;
                        case TIME_COLUMN_INDEX:
                            SimpleDateFormat sdf2 = new SimpleDateFormat("h:mma");
                            interview.setTime(sdf2.format(cell.getLocalDateTimeCellValue()));
                            break;
                        case CUR_LOC_COLUMN_INDEX:
                            interview.setCurrLocation(cell.getStringCellValue());
                            break;
                        case PREF_LOC_COLUMN_INDEX:
                            interview.setPrefLocation(cell.getStringCellValue());
                            break;
                        case CANDIDATE_NAME_COLUMN_INDEX:
                            interview.setCandidateName(cell.getStringCellValue());
                            break;
                    }
                }
                interviewList.add(interview);

            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(interviewList.size());
        return interviewList;
    }
}

