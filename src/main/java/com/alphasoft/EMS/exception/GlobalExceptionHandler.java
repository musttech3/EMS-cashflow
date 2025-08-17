package com.alphasoft.EMS.exception;


import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                "RUN_TIME_EXCEPTION",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
                "ACCESS_DENIED",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "USER_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserNotEnabledException.class})
    public ResponseEntity<ErrorResponse> handleUsernameNotEnabled(UserNotEnabledException ex) {
        ErrorResponse error = new ErrorResponse(
                "USER_NOT_ENABLED",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UsernameAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                "USER_ALREADY_USED",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FamilyNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleFamilyNotFound(FamilyNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "FAMILY_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RelationshipException.class})
    public ResponseEntity<ErrorResponse> handleFamilyNotFound(RelationshipException ex) {
        ErrorResponse error = new ErrorResponse(
                "RELATIONSHIP_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentials() {
        ErrorResponse error = new ErrorResponse(
                "INVALID_CREDENTIALS",
                "Username or password is incorrect",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {
        ErrorResponse error = new ErrorResponse(
                "TOKEN_EXPIRED",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InvalidJwtException.class})
    public ResponseEntity<ErrorResponse> handleInvalidJwt(InvalidJwtException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_JWT",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<ErrorResponse> handleSignature() {
        ErrorResponse error = new ErrorResponse(
                "SIGNATURE_EXCEPTION",
                "Invalid signature",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {JsonParseException.class})
    public ResponseEntity<ErrorResponse> handleJsonParseException() {
        ErrorResponse error = new ErrorResponse(
                "JSON_PARSE_EXCEPTION",
                "Invalid JWT",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {MissingRequestCookieException.class})
    public ResponseEntity<ErrorResponse> handleMissingCookie() {
        ErrorResponse error = new ErrorResponse(
                "NO COOKIE FOUND",
                "Sign in or Login before",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgument() {
        ErrorResponse error = new ErrorResponse(
                "ILLEGAL_ARGUMENT",
                "Missed or invalid parameters",
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // New exception handlers for family invitations
    @ExceptionHandler(value = {InvitationNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleInvitationNotFound(InvitationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVITATION_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvitationExpiredException.class})
    public ResponseEntity<ErrorResponse> handleInvitationExpired(InvitationExpiredException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVITATION_EXPIRED",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.GONE);
    }

    @ExceptionHandler(value = {InvitationAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleInvitationAlreadyExists(InvitationAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVITATION_ALREADY_EXISTS",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotFamilyAdminException.class})
    public ResponseEntity<ErrorResponse> handleNotFamilyAdmin(NotFamilyAdminException ex) {
        ErrorResponse error = new ErrorResponse(
                "NOT_FAMILY_ADMIN",
                ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

}
