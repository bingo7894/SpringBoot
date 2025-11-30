package com.iamnbty.traning.backend;

import com.iamnbty.traning.backend.entity.Address;
import com.iamnbty.traning.backend.entity.Social;
import com.iamnbty.traning.backend.entity.User;
import com.iamnbty.traning.backend.exception.BaseException;
import com.iamnbty.traning.backend.service.AddressService;
import com.iamnbty.traning.backend.service.SocialService;
import com.iamnbty.traning.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private SocialService socialService;

    @Autowired
    private AddressService addressService;

    // ----------------------------------------------------------------
    // 1. Test Create User
    // ----------------------------------------------------------------
    @Test
    @Order(1)
    void testCreate() throws BaseException {
        User user = userService.create(
                TestCreateData.email,
                TestCreateData.password,
                TestCreateData.name
        );

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(TestCreateData.email, user.getEmail());
        Assertions.assertTrue(userService.matchPassword(TestCreateData.password, user.getPassword()));
        Assertions.assertEquals(TestCreateData.name, user.getName());
    }

    // ----------------------------------------------------------------
    // 2. Test Update User
    // ----------------------------------------------------------------
    @Test
    @Order(2)
    void testUpdate() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        User updatedUser = userService.updateName(user.getId(), TestUpdateData.name);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(TestUpdateData.name, updatedUser.getName());
    }

    // ----------------------------------------------------------------
    // 3. Test Create Social
    // ----------------------------------------------------------------
    @Test
    @Order(3)
    void testCreateSocial() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());
        User user = opt.get();

        Social social = user.getSocial();
        Assertions.assertNull(social);

        social = socialService.create(
                user,
                SocialTestData.facebook,
                SocialTestData.line,
                SocialTestData.instagram,
                SocialTestData.tiktok
        );

        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestData.facebook, social.getFacebook());
        Assertions.assertEquals(SocialTestData.line, social.getLine());
        Assertions.assertEquals(SocialTestData.instagram, social.getInstagram());
        Assertions.assertEquals(SocialTestData.tiktok, social.getTiktok());
        Assertions.assertEquals(user.getId(), social.getUser().getId());
    }

    // ----------------------------------------------------------------
    // 4. Test Create Address (One-to-Many)
    // ----------------------------------------------------------------
    @Test
    @Order(4)
    void testCreateAddress() throws BaseException { // ต้อง throws BaseException
        // 1. หา User
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());
        User user = opt.get();

        // 2. เช็คก่อนสร้าง
        List<Address> addresses = user.getAddresses();
        Assertions.assertTrue(addresses == null || addresses.isEmpty());

        // 3. สร้าง 2 ที่อยู่ (เรียกใช้ Helper Method)
        createAddress(user, AddressTestCreateData.line1, AddressTestCreateData.line2, AddressTestCreateData.zipcode);
        createAddress(user, AddressTestCreateData2.line1, AddressTestCreateData2.line2, AddressTestCreateData2.zipcode);
    }

    // Helper Method สำหรับสร้าง Address (ช่วยลดโค้ดซ้ำ)
    private void createAddress(User user, String line1, String line2, String zipcode) throws BaseException {
        Address address = addressService.create(user, line1, line2, zipcode);

        Assertions.assertNotNull(address);
        Assertions.assertEquals(line1, address.getLine1());
        Assertions.assertEquals(line2, address.getLine2());
        Assertions.assertEquals(zipcode, address.getZipcode());
        Assertions.assertEquals(user.getId(), address.getUser().getId());
    }

    // ----------------------------------------------------------------
    // 9. Test Delete User
    // ----------------------------------------------------------------
    @Test
    @Order(9)
    void testDelete() {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        // 1. Check Social (ก่อนลบต้องมี)
        Social social = user.getSocial();
        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestData.facebook, social.getFacebook());

        // 2. Check Address (ก่อนลบต้องมี)
        List<Address> addresses = user.getAddresses();
        Assertions.assertFalse(addresses.isEmpty());
        Assertions.assertEquals(2, addresses.size()); // เราสร้างไว้ 2 ที่อยู่ ต้องมี 2

        // 3. สั่งลบ
        userService.deleteById(user.getId());

        // 4. Check User (ต้องหายไป)
        Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(optDelete.isEmpty());

        // 5. (Optional) Check Cascade:
        // ถ้าคุณมี socialService.findById() หรือ addressService.findById()
        // คุณสามารถเช็คตรงนี้เพิ่มได้ว่า Social/Address ก็ต้องหาไม่เจอแล้วเช่นกัน
        // แต่อย่างน้อยเช็คข้อ 4 ก็ถือว่า User ถูกลบแล้วครับ
    }

    // --- Data Interfaces ---

    interface TestCreateData {
        String email = "Bingo@gmail.com";
        String password = "12345";
        String name = "Bingo";
    }

    interface AddressTestCreateData {
        String line1 = "123/456";
        String line2 = "Bangkok";
        String zipcode = "123444";
    }

    interface AddressTestCreateData2 {
        String line1 = "123/45622";
        String line2 = "Bangkok22";
        String zipcode = "12344422";
    }

    interface TestUpdateData {
        String name = "Bingo Updated";
    }

    interface SocialTestData {
        String facebook = "Bingo Facebook";
        String line = "Bingo555";
        String instagram = "ig_bingo";
        String tiktok = "MeawTiktok";
    }
}