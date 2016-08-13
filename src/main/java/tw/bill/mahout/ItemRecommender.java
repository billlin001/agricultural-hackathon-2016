package tw.bill.mahout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Service;

import tw.bill.util.csvFileWriter;

@Service
public class ItemRecommender {
	static final String FoodFilePath = "data/codingBook.csv";
	static final String filePath = "data/gallstone.csv";
	static final String FileOutPutPath = "data/recommenderList.csv";

	Map<Integer, String> foodDataMapping = new HashMap<Integer, String>();
    Map<String, Integer> reverseFoodDataMapping = new HashMap<>();

	StringBuffer getCsvData = new StringBuffer();

	public ItemRecommender() {
		setFoodData();
		getCsvData = csvFileWriter.readCsvFile(filePath);
		
	}

	/**
	 * 取得食材代號名稱對應資料
	 */
	public void setFoodData() {
		//DEMO預設coding book for cal
        csvFileWriter.transferReadCsvFile(FoodFilePath, "coding book for cal", foodDataMapping, reverseFoodDataMapping);
	}

	/**
	 * 將使用者所選食材資訊加入推薦分析檔
	 * 
	 */
	public boolean addMaterial(List<String> userData) {
		boolean result = true;

		try {
			csvFileWriter.writeCsvFile(FileOutPutPath, getCsvData, userData);
		} catch (Exception e) {
			result = false;
			System.out.println("addMaterial Error : " + e.toString());
		}

		return result;
	}

	/**
	 * 執行Item-based Recommender
	 * 
	 */
	public List<String> doRecommender(List<String> userData) {
		System.out.println("start");

        System.out.println("reverseFoodDataMapping: " + reverseFoodDataMapping);
        List<String> converUserData = new ArrayList<>();
        for(String item : userData) {
            converUserData.add(String.valueOf(reverseFoodDataMapping.get(item)));
        }
        System.out.println("converUserData: " + converUserData);
        addMaterial(converUserData);

        List<String> results = new ArrayList<>();

        Iterator<Entry<Integer, String>> iterator = foodDataMapping.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, String> entry = iterator.next();
			System.out.println(entry.getKey() + "：" + entry.getValue());
		}
		System.out.println("[!!!!!!!!!!!!!]:"+foodDataMapping.get(1));
		try {
			DataModel model = new FileDataModel(new File(FileOutPutPath));
			ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(model);
			GenericItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
			List<RecommendedItem> itemRecommendations = itemRecommender.recommend(1, 3);

			for (RecommendedItem itemRecommendation : itemRecommendations) {
				System.out.println("Item: " + itemRecommendation.getItemID() + " " +
						           "ItemName: " + foodDataMapping.get((int)itemRecommendation.getItemID()) + " " +
						           "ItemValue: " + itemRecommendation.getValue());
                results.add(foodDataMapping.get((int)itemRecommendation.getItemID()));
			}

		} catch (Exception e) {
			System.out.println("doRecommender Error : " + e.toString());

		}

		System.out.println("end");
        return results;
	}

	public static void main(String[] args) {
		String filePath = "";

		ItemRecommender itemRecommender = new ItemRecommender();

		// 到時候是直接取得資料傳入
		List<String> userData = new ArrayList();
		userData.add("牛奶");
		userData.add("粟米");
		userData.add("紅蘿蔔");

		List<String> results = itemRecommender.doRecommender(userData);
        System.out.println(results);
    }
}
