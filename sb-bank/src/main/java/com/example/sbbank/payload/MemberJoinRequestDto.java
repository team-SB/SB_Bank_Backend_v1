package com.example.sbbank.payload;

import com.example.sbbank.entity.Authority;
import com.example.sbbank.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDto {
    private String name;
    private String number;
    private String username;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .number(number)
                .username(username)
                .password(password)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
