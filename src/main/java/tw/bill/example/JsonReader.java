package tw.bill.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by bill33 on 2016/6/11.
 */
public class JsonReader {

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/eddard33/Documents/Hackthon/sample_training.json";
        ObjectMapper mapper = new ObjectMapper();
        List<Record> records = new ArrayList<>();

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(item -> {
                //System.out.println(item);
                try {
                    records.add(mapper.readValue(item, Record.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(records.toString());
        System.out.println(records.size());

        Map<String, Map<String, Integer>> userAndAuthorMap = new HashMap<>();

        records.stream().forEach(item -> {
            Map<String, Integer> authorMap = userAndAuthorMap.get(item.getCookie_pta()) != null ? userAndAuthorMap.get(item.getCookie_pta()) : new HashMap<>();
            userAndAuthorMap.put(item.getCookie_pta(), authorMap);
            int readCount = authorMap.get(item.getAuthor_id()) != null ? authorMap.get(item.getAuthor_id()) + 1 : 1;
            authorMap.put(item.getAuthor_id(), readCount);
            if(readCount>1) System.out.println(readCount);
        });

        System.out.println(userAndAuthorMap);


//        ObjectMapper mapper = new ObjectMapper();
//        List<Record> records = mapper.readValue(new File("/Users/eddard33/Documents/Hackthon/sample_training.json"), new TypeReference<List<Record>>(){});
//        System.out.println(records.size());


//        ObjectMapper mapper = new ObjectMapper();
//        Staff obj = new Staff();
//
//        //Object to JSON in file
//        mapper.writeValue(new File("/Users/eddard33/Documents/Hackthon/test"), obj);
//
//        //Object to JSON in String
//        String jsonInString = mapper.writeValueAsString(obj);
//        System.out.println(jsonInString);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonInString = "{'name' : 'mkyong'}";

//        ObjectMapper mapper = new ObjectMapper();
//        String jsonInString = "{'name' : 'mkyong'}";
//        //JSON from file to Object
//        Staff obj = mapper.readValue(new File("/Users/eddard33/Documents/Hackthon/test"), Staff.class);
//
//        //JSON from URL to Object
//        obj = mapper.readValue(new URL("http://mkyong.com/api/staff.json"), Staff.class);
//
//        //JSON from String to Object
//        obj = mapper.readValue(jsonInString, Staff.class);
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class Record {
    @JsonProperty("cookie_pta")
    private String cookie_pta;
    @JsonProperty("author_id")
    private String author_id;

    public String getCookie_pta() {
        return cookie_pta;
    }

    public void setCookie_pta(String cookie_pta) {
        this.cookie_pta = cookie_pta;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Record{" +
                "cookie_pta='" + cookie_pta + '\'' +
                ", author_id='" + author_id + '\'' +
                '}';
    }
}

class Staff {
    private String name;
    private int age;
    private String position;
    private BigDecimal salary;
    private List<String> skills;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}