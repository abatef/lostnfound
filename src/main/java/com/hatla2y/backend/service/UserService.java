package com.hatla2y.backend.service;

import com.hatla2y.backend.dtos.user.UserInfo;
import com.hatla2y.backend.dtos.user.UserPrivateInfo;
import com.hatla2y.backend.dtos.user.UserPublicInfo;
import com.hatla2y.backend.exceptions.InvalidOtpException;
import com.hatla2y.backend.exceptions.InvalidPhoneNumberException;
import com.hatla2y.backend.exceptions.InvalidUserIdException;
import com.hatla2y.backend.models.User;
import com.hatla2y.backend.repositories.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String OTP_KEY_PREFIX = "UOTP:";


    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public UserPrivateInfo setUserAddress(@Valid UserInfo.Address request, User user) {
        User updatedUser = modelMapper.map(request, User.class);
        updatedUser.setEmail(user.getEmail());
        updatedUser.setId(user.getId());
        userRepository.save(updatedUser);
        return modelMapper.map(updatedUser, UserPrivateInfo.class);
    }

    private static final SecureRandom secureRandom = new SecureRandom();
    private static Integer generateRandomNumber() {
        return secureRandom.nextInt(111111, 999999);
    }

    private void generatePhoneOTP(User user, String phoneNumber) {
        Integer OTP = generateRandomNumber();
        redisTemplate.opsForValue()
                .set(OTP_KEY_PREFIX + user.getId(), OTP + ":" + phoneNumber, Duration.ofMinutes(5));
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return true;
    }

    @Transactional
    public void setUserPhoneNumber(@Valid String phoneNumber, User user) {
        if (!validatePhoneNumber(phoneNumber)) {
            throw new InvalidPhoneNumberException(phoneNumber);
        }
        generatePhoneOTP(user, phoneNumber);
    }

    @Transactional
    public void verifyPhoneNumber(@Valid String otp, User user) {
        // verify
        String fullOtp = redisTemplate.opsForValue().get(OTP_KEY_PREFIX + user.getId());
        logger.info(fullOtp);
        if (fullOtp == null || fullOtp.isBlank()) {
            throw new InvalidOtpException("Empty OTP");
        }
        String[] parts = fullOtp.split(":");
        logger.info(parts[0], parts[1]);
        if (!parts[0].equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
        user.setPhoneNumber(parts[1]);
        userRepository.save(user);
        redisTemplate.delete(OTP_KEY_PREFIX + user.getId());
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException(id.toString()));
    }

    public UserPrivateInfo getPrivateInfo(User user) {
        return modelMapper.map(user, UserPrivateInfo.class);
    }

    public UserPublicInfo getPublicInfo(Integer publicId, User user) {
        UserPublicInfo publicInfo = new UserPublicInfo();
        String fullName = getUserById(publicId).getFullName();
        publicInfo.setFullName(fullName);
        publicInfo.setId(publicId);
        return publicInfo;
    }
}
