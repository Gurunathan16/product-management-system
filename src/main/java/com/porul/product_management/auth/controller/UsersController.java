package com.porul.product_management.auth.controller;

import com.porul.product_management.auth.dto.UsersPasswordChange;
import com.porul.product_management.auth.dto.UsersUpdate;
import com.porul.product_management.util.response.ResponseEntityHandler;
import com.porul.product_management.auth.dto.UsersLogin;
import com.porul.product_management.auth.dto.UsersRegistration;
import com.porul.product_management.auth.dto.TokenRequest;
import com.porul.product_management.auth.service.impl.UsersServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class UsersController {
    private final UsersServiceImpl usersService;

    UsersController(UsersServiceImpl usersService)
    {
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> userLogin(@Valid @RequestBody UsersLogin usersLogin,
                                                         BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.login(usersLogin);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>>  userRegistration(@Valid @RequestBody UsersRegistration user,
                                                                 BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.register(user);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> profileUpdate(@Valid @RequestBody UsersUpdate user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.update(user);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> passwordUpdate(@Valid @RequestBody UsersPasswordChange usersPasswordChange, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.updatePassword(usersPasswordChange);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile()
    {
        return usersService.viewProfile();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> userDelete()
    {
        return usersService.delete();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map<String, Object>> getRefreshToken(@Valid @RequestBody TokenRequest tokenRequest, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            return validationErrorBuilder(bindingResult);

        return usersService.refreshTokens(tokenRequest);
    }

    private ResponseEntity<Map<String, Object>> validationErrorBuilder(BindingResult bindingResult)
    {
        return ResponseEntityHandler.getResponseEntity(HttpStatus.BAD_REQUEST, "Validation check failed.",
                "Validation" +
                    " " +
                        "Errors",
                bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
    }

    /*@GetMapping("/csrf")
    public String getCsrf(@CookieValue("XSRF-TOKEN") String token)
    {
        return token;
    }*/
}
