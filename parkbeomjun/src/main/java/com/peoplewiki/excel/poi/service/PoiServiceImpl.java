package com.peoplewiki.excel.poi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peoplewiki.excel.poi.domain.PoiBean;
import com.peoplewiki.excel.poi.persistence.PoiDao;

@Service("poiService")
public class PoiServiceImpl implements PoiService {

	@Autowired
	private PoiDao poiDao;

	public void setPoiDao(PoiDao poiDao) {
		this.poiDao = poiDao;
	}

	@Override
	public void insertForm1(String path) throws Exception {
		List<List<String>> data = convertForm1(path);
		List<String> dataSet = new ArrayList<String>();

		String[] x = new String[9];

		for (int i = 0; i < data.size(); i++) {
			dataSet = data.get(i);
			x = new String[9];
			/*
			 * for (int k = 0 ; k < dataSet.size(); k++) { x[k] =
			 * dataSet.get(k); }
			 */

			PoiBean p = new PoiBean(dataSet.get(0), dataSet.get(1), dataSet.get(2), dataSet.get(3), dataSet.get(4), dataSet.get(5),
					dataSet.get(6), dataSet.get(7), dataSet.get(8));
			poiDao.insertForm1(p);
		}
	}

	@Override
	public void insertForm2(String path) throws Exception {

		Map<String, Object> map = convertForm2(path);
		try {
			@SuppressWarnings("unchecked")
			List<String> mapDataHour = (List<String>) map.get("hour");
			@SuppressWarnings("unchecked")
			Map<String, List<String>> mapDataMinute = (Map<String, List<String>>) map.get("minute");
			mapDataMinute.remove("");
			List<String> mapDataSetMinute = new LinkedList<String>();

			String hour = "";
			String value = "";
			for (int k = 0; k < mapDataHour.size(); k++) {
				hour = mapDataHour.get(k);
				Iterator<?> entries = mapDataMinute.entrySet().iterator();
				while (entries.hasNext()) {
					@SuppressWarnings("rawtypes")
					Entry thisEntry = (Entry) entries.next();
					String key = (String) thisEntry.getKey();

					mapDataSetMinute = mapDataMinute.get(key);
					if (mapDataSetMinute == null)
						continue;
					value = mapDataSetMinute.get(k);

					PoiBean poiBean = new PoiBean(hour, key, value);

					poiDao.insertForm2(poiBean);
				}
			}
			System.out.println("=======complete=========");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<List<String>> convertForm1(String path) throws Exception {
		XSSFWorkbook wb = null;
		try {
			FileInputStream file = new FileInputStream(new File(path));
			wb = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<List<String>> data = new ArrayList<List<String>>();
		List<String> dataSet = new ArrayList<String>();
		boolean isNull = false;

		int lastCellNum = 0;

		XSSFSheet sheet = wb.getSheetAt(0);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {// 행 구분
			XSSFRow row = sheet.getRow(i);

			lastCellNum = lastCellNum == 0 ? row.getLastCellNum() : lastCellNum;

			dataSet = new ArrayList<String>();
			loop: for (int cn = 0; cn < lastCellNum; cn++) { // 열구분

				Cell cell = row.getCell(cn);
				String str = new String();

				isNull = false;

				cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
				if (cell.toString().equals("") || cell.toString().equals(null)) {
					dataSet.add("NaN");
					continue loop;
				}

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					str = str + cell.getRichStringCellValue().getString();
					break;

				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
						str = str + cell.getDateCellValue().toString();
					else
						str = str + Integer.toString((int) cell.getNumericCellValue());
					break;

				case Cell.CELL_TYPE_BOOLEAN:
					str = str + cell.getBooleanCellValue();
					break;

				case Cell.CELL_TYPE_FORMULA:
					str = str + cell.getCellFormula();
					break;
				default:
					isNull = true;
				}
				if (isNull != true) {
					str = str + "";
				}

				dataSet.add(str);

			}
			data.add(dataSet);
		}

		return data;
	}

	public Map<String, Object> convertForm2(String path) throws Exception {
		XSSFWorkbook wb = null;
		// Workbook wb = WorkbookFactory.create(fis);
		try {
			FileInputStream file = new FileInputStream(new File(path));
			wb = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> mapDataHour = new LinkedList<String>();
		Map<String, List<String>> mapDataMinute = new LinkedHashMap<String, List<String>>();
		List<String> mapDataSetMinute = new LinkedList<String>();

		boolean isNull = false;

		int lastCellNum = 0;

		XSSFSheet sheet = wb.getSheetAt(1);
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {// 행 구분
			XSSFRow row = sheet.getRow(i);

			lastCellNum = lastCellNum == 0 ? row.getLastCellNum() : lastCellNum;

			mapDataSetMinute = new LinkedList<String>();
			String hourKey = "";

			loop: for (int cn = 0; cn < lastCellNum; cn++) { // 열구분

				Cell cell = row.getCell(cn);
				String str = new String();

				isNull = false;

				cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
				if (cell.toString().equals("") || cell.toString().equals(null)) {
					mapDataSetMinute.add("NaN");
					continue loop;
				}

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					str = str + cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
						str = str + cell.getDateCellValue().toString();
					else
						str = str + Integer.toString((int) cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					str = str + cell.getBooleanCellValue();
					break;
				case Cell.CELL_TYPE_FORMULA:
					str = str + cell.getCellFormula();
					break;
				default:
					isNull = true;
				}
				if (isNull != true) {
					str = str + "";
				}

				if (i == 0) {
					// 첫 행은 시간을 저장
					mapDataHour.add(str);
				} else {
					// 분 단위 첫 열은 키값으로 저장
					if (cn == 0) {
						hourKey = str;
					} else {
						mapDataSetMinute.add(str);
					}
				}
			}
			mapDataMinute.put(hourKey, mapDataSetMinute);
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("hour", mapDataHour);
		returnMap.put("minute", mapDataMinute);

		return returnMap;

	}

	@Override
	public List<PoiBean> listForm1() throws Exception {
		return poiDao.listForm1();
	}

	@Override
	public List<PoiBean> listForm2() throws Exception {
		return poiDao.listForm2();
	}

	@Override
	public void delForm1() throws Exception {
		poiDao.delForm1();
	}

	@Override
	public void delForm2() throws Exception {
		poiDao.delForm2();
	}

}
