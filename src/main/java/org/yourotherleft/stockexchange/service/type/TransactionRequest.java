package org.yourotherleft.stockexchange.service.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequest {

    private final String symbol;
    private final TransactionAmount amount;

    public static TransactionRequest of(final String symbol, final String currencyUnit, final double amount) {
        return TransactionRequest.builder()
                .symbol(symbol)
                .amount(
                        TransactionAmount.builder()
                                .currencyUnit(currencyUnit)
                                .value(amount)
                                .build()
                )
                .build();
    }
}
