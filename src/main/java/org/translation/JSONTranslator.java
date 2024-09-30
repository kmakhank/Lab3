package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    public  JSONArray translations;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        // try {
        //     System.out.println("Translations before : ");
        //     String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

        //     JSONArray jsonArray = new JSONArray(jsonString);

        //     translations = jsonArray;
        //     System.out.println("Translations: " + translations);
        // }
        // catch (IOException | URISyntaxException ex) {
        //     throw new RuntimeException(ex);
        // }
        try {
            
            var resource = getClass().getClassLoader().getResource("sample.json");
            if (resource == null) {
                throw new RuntimeException("File not found: " + filename);
            }
            String jsonString = new String(Files.readAllBytes(Paths.get(resource.toURI())), StandardCharsets.UTF_8);
            System.out.println("Translations before : " + jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);

            translations = jsonArray;
           
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*@Override
    public List<String> getCountryLanguages(String country) {
        List<String> languages = new ArrayList<>();
        for (int i = 0; i < translations.length(); i++) {
            
            String tmp = translations.getJSONObject(i).getString("alpha3");
            if (tmp.equals(country)) {
                System.out.println("Found country: " + country + " tmp = " + tmp + " index is " + i);
                //Get 
                //After getting 
                for (int j = 0; i < translations.getJSONObject(i).names().length(); j++) {
                    String key = translations.getJSONObject(i).names().getString(j);
                    if (!("alpha2".equals(key) || "alpha3".equals(key) || "id".equals(key))) {
                        languages.add(translations.getJSONObject(i).names().getString(j));
                    }
                }
            }
        }
        return languages;
    }*/

@Override
public List<String> getCountryLanguages(String country) {
    List<String> languages = new ArrayList<>();
    // System.out.println(" Starting to gather languages for country: " + country);
        for (int i = 0; i < translations.length(); i++) {
        JSONObject jsonObject = translations.getJSONObject(i);
        // System.out.println("Checking country: " + jsonObject.getString("alpha3"));
        if (jsonObject.has("alpha3")) {
            String tmp = jsonObject.getString("alpha3");
            if (tmp.equalsIgnoreCase(country)) {
                // System.out.println("Found for country: " + tmp);
                JSONArray names = jsonObject.names();
                //Check if we have names for the country
                if (names != null) {
                    //PRINT THE LENGHT OF NAMES this gets keys)
                    for (int j = 0; j < names.length(); j++) {
                        String key = names.getString(j);
                        if (!("alpha2".equals(key) || "alpha3".equals(key) || "id".equals(key))) {
                            if (jsonObject.has(key)) {
                                languages.add(key);
                            }
                        }
                    }
                }
            }
        }
    }
    return languages;
}

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();
        for (int i = 0; i < translations.length(); i++) {
            countries.add(translations.getJSONObject(i).getString("alpha3"));
        }
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < translations.length(); i++) {
            JSONObject tmp = translations.getJSONObject(i);
            String cCode = tmp.getString("alpha3");
            System.out.println("Checking country: " + cCode + " with language: " + language);
            System.out.println(tmp.has(language));
            if (tmp.has(language) && cCode.equalsIgnoreCase(country)){
                String tmp2 = tmp.getString(language);
                System.out.println("Found translation: " + tmp2);
                return tmp2;
            }

            
            
        }
        // NO answer found
        return null;
    }
}
