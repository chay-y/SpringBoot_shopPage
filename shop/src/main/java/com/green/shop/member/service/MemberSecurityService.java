package com.green.shop.member.service;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor    //final 멤버의 생성자를 자동으로 작성
                            //UserDetailsService : 사용자 이름(username)을 사용하여 사용자의 정보를 읽고 결과값을 리턴
public class MemberSecurityService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //사용자의 이름으로 회원 정보를 조회한 후 결과를 반환
        MemberDto member = memberMapper.loginMember(username);

        //회원 정보가 없으면 예외 발생
        if(member == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        //Role에 따라 권한을 부여하도록 함
        //사용자의 권한을 나타내기 위해 list 생성
        List<GrantedAuthority> authorities = new ArrayList<>();

        //SimpleGrantedAuthority : 사용자의 권한을 Security에서 사용하는 형태로 변환
        if("ADMIN".equals(member.getRole().toString())){    //문자열을 equls 앞에 써주면 오류 덜남
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.toString()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        }

        System.out.println(member.getPassword());

        //Spring Security의  User 객체를 생성하여 반환
        return User.builder()   //User객체 생성
                .username(member.getId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
