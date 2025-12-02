package com.zullo.christmas.model.database;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    Integer id;
    String title;
    String description;
    Integer userIdOwner;
    Boolean isActive;
    LocalDateTime dtCrtd;
    LocalDateTime dtLastModified;
    Integer userIdLastModified;
}
