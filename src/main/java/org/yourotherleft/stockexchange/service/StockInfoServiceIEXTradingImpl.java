package org.yourotherleft.stockexchange.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.PriceRequestBuilder;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Service
public class StockInfoServiceIEXTradingImpl implements StockInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(StockInfoServiceIEXTradingImpl.class);

    private final IEXTradingClient client;

    @Autowired
    public StockInfoServiceIEXTradingImpl() {
        client = IEXTradingClient.create();
    }

    @Override
    public double getMarketPrice(final String symbol) {
        requireNonNull(symbol, "symbol is null");

        // execute the request to the IEX API
        final RestRequest<BigDecimal> priceRequest = new PriceRequestBuilder().withSymbol(symbol).build();
        final BigDecimal value = client.executeRequest(priceRequest);

        // log it
        LOG.debug("fetched price of symbol '{}', found '{}'", symbol, value);

        // convert to a double and return
        return value.doubleValue();
    }

}
