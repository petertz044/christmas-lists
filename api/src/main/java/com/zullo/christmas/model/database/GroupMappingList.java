package com.zullo.christmas.model.database;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMappingList {
    Integer id;
    Integer groupId;
    Integer listId;
    Boolean isActive;
    LocalDateTime dtCrtd;
    LocalDateTime dtLastModified;
    Integer userIdLastModified;
}
