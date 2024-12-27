package com.green.shop.member.dto;

import com.green.shop.member.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long memberId;
    private String id;
    private String password; // 시큐리티 이용할 땐 반드시 변수명에 password여야 함.
    private String name;
    private String email;
    private String address;
    private Role role;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
