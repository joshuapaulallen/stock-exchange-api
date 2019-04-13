package org.yourotherleft.stockexchange.service;

/**
 * A service that fetches information about stocks.
 *
 * @author jallen
 */
public interface StockInfoService {

    /**
     * Get the market price of the stock with the given symbol.
     *
     * @param symbol The symbol.
     * @return The market price of the stock with the given symbol.
     */
    double getMarketPrice(String symbol);

}
