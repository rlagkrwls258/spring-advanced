package org.example.expert.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }

    public void validEqualPassword(String rawPassword, String encodedPassword) {
        if (!matches(rawPassword, encodedPassword)) {
            throw new AuthException("잘못된 비밀번호입니다.");
        }
    }

    public void validNewPassword(String rawPassword, String encodedPassword) {
        if (matches(rawPassword, encodedPassword)) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }
    }

}
