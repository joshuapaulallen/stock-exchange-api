package org.yourotherleft.stockexchange.service.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {

    private final String symbol;
    private final TransactionAmount amount;

    public static TransactionRequest of(final String symbol, final String currencyUnit, final double amount) {
        return new TransactionRequest(symbol, new TransactionAmount(currencyUnit, amount));
    }
}
