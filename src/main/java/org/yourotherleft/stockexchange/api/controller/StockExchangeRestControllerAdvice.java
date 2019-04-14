package org.yourotherleft.stockexchange.api.controller;

import com.google.common.base.Throwables;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.NotFoundException;

/**
 * A {@link RestControllerAdvice} which prepares a standard response when handling errors.
 *
 * @author jallen
 */
@RestControllerAdvice
public class StockExchangeRestControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(StockExchangeRestControllerAdvice.class);

    /**
     * A "safety net" exception handler, which handles all throwables.
     *
     * @param t The throwble.
     * @return A {@link StockExchangeError} describing the error.
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public StockExchangeError handleException(final Throwable t) {
        // log the error
        LOG.error(String.format("unhandled exception [%s]", Throwables.getStackTraceAsString(t)));

        return new StockExchangeError(HttpStatus.INTERNAL_SERVER_ERROR.value(), t.getMessage());
    }

    /**
     * A handler for {@link NotFoundException}. Returns a 404 with a descriptive message.
     *
     * @param e The exception.
     * @return A {@link StockExchangeError} describing the error.
     */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StockExchangeError handleNotFound(final NotFoundException e) {
        return new StockExchangeError(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    /**
     * A handler for {@link HttpMessageNotReadableException}. Returns a 400.
     *
     * @param e The exception.
     * @return A {@link StockExchangeError} describing the error.
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StockExchangeError handleBadRequest(final HttpMessageNotReadableException e) {
        return new StockExchangeError(HttpStatus.BAD_REQUEST.value(), "malformed request");
    }

    /**
     * A simple pojo that describes an error.
     */
    @Data
    private static class StockExchangeError {
        private final int statusCode;
        private final String message;
    }

}
