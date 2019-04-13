package org.yourotherleft.stockexchange.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;

import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link StockInfoServiceIEXTradingImpl}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockInfoServiceIEXTradingImplTest {

    @Autowired
    private StockInfoServiceIEXTradingImpl stockInfoService;

    @Test
    public void test() {
        double aapl = stockInfoService.getMarketPrice("AAPL");
        assertTrue(aapl > 0);
    }

    @Test(expected = IEXTradingException.class)
    public void testBogus() {
        stockInfoService.getMarketPrice("not a real symbol, yo");
    }
}
