package com.oidc.zunsi.domain.social.naver;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NaverProfile {
    private String id;
    private String email;
    private String name;
    private String nickname;
    private String profile_image;
    private String gender;
    private String birthday;
    private String birthyear;
    private String age;
    private String mobile;
    private String mobile_e164;
}


/*
{
  "resultcode": "00",
  "message": "success",
  "response": {
      id=gXnnSgq4_hf7bZ6GiV2J4AcN7i6KBNuAkkdWQRMKqIY,
      nickname=byunghak,
      profile_image=https://phinf.pstatic.net/contact/20200316_81/1584369634650JF0NR_JPEG/KakaoTalk_20200316_233532017.jpg,
      age=20-29,
      gender=M,
      email=qkwl4678@gmail.com,
      mobile=010-9931-7685,
      mobile_e164=+821099317685,
      name=고병학,
      birthday=03-18,
      birthyear=1996
  }
}
 */