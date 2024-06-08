package com.github.wirecard.config.utils;

import com.github.wirecard.exceptions.CpfNotValidException;
import org.springframework.stereotype.Component;

@Component
public class CpfUtils {
    public static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new CpfNotValidException();
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }
}
