package com.schedule.dto.account;

import com.schedule.domain.account.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserInfoSaveRequestDto {

    @NotBlank(message = "USERNAME_IS_MANDATORY")
    private String username;
    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;
    @NotBlank(message = "ROLE_IS_MANDATORY")
    private String role;

    @Builder
    public UserInfoSaveRequestDto(String username,
                                  String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password))
                .role(role)
                .build();
    }
}
