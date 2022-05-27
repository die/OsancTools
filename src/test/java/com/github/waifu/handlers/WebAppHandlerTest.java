package com.github.waifu.handlers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebAppHandlerTest {

    @Test
    void getData() {
        JSONObject jsonObject = WebAppHandler.getData(14389);
        System.out.println("\nObtained JSON:\n" + jsonObject + "\n");
    }
}