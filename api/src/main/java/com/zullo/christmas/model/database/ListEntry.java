package com.zullo.christmas.model.database;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListEntry {
    Integer id;
    String url;
    String title;
    String description;
    Character priority;
    Integer listId;
    Integer userIdOwner;
    Boolean isPurchased;
    Integer userIdPurchased;
    Boolean isActive;
    LocalDateTime dtCrtd;
    LocalDateTime dtLastModified;
    Integer userIdLastModified;
}
