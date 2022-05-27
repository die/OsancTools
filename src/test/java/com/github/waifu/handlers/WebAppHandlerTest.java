package com.github.waifu.handlers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebAppHandlerTest {

    @Test
    void getData() {
        JSONObject jsonObject = WebAppHandler.getData(14389);
        assertNotNull(jsonObject);
        System.out.println("\nObtained JSON:\n" + jsonObject + "\n");
        assertEquals(1, jsonObject.get("status"));
    }
}