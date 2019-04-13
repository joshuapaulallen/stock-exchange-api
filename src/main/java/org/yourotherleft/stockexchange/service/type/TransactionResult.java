package org.yourotherleft.stockexchange.service.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResult {

    private final String identifier;
    private final String symbol;
    private final TransactionAmount amount;
    private final double shares;

    public static TransactionResult of(final String identifier, final String symbol, final String currencyUnit, final double amount, final double shares) {
        return TransactionResult.builder()
                .identifier(identifier)
                .symbol(symbol)
                .shares(shares)
                .amount(
                        TransactionAmount.builder()
                                .currencyUnit(currencyUnit)
                                .value(amount)
                                .build()
                )
                .build();
    }

}
