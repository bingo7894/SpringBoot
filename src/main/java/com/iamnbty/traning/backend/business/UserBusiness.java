package com.iamnbty.traning.backend.business;

import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.exception.FileException;
import com.iamnbty.traning.backend.exception.UserException;
import com.iamnbty.traning.backend.mapper.UserMapper;
import com.iamnbty.traning.backend.model.MLoginRequest;
import com.iamnbty.traning.backend.model.MRegisterRequest;
import com.iamnbty.traning.backend.model.MRegisterResponse;
import com.iamnbty.traning.backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserBusiness {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserBusiness(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public String login(MLoginRequest request) throws BaseException {
        //validate request

        //verift database
        Optional<User> opt = userService.findByEmail(request.getEmail());
        if(opt.isEmpty()){
            //throw error login fail not found
            throw UserException.loginFailEmailNotFound();

        }
        User user = opt.get();
        if(!userService.matchPassword(request.getPassword(),user.getPassword())){
            //throw error login fail password invalid
            throw UserException.loginFailPasswordIncorrect();
        }

        //Todo: generate
        String token ="JWT TO DO";
        return token;

    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName());

        //Todo: mapper
        MRegisterResponse model = userMapper.toRegisterResponse(user);

        return model;

    }

    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        //validate file
        if (file == null) {
            throw FileException.fileNull();
        }

        //validate size (2MB)
        if (file.getSize() > 1048576 * 2) {
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw FileException.unsupported();
        }

        List<String> suportedTypes = Arrays.asList("image/jpeg", "image/png");

        // แก้ไขตรรกะ: โยน Error เมื่อ Content Type ไม่อยู่ในรายการที่รองรับ
        if (!suportedTypes.contains(contentType)) {
            throw FileException.unsupported();
        }

        // Todo: upload file File Storage (Aws s3,etc...)
        try {
            byte[] bytes = file.getBytes();
            // ในทางปฏิบัติ: บันทึก bytes ลงใน S3 หรือ Local Disk

        } catch (IOException e) {
            // โยน Exception ที่เหมาะสมกว่า RuntimeException เช่น FileException.uploadFailed()
            throw new RuntimeException("Cannot read file bytes: " + e.getMessage(), e);
        }

        // ต้องมีคำสั่ง return เมื่อเมธอดทำงานสำเร็จ
        return "Profile picture uploaded: " + file.getOriginalFilename();
    }
}