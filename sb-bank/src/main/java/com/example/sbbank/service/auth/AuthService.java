package com.example.sbbank.service.auth;

import com.example.sbbank.entity.Authority;
import com.example.sbbank.entity.member.Member;
import com.example.sbbank.entity.member.MemberRepository;
import com.example.sbbank.exception.InvalidPasswordException;
import com.example.sbbank.exception.UserAlreadyExistsException;
import com.example.sbbank.exception.UserNotFoundException;
import com.example.sbbank.payload.request.MemberSecLoginRequestDto;
import com.example.sbbank.payload.response.TokenResponseDto;
import com.example.sbbank.security.jwt.JwtTokenProvider;
import com.example.sbbank.payload.request.MemberLoginRequestDto;
import com.example.sbbank.payload.request.MemberJoinRequestDto;
import com.example.sbbank.payload.response.AccessTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public String join(MemberJoinRequestDto request) {
        if(memberRepository.existsByNameOrUsername(request.getName(), request.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        memberRepository.save(
                Member.builder()
                        .name(request.getName())
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .secPassword(passwordEncoder.encode(request.getSecPassword()))
                        .authority(Authority.ROLE_USER)
                        .build());

        return "success join";
    }

    public AccessTokenResponseDto login(MemberLoginRequestDto request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        return tokenProvider.createAccessToken(member.getUsername(), member.getAuthority());
    }

    public TokenResponseDto secLogin(MemberSecLoginRequestDto request, Member member) {
        if(!passwordEncoder.matches(request.getSecPassword(), member.getSecPassword())) {
            throw new InvalidPasswordException();
        }

        return tokenProvider.createToken(member.getUsername());
    }

}
