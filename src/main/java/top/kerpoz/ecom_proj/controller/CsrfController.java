package top.kerpoz.ecom_proj.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller is only enabled if the property csrf.enabled is set to true
@ConditionalOnProperty(name = "csrf.enabled", havingValue = "true", matchIfMissing = true)
@RestController
@RequestMapping("/api")
public class CsrfController {

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}