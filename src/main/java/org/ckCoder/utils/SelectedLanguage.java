package org.ckCoder.utils;

import org.apache.commons.io.IOUtils;
import org.ckCoder.models.LanguageEmun;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class SelectedLanguage {
    public static Properties getInstace() throws IOException {
        InputStream inputStream =  null;
        if (readtoFile().equals(LanguageEmun.en.name())) {
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.en.properties");
        } else if(readtoFile().equals(LanguageEmun.fr.name()))
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.fr.properties");
        else
            inputStream = SelectedLanguage.class.getResourceAsStream("/properties/lang.en.properties");

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    public static void writeToFile(LanguageEmun languageEmun) throws IOException {
        File file = new File(SelectedLanguage.class.getResource("/config/currentlangue").getFile());
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(languageEmun.name());
        printWriter.close();
    }

    private static String readtoFile() throws IOException {
        InputStream inputStream = SelectedLanguage.class.getResourceAsStream("/config/currentlangue");
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

}
