package com.github.waifu.handlers;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebAppHandlerTest {

    @Test
    void getRaidTest() {
        JSONObject jsonObject = WebAppHandler.getRaid("15982");
        assertNotNull(jsonObject);

        jsonObject = WebAppHandler.getRaid("-1");
        assertNull(jsonObject);

        jsonObject = WebAppHandler.getRaid("10000000");
        assertNotNull(jsonObject);

        jsonObject = WebAppHandler.getRaid("test");
        assertNull(jsonObject);
    }
}