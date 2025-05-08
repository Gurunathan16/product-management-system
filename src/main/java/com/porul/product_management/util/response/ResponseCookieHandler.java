package com.porul.product_management.util.response;

import org.springframework.http.ResponseCookie;

public class ResponseCookieHandler
{
    public static ResponseCookie responseCookieBuilder(String cookieName, String cookie, Boolean httpOnly,
                                                       Boolean Secure,
                                                       String sameSite, String path,
                                                       long maxAge)
    {
        return ResponseCookie.from(cookieName, cookie)
                .httpOnly(httpOnly)
                .secure(Secure)
                .sameSite(sameSite)
                .path(path)
                .maxAge(maxAge)
                .build();
    }


}
