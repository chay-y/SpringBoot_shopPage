package com.green.shop.member.form;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.w3c.dom.Text;

@Data
public class MemberJoinForm {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String id;

    @Length(min=4, max=16, message = "비밀번호는 8자이상, 16자 이하로 입력해주세요.")
    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
                //  (?=.*[a-z]) : 하나이상의 영어 소문자, (?=.*//d):하나 이상의 숫자
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,16}$", message = "비밀번호는 영문 대문자, 소문자, 숫자를 각각 1개 이상 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Email(message = "이메일 형식으로 입력해주세요.") /*이메일형식 확인가능*/
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotEmpty(message = "주소는 필수 입력값입니다.")
    private String address;







}
