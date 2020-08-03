package org.ckCoder.utils;

import org.ckCoder.models.LanguageEmun;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SelectedLanguage {
    public static Properties getInstace(LanguageEmun lang) throws IOException {
        InputStream inputStream =  null;
        if (readtoFile().equals(LanguageEmun.en.name())) {
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.en.properties");
        } else if(readtoFile().equals(LanguageEmun.fr.name()))
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.fr.properties");
        else if (lang.compareTo(LanguageEmun.AUCUN) != 0)
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang."+lang.name()+".properties");
        else
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.en.properties");

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    public static void writeToFile(LanguageEmun languageEmun) throws IOException {
        File file = new File("src/main/java/org/ckCoder/file/currentlangue");
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(languageEmun.name());
        printWriter.close();
    }

    private static String readtoFile() throws IOException {
        Path path = Paths.get("src/main/java/org/ckCoder/file/currentlangue");
        return Files.readAllLines(path).get(0);
    }

}
