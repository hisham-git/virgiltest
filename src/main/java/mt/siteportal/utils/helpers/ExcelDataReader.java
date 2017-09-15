package mt.siteportal.utils.helpers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import mt.siteportal.utils.tools.Log;

/**
 * Custom Excel Parser Class. Mainly used as a helper for parsing test
 * parameters from external Excel sources.
 * 
 * @author Syed A. Zawad
 *
 */
public class ExcelDataReader {

	/*
	 * Some helpful objects
	 */
	private String fullFileName;
	private final static String FILEPATH = "src\\main\\resources\\";
	private Workbook excelFile;
	private Sheet currentSheet;

	/*
	 * A Map of the column and row headers to their corresponding column and row
	 * indexes
	 */
	private HashMap<String, Integer> columns;
	private HashMap<String, Integer> rows;

	/*
	 * Used to format the Cell values correctly for display as String
	 */
	private DataFormatter formatter;

	/**
	 * Contructor. Takes in the filename of the excel file. This file should be
	 * kept in the src/main/resources directory
	 * 
	 * @param fileName
	 *            - String - name of the file and its extension. Eg.
	 *            FILENAME.extension
	 */
	public ExcelDataReader(String fileName) {
		formatter = new DataFormatter();
		this.fullFileName = FILEPATH + fileName;
		try {
			excelFile = WorkbookFactory.create(new File(fullFileName));
			currentSheet = excelFile.getSheetAt(0);
		} catch (InvalidFormatException e) {
			Log.logError(String.format("Could not parse file [%s] in path [%s]...", fileName, fullFileName));
			e.printStackTrace();
		} catch (IOException e) {
			Log.logError(String.format("Error reading file [%s] in path [%s]...", fileName, fullFileName));
			e.printStackTrace();
		}
	}

	/**
	 * Takes the sheet name and sets it as the sheet from which all the data is
	 * taken
	 * 
	 * @param sheetName
	 *            - String - name of the sheet
	 * @return ExcelDataReader - this particular instance
	 */
	public ExcelDataReader fromSheet(String sheetName) {
		currentSheet = excelFile.getSheet(sheetName);
		populateColumnMap();
		populateRowMap();
		return this;
	}

	/**
	 * Generates the map of the column name to column index for faster location
	 * of data Assumes that the top row is always the column headers
	 */
	private void populateColumnMap() {
		Row rowHeader = currentSheet.getRow(currentSheet.getTopRow());
		columns = new HashMap<String, Integer>();
		for (Cell cell : rowHeader) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				columns.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
			else {
				System.out.printf("There was an issue in trying to create the column map. The cell was of Type ["
						+ cell.getCellType() + "] and had data [" + formatter.formatCellValue(cell) + "]\n");
				return;
			}
		}
	}

	/**
	 * Generates the map of the row name to row index for faster location of
	 * data Assumes that the first cell is always the row name
	 */
	private void populateRowMap() {
		rows = new HashMap<String, Integer>();
		for (Row row : currentSheet) {
			Cell cell = row.getCell(row.getFirstCellNum());
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				rows.put(cell.getStringCellValue().trim(), cell.getRowIndex());
			else {
				System.out.println("There was an issue in trying to create the row map.");
				return;
			}
		}
	}

	/**
	 * Takes the name of the row and column for the desired data and returns
	 * that Cell object
	 * 
	 * @param rowName
	 *            - String - the name of the row
	 * @param columnName
	 *            - String - the name of the column
	 * @return Cell - the cell object at that corresponding row and column
	 */
	private Cell getData(String rowName, String columnName) {
		Integer columnIndex = columnName != null ? columns.get(columnName) : 1;
		Integer rowIndex = rowName != null ? rows.get(rowName) : 1;
		if(columnIndex == null || rowIndex == null){
			System.out.println(String.format("The cell for [%s, %s] is null...", rowName, columnName));
			return null;
		}
		return currentSheet.getRow(rowIndex).getCell(columnIndex);
	}

	/**
	 * Returns the value of the Cell at a particular row and column as String
	 * 
	 * @param rowName
	 *            - String - name of the Row
	 * @param columnName
	 *            - String - name of the Column
	 * @return - String - the data as String
	 */
	public String getDataAsStringFor(String rowName, String columnName) {
		Cell cell = getData(rowName, columnName);
		return formatter.formatCellValue(cell);
	}

	/**
	 * Returns the value of the Cell at a particular row and column as Boolean
	 * If the Cell is blank or is 0, will return false
	 * If the Cell is "X" or is 1, will return true
	 * 
	 * @param rowName - String - the name of the Row
	 * @param columnName - String - the name of the Column
	 * @return boolean
	 */
	public boolean getDataAsBooleanFor(String rowName, String columnName) {
		Cell cell = getData(rowName, columnName);
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return false;
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_STRING:
			if (cell.getStringCellValue().equalsIgnoreCase("X"))
				return true;
		case Cell.CELL_TYPE_NUMERIC:
			if (cell.getNumericCellValue() == 1)
				return true;
			else if (cell.getNumericCellValue() == 0)
				return false;
		default:
			Log.log(String.format("The data at cell [%s, %s] could not be converted to boolean. The data was [%s]",
					rowName, columnName, getDataAsStringFor(rowName, columnName)));
			return false;
		}
	}

	/**
	 * Gets the value of the first row for column specified
	 * 
	 * @param columnName - String - the name of the column
	 * @return String - the data
	 */
	public String getDataAsStringForColumn(String columnName) {
		return getDataAsStringFor(null, columnName);
	}
	
	/**
	 * Gets the value of the first column for row specified
	 * 
	 * @param rowName - String - the name of the row
	 * @return String - the data
	 */
	public String getDataAsStringForRow(String rowName) {
		return getDataAsStringFor(rowName, null);
	}

}
