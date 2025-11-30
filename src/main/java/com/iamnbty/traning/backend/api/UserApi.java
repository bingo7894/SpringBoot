package com.iamnbty.traning.backend.api;

import com.iamnbty.traning.backend.business.UserBusiness;
import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.model.MLoginRequest;
import com.iamnbty.traning.backend.model.MRegisterRequest;
import com.iamnbty.traning.backend.model.MRegisterResponse;
import com.iamnbty.traning.backend.model.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserApi {

    //Method : 1 Field Injection
//    @Autowired
//    private TestBusiness business;

    //Method : 2 Construtor Injection
    private final UserBusiness business;

    public UserApi(UserBusiness business) {
        this.business = business;
    }

    @PostMapping
    @RequestMapping("/login")
    public ResponseEntity<String> login(@RequestBody MLoginRequest request) throws BaseException {
        String response = business.login(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = business.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file)throws BaseException{
        String response = business.uploadProfilePicture(file);
        return ResponseEntity.ok(response);
    }

}
