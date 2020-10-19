package com.util;

import com.util.annotations.Decode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class SecurityUtil {

    @Value("${decodeSplit}")
    private String split;

    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "anonymous";
        }

        return ((User) authentication.getPrincipal()).getUsername();
    }

    public <T> T decode(T decodeObj) {
        Annotation annotation = decodeObj.getClass().getAnnotation(Decode.class);
        if (annotation != null) {
            for (String a : ((Decode) annotation).methods()) {
                String[] arr = a.split("-");
                String method = arr[0];
                String fieldName = arr[1];
                Field field = ReflectionUtils.findField(decodeObj.getClass(), fieldName);
                try {
                    Method getMethod = decodeObj.getClass().getDeclaredMethod("get" + method);
                    Object o = getMethod.invoke(decodeObj);
                    byte[] decode = Base64Utils.decode(Base64Utils.decodeFromString((String) o));
                    String decodeStr = new String(decode).split(split)[0];
                    Method setMethod = decodeObj.getClass().getDeclaredMethod("set" + method, field.getType());
                    setMethod.invoke(decodeObj, decodeStr);
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return decodeObj;
    }

    public String decode(String param) {
        byte[] decode = Base64Utils.decode(Base64Utils.decodeFromString(param));
        return new String(decode).split(split)[0];
    }

    public String encode(String param) {
        return Base64Utils.encodeToString(param.getBytes());
    }
}
