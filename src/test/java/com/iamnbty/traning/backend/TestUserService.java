package com.iamnbty.traning.backend;

import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserService {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void testCreate() throws BaseException {
        User user = userService.create(
                TestCreateData.email,
                TestCreateData.password,
                TestCreateData.name
        );

        // check not null
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());

        // check equals
        Assertions.assertEquals(TestCreateData.email, user.getEmail());

        boolean isMatched = userService.matchPassword(TestCreateData.password, user.getPassword());
        Assertions.assertTrue(isMatched);

        Assertions.assertEquals(TestCreateData.name, user.getName());
    }

    @Test
    @Order(2)
    void testUpdate() throws BaseException { // อาจต้อง throws BaseException ถ้า updateName มีการโยน Error
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        // อัปเดตชื่อ
        User updatedUser = userService.updateName(user.getId(), TestUpdateData.name);

        Assertions.assertNotNull(updatedUser);
        // ตรวจสอบว่าชื่อเปลี่ยนเป็นชื่อใหม่แล้ว
        Assertions.assertEquals(TestUpdateData.name, updatedUser.getName());
    }

    @Test
    @Order(3)
    void testDelete() {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        userService.deleteById(user.getId());

        // ลองหาอีกครั้ง ต้องไม่เจอแล้ว
        Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(optDelete.isEmpty());
    }

    // --- Data Interfaces ---

    interface TestCreateData {
        String email = "Bingo@gmail.com";
        String password = "12345";
        String name = "Bingo";
    }

    interface TestUpdateData {
        // แก้ไข: เปลี่ยนชื่อให้ต่างจากตอนสร้าง จะได้รู้ว่าอัปเดตสำเร็จจริง
        String name = "Bingo Updated";
    }

}