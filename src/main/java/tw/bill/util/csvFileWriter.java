package tw.bill.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class csvFileWriter {

	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	/**
	 * 讀取CSV檔案
	 * 
	 * @param fileName
	 * @return
	 */
	public static StringBuffer readCsvFile(String fileName) {
		// Create a new list of student objects
		StringBuffer result = new StringBuffer();
		BufferedReader fileReader = null;
		FileWriter fileWriter = null;

		try {
			// 讀取檔案資料並存入StringBuffer
			int index = 0;
			String line = "";
			fileReader = new BufferedReader(new FileReader(fileName));
			fileReader.readLine();
			while ((line = fileReader.readLine()) != null) {
				String a = line.toString();
				result.append(a).append(NEW_LINE_SEPARATOR);
			}
			System.out.println("[RESULT]:" + result);
			return result;
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 轉換整理CSV資料
	 * 
	 * @param fileName
	 * @param foodDataMapping
     *@param reverseFoodDataMapping @return
	 */
	public static Map<Integer, String> transferReadCsvFile(String fileName, String searchType, Map<Integer, String> foodDataMapping, Map<String, Integer> reverseFoodDataMapping) {
		// Create a new list of student objects
		StringBuffer result = new StringBuffer();
		BufferedReader fileReader = null;
		FileWriter fileWriter = null;
		foodDataMapping.clear();
        reverseFoodDataMapping.clear();
		try {
			// 讀取檔案資料並存入StringBuffer
			int index = 0;
			boolean checkRead = false;
			String line = "";

			fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "BIG5"));
			fileReader.readLine();
			while ((line = fileReader.readLine()) != null) {
				String[] a = line.split(",");
				int length = a.length;
				if (searchType.equals(a[0])) {
					checkRead = true;
				}
				System.out.println(a[0].toString());
				if (!("coding book for diabete".equals(a[0]) || "coding book for health".equals(a[0]) ||
					  "coding book for cal".equals(a[0]) || "coding book for gallstone".equals(a[0]))) {
					if(checkRead && index < 2){
						index++;
						
						if (!(length <= 1)) {
							if ("".equals(a[0].toString()) && "1".equals(a[1].toString())) {
								for (int i = 0; i < a.length; i++) {
									foodDataMapping.put(i, "");
								}
							} else {
								for (int i = 0; i < a.length; i++) {
									foodDataMapping.put(i, a[i].toString());
                                    reverseFoodDataMapping.put(a[i].toString(), i);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}

		return foodDataMapping;
	}

	/**
	 * 更新食材資料檔
	 * 
	 * @param fileName
	 * @param originData
	 * @param userData
	 */
	public static void writeCsvFile(String outputPath, StringBuffer originData, List<String> userData) {

		// Create a new list of student objects
		BufferedReader fileReader = null;
		FileWriter fileWriter = null;

		try {
			// 將WEB收集到的資料加入並輸出檔案資料
			fileWriter = new FileWriter(outputPath);

			// Write a new student object list to the CSV file
			for (String inputData : userData) {
				originData.append("1").append(COMMA_DELIMITER).append(inputData).append(COMMA_DELIMITER).append("8")
						.append(NEW_LINE_SEPARATOR);
			}

			fileWriter.append(originData);

			System.out.println("CSV file was created successfully !!!");

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

}
