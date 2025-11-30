package com.iamnbty.traning.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    // ลบ strategy = GenerationType.IDENTITY ออก
    @GeneratedValue(generator = "uuid2")
    @Column(length = 36,nullable = false,updatable = false)
    private String id;
}