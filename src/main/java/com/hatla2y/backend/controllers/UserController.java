package com.hatla2y.backend.controllers;

import com.hatla2y.backend.dtos.user.UserInfo;
import com.hatla2y.backend.dtos.user.UserPrivateInfo;
import com.hatla2y.backend.dtos.user.UserPublicInfo;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        ResponseEntity<User> response = ResponseEntity.ok(user);
        System.out.println(response.getBody());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/address")
    public ResponseEntity<UserPrivateInfo> setUserAddress(@RequestBody UserInfo.Address address,
                                                          @AuthenticationPrincipal User user) {
        UserPrivateInfo info = userService.setUserAddress(address, user);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @PostMapping("/phone")
    public ResponseEntity<UserPrivateInfo> setUserPhoneNumber(@RequestParam("phone") String phoneNumber,
                                                              @AuthenticationPrincipal User user) {
        userService.setUserPhoneNumber(phoneNumber, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/phone/verify")
    public ResponseEntity<UserPrivateInfo> verifyUserPhoneNumber(@RequestParam("otp") String otp,
                                                                 @AuthenticationPrincipal User user) {
        userService.verifyPhoneNumber(otp, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pub/{id}")
    public ResponseEntity<UserPublicInfo> getPublicInfo(@PathVariable Integer id,
                                                        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getPublicInfo(id, user));
    }
}
