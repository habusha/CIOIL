package gov.taxes.infra.github.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExcelWorksheet {

	private List<List<Object>> data = new ArrayList<List<Object>>();
	private final static Logger logger = LoggerFactory.getLogger(ExcelWorksheet.class.getName());

	public void addRow(List<Object> row) {
		data.add(row);
	}

	public List<List<Object>> getData() {
		return data;
	}

	public void setData(List<List<Object>> data) {
		this.data = data;
	}

	public String toJson() throws JsonProcessingException {
		List<Map<String, Object>> map = toJsonList();
		String json = new ObjectMapper().writer().writeValueAsString(map);
		logger.debug("Json from excel: ", json);
		return json;
	}

	public List<Map<String, Object>> toJsonList() {
		List<Map<String, Object>> jsonReadyList = new ArrayList<>();
		List<Object> headerRow = null;
		for (List<Object> row : data) {
			if (headerRow == null) {
				headerRow = row;
				continue;
			}
			Map<String, Object> map = new HashMap<>();
			for (int i = 0; i < row.size(); i++) {
				map.put(headerRow.get(i).toString(), row.get(i));
			}
			jsonReadyList.add(map);
		}
		return jsonReadyList;
	}

}