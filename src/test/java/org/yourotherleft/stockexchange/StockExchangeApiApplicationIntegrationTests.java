package org.yourotherleft.stockexchange;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockExchangeApiApplicationIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
        // nothing to test
    }

    @Test
    public void testHealthCheck() throws Exception {
        mvc.perform(get("/api/v1/admin/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

    }

}
