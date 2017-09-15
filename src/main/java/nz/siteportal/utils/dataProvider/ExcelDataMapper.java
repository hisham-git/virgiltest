package nz.siteportal.utils.dataProvider;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import mt.siteportal.utils.tools.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class ExcelDataMapper {

	@Test(dataProvider = "getExcelData")
	public void userCofigAndClaims(Map<String, String> userData) {	
		Set<String> keys = userData.keySet();	
		System.out.println();
		for (String key : keys) {
			System.out.println(userData.get(key));
		}
		System.out.println();
	}
	
	@DataProvider
	public static Iterator<Object[]> getExcelData(Method callingMethod) throws IOException {
		FileInputStream sourceFile = null;
		Workbook workbook = null;
		ExcelDataMapper dataMapper = new ExcelDataMapper();
		ClassLoader loader = dataMapper.getClass().getClassLoader();

		List<Object[]> jParamObj = new ArrayList<>();
		List<String> configKeys = new ArrayList<String>();
		try {
			sourceFile = new FileInputStream(new File(loader.getResource("ConfigAndTestData.xlsx").toURI()));
			workbook = WorkbookFactory.create(sourceFile);
			Sheet sheet = workbook.getSheet(callingMethod.getName());

			for (Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext();) {
				Row row = rowsIT.next();
				Iterator<Cell> headerIterator = row.cellIterator();

				// Config key extraction
				if (row.getRowNum() == 0) {
					while (headerIterator.hasNext()) {
						Cell headerCell = headerIterator.next();
						configKeys.add(getCellValue(headerCell));
					}
				} else {
					dataCell: 
						for (Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext();) {
						Map<String, String> JcellObj = new HashMap<>();
						for (String getKey : configKeys) {
							Cell cellData = cellsIT.next();

							if ((cellData.getColumnIndex() == 0) && (getCellValue(cellData).equalsIgnoreCase("FALSE")))
								break dataCell;

							/*if ((getKey.equalsIgnoreCase("UserFullName")))
								Log.logInfo("Activated User(s): " + getCellValue(cellData));*/

							switch (getCellValue(cellData)) {
							case "TRUE":
								JcellObj.put(getKey, "true");
								break;
							case "FALSE":
								JcellObj.put(getKey, "false");
								break;
							case "n/a":
								JcellObj.put(getKey, null);
								break;
							case "":
								JcellObj.put(getKey, null);
								break;
							default:
								JcellObj.put(getKey, getCellValue(cellData));
								break;
							}
						}
						jParamObj.add(new Object[] { JcellObj });
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.logException("Failed to parse excel file", e);
		} finally {
//			 if (workbook != null)
//				 workbook.close();
		}
		return jParamObj.iterator();
	}
	
	/**
	 * Returns the formatted value of a cell as String regardless of the cell type
	 * 
	 * @param cell
	 * @return String 
	 * 			- Cell value as String
	 */
	private static String getCellValue(Cell cell) {
		DataFormatter formatter = new DataFormatter();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return formatter.formatCellValue(cell).trim();
		case Cell.CELL_TYPE_BOOLEAN:
			try {
				return formatter.formatCellValue(cell).trim();
			} catch (ClassCastException e) {
				return String.valueOf(cell).trim();
			}
		case Cell.CELL_TYPE_NUMERIC:
			return formatter.formatCellValue(cell).trim();
		case Cell.CELL_TYPE_BLANK:
			return formatter.formatCellValue(cell).trim();
		default:
			return "";
		}
	}
}
