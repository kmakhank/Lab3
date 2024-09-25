package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final JSONArray translations;
    private String alpha3 = "alpha3";
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
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            translations = jsonArray;

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> languages = new ArrayList<>();
        for (int i = 0; i < translations.length(); i++) {
            String tmp = translations.getJSONObject(i).getString(alpha3);
            if (tmp.equals(country)) {
                for (int j = 0; i < translations.getJSONObject(i).names().length(); j++) {
                    String key = translations.getJSONObject(i).names().getString(j);
                    if (!("alpha2".equals(key) || alpha3.equals(key) || "id".equals(key))) {
                        languages.add(translations.getJSONObject(i).names().getString(j));
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
            countries.add(translations.getJSONObject(i).getString(alpha3));
        }
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < translations.length(); i++) {
            String tmp = translations.getJSONObject(i).getString(alpha3);
            if (tmp.equals(country) && translations.getJSONObject(i).has(language)) {
                return translations.getJSONObject(i).getString(language);
            }
        }
        // NO answer found
        return null;
    }
}
