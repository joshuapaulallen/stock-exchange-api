package org.yourotherleft.stockexchange.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminRestController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminRestController.class);

    /**
     * A ping endpoint that can be used for a health check.
     *
     * @return status of the application.
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        // log it
        LOG.info("received ping");

        // return "ok"
        return "OK";
    }
}
