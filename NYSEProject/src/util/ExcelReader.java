package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	
	private Workbook workbook;
	
	/**
	 * Constructor will return workbook instance object
	 * 
	 * @param filepath
	 * 				- File path of the workbook
	 * 
	 */
	public ExcelReader(String filepath) throws IOException
	{
		workbook = new XSSFWorkbook(new FileInputStream(filepath));
	}
	
	/**
	 * Returns sheet name.
	 * 
	 * @param sheetName
	 *            - Name of the sheet
	 * @return sheetName
	 */
	private Sheet getSheet(String sheetName)
	{
		return workbook.getSheet(sheetName);
	}
	
	/**
	 * Returns number of rows in a given sheet.
	 * 
	 * @param sheetName
	 *            - Name of the sheet
	 * @return rownumbers
	 */
	public int getRowCounts(String sheetName)
	{
		return getSheet(sheetName).getLastRowNum();
	}

	/**
	 * Read and returns the sheet data as a Map.
	 * 
	 * @param rownum
	 *            - Row number to be read
	 * @param sheetName
	 *            - Name of the sheet
	 * @return sheetvalues as Map
	 */
	public Map<String, String> getSheetData(int rownum, String sheetName) {
		final List<String> rowData = new ArrayList<String>();
		final Map<String, String> rowVal = new LinkedHashMap<String, String>();
		Object value = null;
		final Sheet sheet = getSheet(sheetName);
		final List<String> coulmnNames = getColumns(sheet);
		final Row row = sheet.getRow(rownum);
		final int firstCellNum = row.getFirstCellNum();
		final int lastCellNum = row.getLastCellNum();
		for (int j = firstCellNum; j < lastCellNum; j++) {
			final Cell cell = row.getCell(j);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				rowData.add("");
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				final Double val = cell.getNumericCellValue();
				value = val.intValue();// cell.getNumericCellValue();
				rowData.add(value.toString());
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				rowData.add(cell.getStringCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN
					|| cell.getCellType() == Cell.CELL_TYPE_ERROR
					|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				throw new RuntimeException(" Cell Type is not supported ");
			}
			rowVal.put(coulmnNames.get(j), rowData.get(j));
		}
		return rowVal;

	}
	
	/**
	 * Returns Column values from the given sheet.
	 * 
	 * @param sheet
	 *            - sheet
	 * @return columnvalues
	 */
	private List<String> getColumns(Sheet sheet) {
		final Row row = sheet.getRow(0);
		final List<String> columnValues = new ArrayList<String>();
		final int firstCellNum = row.getFirstCellNum();
		final int lastCellNum = row.getLastCellNum();
		for (int i = firstCellNum; i < lastCellNum; i++) {
			final Cell cell = row.getCell(i);
			columnValues.add(cell.getStringCellValue());
		}
		return columnValues;
	}
	
}
