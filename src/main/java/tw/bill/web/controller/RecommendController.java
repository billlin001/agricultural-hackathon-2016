package tw.bill.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.bill.mahout.ItemRecommender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bill33 on 2016/8/12.
 */
@RestController
public class RecommendController {
    Logger log = LoggerFactory.getLogger(RecommendController.class);

//    @CrossOriginx
    @RequestMapping(value = "/recommend/ingredient", method = RequestMethod.POST)
    public ResponseEntity<List<RecommendIngredientDto>> recommendIngredient(@RequestBody IngredientDto intgredientDto) {
        log.info(intgredientDto.toString());

        List<String> inputIngredients = intgredientDto.getIngredients();

        ItemRecommender itemRecommender = new ItemRecommender();
        List<String> names = itemRecommender.doRecommender(inputIngredients);

//        List<String> names = new ArrayList<>(Arrays.asList(new String[]{"高麗菜","豬肉","青江菜","洋蔥","米酒"}));

        List<RecommendIngredientDto> results = new ArrayList<>();
        for(String item : names) {
            RecommendIngredientDto dto = new RecommendIngredientDto(item, false);
            results.add(dto);
        }
        return new ResponseEntity<List<RecommendIngredientDto>>(
                results,
                HttpStatus.OK
        );
    }

//    @CrossOrigin
    @RequestMapping("/recommend/recipes")
    public List<String> recommendRecipe(List<String> ingredients) {
        return  new ArrayList<String>(Arrays.asList(new String[]{"雜菜薯仔瘦肉湯","馬鈴薯泥","泡菜雞絲沙拉","腐乳油醋雞絲沙拉","奇亞籽高纖美身飲"}));
    }
}

class IngredientDto {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

class RecommendIngredientDto {
    private String name;
    private boolean isSelect = false;

    public RecommendIngredientDto(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}