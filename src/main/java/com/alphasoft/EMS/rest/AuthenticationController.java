package com.alphasoft.EMS.rest;


import com.alphasoft.EMS.service.JwtService;
import com.alphasoft.EMS.dto.AuthenticationResponse;
import com.alphasoft.EMS.dto.LoginRequest;
import com.alphasoft.EMS.dto.LogoutResponse;
import com.alphasoft.EMS.dto.RegisterRequest;
import com.alphasoft.EMS.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;

    @PostMapping("/register") // Test success
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse // مهم لإضافة الكوكي
    ) {
        String token = service.register(request, httpRequest.getHeader("User-Agent"));

        ResponseCookie cookie = ResponseCookie.from("CashFlow-EMS-JWT", token)
                .httpOnly(true)
                .secure(false)    // ← false للمحلي (HTTP)
                .path("/")
                .domain("localhost")     // ← null للمحلي
                .maxAge(jwtService.expiration / 1000)
                .sameSite("Lax")  // ← Lax للمحلي
                .build();

        // 3. إضافة الكوكي إلى الرد
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity.ok()
                .body(AuthenticationResponse.builder()
                        .status("Sign up success")
                        .message("We`re glad you joined, " + jwtService.extractUsername(token))
                        .build()
                );
    }

    @PostMapping("/login") // Test success
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        String token = service.login(request, httpRequest.getHeader("User-Agent"));

        ResponseCookie cookie = ResponseCookie.from("CashFlow-EMS-JWT", token)
                .httpOnly(true)
                .secure(false)    // ← false للمحلي (HTTP)
                .path("/")
                .domain("localhost")     // ← null للمحلي
                .maxAge(jwtService.expiration / 1000)
                .sameSite("Lax")  // ← Lax للمحلي
                .build();

        // 3. إضافة الكوكي إلى الرد
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .body(AuthenticationResponse.builder()
                        .status("Login success")
                        .message("Welcome Back, " + jwtService.extractUsername(token))
                        .build()
                );
    }

    @PostMapping("/logout") // Test success
    public ResponseEntity<?> logout(
            HttpServletResponse httpResponse,
            HttpServletRequest httpRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt
    ) {
        String jti = jwtService.extractJti(jwt);
        String username = jwtService.extractUsername(jwt);
        service.logout(jti, username);

        String origin = httpRequest.getHeader("Origin");
        boolean isHttps = origin != null && origin.startsWith("https");

        ResponseCookie cookie = ResponseCookie.from("CashFlow-EMS-JWT", "")
                .maxAge(0)
                .httpOnly(isHttps)  // نفس إعدادات الكوكي الأصلي
                .secure(true)    // إذا كنت تستخدم HTTPS
                .path("/")       // نفس المسار الذي أنشأت به الكوكي
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LogoutResponse.builder()
                        .message("Logged out successfully")
                        .build()
                );
    }

    @PostMapping("/logout-all") // Test success
    public ResponseEntity<?> logoutAll(
            HttpServletResponse response,
            HttpServletRequest httpRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt
    ) {
        String username = jwtService.extractUsername(jwt);
        service.logoutAll(username);

        String origin = httpRequest.getHeader("Origin");
        boolean isHttps = origin != null && origin.startsWith("https");

        ResponseCookie cookie = ResponseCookie.from("CashFlow-EMS-JWT", "")
                .maxAge(0)
                .httpOnly(true)  // نفس إعدادات الكوكي الأصلي
                .secure(isHttps)    // إذا كنت تستخدم HTTPS
                .path("/")       // نفس المسار الذي أنشأت به الكوكي
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LogoutResponse.builder()
                        .message("Logged out successfully from all devices")
                        .build()
                );
    }

}
