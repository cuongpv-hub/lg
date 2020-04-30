package vn.vsd.agro.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private PasswordEncoder encoder;

    private static PasswordUtils encoderUtils;

    public PasswordUtils() {
    }

    @PostConstruct
    void afterInit() {
        if (encoderUtils == null) {
            encoderUtils = this;
        }
    }

    protected PasswordEncoder getEncoder() {
        return encoder;
    }

    public static String encode(String key) {
        String value = encoderUtils.getEncoder().encode(key);
        return value;
    }
    
    public static String getDefaultPassword() {
        return encode(DEFAULT_PASSWORD);
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean isMactch = encoderUtils.getEncoder().matches(rawPassword, encodedPassword);
        return isMactch;
    }

}
