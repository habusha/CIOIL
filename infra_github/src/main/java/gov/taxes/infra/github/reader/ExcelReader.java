package gov.taxes.infra.github.reader;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Util class to read an excel file and return its rows
 * 
 * @author Dan Erez
 */
public class ExcelReader {

	public static List<Map<String, Object>> read(String filename) throws InvalidFormatException, IOException {
		InputStream inp = new FileInputStream(filename);
		return read(inp);
	}

	public static List<Map<String, Object>> read(InputStream is) throws InvalidFormatException, IOException {
		Workbook workbook = null;
		ExcelWorksheetDTO sheetData = new ExcelWorksheetDTO();
		try {
			workbook = WorkbookFactory.create(is);
			// Support only one
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row currentRow = sheet.getRow(i);
				if (currentRow == null) {
					continue;
				}
				//
				List<Object> rowData = new ArrayList<Object>();
				for (int cn = 0; cn < currentRow.getLastCellNum(); cn++) {
					Cell currentCell = currentRow.getCell(cn);

					// String cellValue = dataFormatter.formatCellValue(currentCell);
					rowData.add(cellToObject(currentCell));
					/*
					 * if (currentCell.getCellTypeEnum() == CellType.STRING) {
					 * System.out.print(currentCell.getStringCellValue() + "--"); } else if
					 * (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
					 * System.out.print(currentCell.getNumericCellValue() + "--"); }
					 */

				}
				if (!isEmpty(rowData)) {
					sheetData.addRow(rowData);
				}

			}
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}

		return sheetData.toJsonList();

	}

	private static boolean isEmpty(List<Object> rowData) {
		if (rowData == null || rowData.isEmpty()) {
			return true;
		}
		for (Object obj : rowData) {
			if (obj != null && !obj.toString().trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static List<Map<String, Object>> readAsJson(byte[] buf) throws InvalidFormatException, IOException {
		return read(new ByteArrayInputStream(buf));
	}

	private static Object cellToObject(Cell cell) {
		if (cell == null || cell.getCellTypeEnum() == null) {
			return "";
		}
		switch (cell.getCellTypeEnum()) {
		case BOOLEAN:
			return cell.getBooleanCellValue();

		case STRING:
			return cell.getRichStringCellValue().getString();

		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				return numeric(cell);
			}

		case FORMULA:

			switch (cell.getCachedFormulaResultTypeEnum()) {
			case NUMERIC:
				return numeric(cell);
			case STRING:
				return cleanString(cell.getRichStringCellValue().toString());
			default:
				return "";
			}

		case BLANK:
			return "";

		default:
			return "";
		}

	}

	private static String cleanString(String str) {
		return str.replace("\n", "").replace("\r", "");
	}

	private static Object numeric(Cell cell) {
		if (HSSFDateUtil.isCellDateFormatted(cell)) {
			return cell.getDateCellValue();
		}
		double asDouble = cell.getNumericCellValue();
		if ((asDouble % 1) != 0) {
			return asDouble;
		} else {
			return (long) asDouble;
		}
	}
}
