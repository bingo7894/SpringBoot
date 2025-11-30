package com.iamnbty.traning.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration // บอก Spring ว่าคลาสนี้คือไฟล์ตั้งค่า (Config)
@EnableWebSecurity // เปิดใช้งานระบบความปลอดภัย Web Security
public class SecurityConfig {

    // ตัวช่วยสำหรับส่ง Error ไปให้ @ControllerAdvice (ErrorAdvisor) จัดการ
    private final HandlerExceptionResolver exceptionResolver;

    // Inject 'handlerExceptionResolver' เข้ามาเพื่อใช้ส่ง Error 401/403 ข้ามไปที่ ControllerAdvice
    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    // ----------------------------------------------------------------------
    // 1. Password Encoder
    // ----------------------------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        // ใช้ BCrypt เป็นมาตรฐานสากลในการเข้ารหัสรหัสผ่าน (ปลอดภัยสูง)
        return new BCryptPasswordEncoder();
    }

    // ----------------------------------------------------------------------
    // 2. Authentication Manager
    // ----------------------------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // ดึงตัวจัดการการยืนยันตัวตนออกมา เพื่อเอาไปใช้ใน Controller (เช่น ตอนทำ Login API)
        return authConfig.getAuthenticationManager();
    }

    // ----------------------------------------------------------------------
    // 3. Security Filter Chain (หัวใจหลักของการตั้งค่า)
    // ----------------------------------------------------------------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // รวบรวม URL ที่ไม่ต้อง Login ไว้ที่เดียว (จะได้แก้ใขง่าย)
        final String[] PUBLIC_URLS = {
                "/user/register",
                "/user/login",
                "/actuator/**" // (เผื่อไว้) สำหรับ Health Check
        };

        http
                // ปิด CORS (Cross-Origin) และ CSRF (Cross-Site Request Forgery)
                // เพราะเราทำ REST API ที่ Stateless (ไม่ได้ใช้ Browser Session แบบเว็บทั่วไป) จึงไม่จำเป็นต้องเปิด
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)

                // กำหนดรูปแบบ Session เป็น Stateless
                // หมายความว่า Server จะไม่จำ User (ไม่สร้าง Session ID)
                // ทุก Request ต้องส่ง Token มายืนยันตัวตนเอง (เหมาะกับ JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // กำหนดกฎการเข้าถึง URL (Firewall Rules)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll() // อนุญาต URL ในตัวแปรข้างบนให้เข้าได้เลย
                        .anyRequest().authenticated()             // URL อื่นๆ นอกจากนั้น "ต้อง Login" เท่านั้น
                )

                // การจัดการ Error (เมื่อ User เข้าไม่ได้)
                .exceptionHandling(ex -> ex
                        // กรณี 401 (ยังไม่ Login หรือ Token ผิด) -> ส่งไปให้ ErrorAdvisor
                        .authenticationEntryPoint((req, res, e) -> exceptionResolver.resolveException(req, res, null, e))
                        // กรณี 403 (Login แล้ว แต่ไม่มีสิทธิ์เข้า) -> ส่งไปให้ ErrorAdvisor
                        .accessDeniedHandler((req, res, e) -> exceptionResolver.resolveException(req, res, null, e))
                );

        return http.build();
    }
}