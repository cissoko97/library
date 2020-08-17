package org.ckCoder.utils;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

class HashWordUtilsTest {

    @Test
    void hashWord() {
        String password = HashWordUtils.hashWord("password");
        System.out.println(password);
    }

    @Test
    void testFileOn() {
        try {

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("domain", "update", "Start001");
            SmbFile smbFile = new SmbFile("smb://10.1.1.16/update/Nouveau document texte.txt", auth);


            Assertions.assertEquals(true, smbFile.canRead());
            Assertions.assertEquals(true, smbFile.isFile());

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new SmbFileInputStream(smbFile)))) {
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    System.out.println(line);
                }
            }
            File file = new File("/home/cissoko/test.txt");

            FileUtils.copyInputStreamToFile(smbFile.getInputStream(), file);
        } catch (Exception ex) {
            System.err.println("Dans la methode catch");
            System.err.println(ex);
        }
    }
}
