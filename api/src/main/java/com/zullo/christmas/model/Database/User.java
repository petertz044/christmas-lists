package com.zullo.christmas.model.Database;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    Integer id;
    String username;
    String password;
    String role;
    LocalDateTime dt_crtd;
    LocalDateTime dt_last_login;
    LocalDateTime dt_last_modified;
    String user_id_last_modified;
}
