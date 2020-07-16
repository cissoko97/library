package org.ckCoder.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashWordUtilsTest {

    @Test
    void hashWord() {
        String password = HashWordUtils.hashWord("password");
        System.out.println(password);
    }
}
