package com.iamnbty.traning.backend.model;

import lombok.Data;

@Data
public class MRegisterResponse {
    private String email;
    private String name;
    // ไม่ควรส่ง password กลับไป
}