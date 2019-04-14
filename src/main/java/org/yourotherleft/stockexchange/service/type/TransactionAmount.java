package org.yourotherleft.stockexchange.service.type;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionAmount {

    private final String currencyUnit;
    private final double value;

}
