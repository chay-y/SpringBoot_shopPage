package com.green.shop.member.service;

import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;


    @Autowired
    private PasswordEncoder passwordEncoder; // 주입한 인코더를 가져다쓰겠다.


    public int insertMember(MemberDto memberDto){

        this.overlapId(memberDto.getId());
        this.overlapEmail(memberDto.getEmail());


        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(memberDto.getPassword());

        memberDto.setPassword(encodedPassword);

        return memberMapper.insertMember(memberDto);
    }

    public void overlapId(String id){
        MemberDto findId = memberMapper.overlapId(id);

        if(findId != null){
            // IllegalStateException : 대상 객체의 상태가 호출된 메서드를 수행하기에
            // 적절하지 않을 때 발생시키는 예외
            throw new IllegalStateException("중복된 아이디입니다.");
        }
    }

    public void overlapEmail(String email){
        MemberDto findEmail = memberMapper.overlapEmail(email);

        if(findEmail != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }

    public Long findMemberId(String id){
        return memberMapper.findMemberId(id);
    }





}
