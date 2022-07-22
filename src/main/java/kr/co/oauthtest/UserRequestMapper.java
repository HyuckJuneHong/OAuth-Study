package kr.co.oauthtest;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public UserDto getUser(OAuth2User oAuth2User) {

        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .build();
    }

//    public UserFindRequest findByUserDto(UserDto userDto){
//        return new UserFindRequest(userDto.getEmail());
//    }
}