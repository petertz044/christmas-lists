package com.zullo.christmas.model.api;

import com.zullo.christmas.model.database.ListEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateListRequest {
    ListEntity list;
    Integer groupId;
}
