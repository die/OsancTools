package com.github.waifu.handlers;

import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WebAppHandlerTest {

    @Test
    void getData() throws IOException, InterruptedException {
        JSONObject jsonObject = WebAppHandler.getData(15982);
        JSONObject jsonObject2 = WebAppHandler.getData(15981);
        System.out.println("\nObtained JSON:\n" + jsonObject + "\n");
        Set<String> raiders = Utilities.parseRaiders(jsonObject);
        Set<String> raiders2 = Utilities.parseRaiders(jsonObject2);

        for (String s : raiders) {
            for (String s1 : raiders2) {
                if (s.equals(s1)) {
                    System.out.println(s);
                }
            }
        }
    }
}