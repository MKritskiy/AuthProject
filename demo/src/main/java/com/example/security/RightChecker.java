package com.example.security;

import com.example.models.securityModels.User;
import com.example.repository.ProductRepository;
import com.example.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
public class RightChecker implements AuthorizationManager<RequestAuthorizationContext> {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext ctx) {
        Authentication authentication = (Authentication) authenticationSupplier.get();
        try{
            Long id = Long.parseLong(ctx.getVariables().get("id"));
            return new AuthorizationDecision(checkCurrentUserRightOnProductWithId(authentication, id));
        } catch (NullPointerException | NumberFormatException e){
            return new AuthorizationDecision(checkCurrentUserRight(authentication));
        }
    }

    public boolean checkCurrentUserRightOnProductWithId(Authentication authentication, Long product_id)
    {
        try {
            int sellerNumber = productRepository.findById(product_id).get().getSellerNumber();
            String currSellerName = authentication.getPrincipal().toString();

            User user = userService.findByUsername(currSellerName);
            List<String> userRoles = user.getAuthorities().stream().map(a->a.toString()).toList();

            if (userRoles.contains("ROLE_ADMIN"))
                return true;
            if (user.getId()!=sellerNumber)
                return false;
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean checkCurrentUserRight(Authentication authentication)
    {
        try {

            String currSellerName = authentication.getPrincipal().toString();
            User user = userService.findByUsername(currSellerName);
            List<String> userRoles = user.getAuthorities().stream().map(a->a.toString()).toList();
            if (userRoles.contains("ROLE_ADMIN") || userRoles.contains("ROLE_SELLER"))
                return true;
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
