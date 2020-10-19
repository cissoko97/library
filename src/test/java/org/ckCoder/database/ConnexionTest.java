package org.ckCoder.database;

import org.ckCoder.utils.HashWordUtils;

import static org.junit.jupiter.api.Assertions.*;

class ConnexionTest {

    @org.junit.jupiter.api.Test
    void getInstance() {


        assertEquals("bonjour", "bonjour");

        System.out.println(HashWordUtils.hashWord("123456"));
    }
}
