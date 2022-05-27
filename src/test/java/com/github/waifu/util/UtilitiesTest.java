package com.github.waifu.util;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.React;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class UtilitiesTest {

    @Test
    void parseRaiders() throws IOException, InterruptedException {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("WebAppResponse.json")), StandardCharsets.UTF_8));
        Set<String> set = Utilities.parseRaiders(new JSONObject(tokener));
        System.out.println(set);
    }

    @Test
    void parseRaiderReacts() throws IOException, InterruptedException {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("WebAppResponse.json")), StandardCharsets.UTF_8));
        Map<String, React> map = Utilities.parseRaiderReacts(new JSONObject(tokener));

        for (Map.Entry<String, React> m : map.entrySet()) {
            System.out.println("Name: " + m.getValue().getName());
            System.out.println("Requirement: " + m.getValue().getRequirement());
            System.out.println("Raiders:");
            for (Account a : m.getValue().getRaiders()) {
                System.out.print(a.getName() + " ");
            }
            System.out.println("\n");
        }
    }
}