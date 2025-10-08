package com.zullo.christmas.model.Database;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Integer id;
    String username;
    String password;
    Character role;
    LocalDateTime dt_crtd;
    LocalDateTime dt_last_login;
    LocalDateTime dt_last_modified;
    String user_id_last_modified;
}
