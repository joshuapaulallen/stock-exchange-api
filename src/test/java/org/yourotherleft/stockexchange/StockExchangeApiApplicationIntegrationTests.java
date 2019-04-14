package org.yourotherleft.stockexchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.yourotherleft.stockexchange.service.type.TransactionRequest;
import org.yourotherleft.stockexchange.service.type.TransactionResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Ensure basic functionality of the stock exchange api.
 *
 * @author jallen
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockExchangeApiApplicationIntegrationTests {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Test
    public void testAddTransaction() throws Exception {
        final TransactionRequest transactionRequest = TransactionRequest.of("ABCD", "USD", 135.55d);
        mvc.perform(
                post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTransactionRequestAsJson(transactionRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value(transactionRequest.getSymbol()))
                .andExpect(jsonPath("$.shares").exists())
                .andExpect(jsonPath("$.identifier").exists());
    }

    @Test
    public void testAddTransaction_malformedRequest() throws Exception {
        mvc.perform(
                post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("this totally isn't valid")
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testAddTransaction_unsupportedCurrencyUnit() throws Exception {
        final TransactionRequest transactionRequest = TransactionRequest.of("ABCD", "EUR", 135.55d);
        mvc.perform(
                post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getTransactionRequestAsJson(transactionRequest))
        )
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));;
    }

    @Test
    public void testFindTransactions() throws Exception {
        // save several transactions
        final List<TransactionRequest> requests = new ArrayList<>();
        requests.add(TransactionRequest.of("MSFT", "USD", 100.0d));
        requests.add(TransactionRequest.of("MSFT", "USD", 200.0d));
        requests.add(TransactionRequest.of("SUN", "USD", 150.0d));
        requests.forEach(transactionRequest ->  {
            try {
                mvc.perform(
                        post("/api/v1/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getTransactionRequestAsJson(transactionRequest))
                )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.shares").exists())
                        .andExpect(jsonPath("$.identifier").exists());
            } catch (Exception e) {
                throw new RuntimeException(String.format("transaction request '%s' failed", transactionRequest), e);
            }
        });

        // exercise find all
        final MvcResult mvcResult = mvc.perform(get("/api/v1/transaction")).andExpect(status().isOk()).andReturn();
        final List<TransactionResult> transactionResults = getTransactionResultListFromJson(mvcResult.getResponse().getContentAsString());
        assertTrue(transactionResults.size() >= requests.size());

        // exercise find by symbol
        mvc.perform(get("/api/v1/transaction/symbol/MSFT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
        mvc.perform(get("/api/v1/transaction/symbol/BOGUS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));

        // exercise find by transaction identifier
        final TransactionResult result = transactionResults.get(0);
        mvc.perform(get("/api/v1/transaction/id/" + result.getIdentifier()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value(result.getIdentifier()))
                .andExpect(jsonPath("$.symbol").value(result.getSymbol()))
                .andExpect(jsonPath("$.amount.currencyUnit").value(result.getAmount().getCurrencyUnit()))
                .andExpect(jsonPath("$.amount.value").value(result.getAmount().getValue()));
    }

    @Test
    public void testFindByTransactionId_notFound() throws Exception {
        final String bogusIdentifier = UUID.randomUUID().toString();
        mvc.perform(get("/api/v1/transaction/id/" + bogusIdentifier))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    private String getTransactionRequestAsJson(final TransactionRequest transactionRequest) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(transactionRequest);
    }

    private List<TransactionResult> getTransactionResultListFromJson(final String transactionResultListAsJson) throws IOException {
        return OBJECT_MAPPER.readValue(transactionResultListAsJson, new TypeReference<List<TransactionResult>>() {});
    }

}
