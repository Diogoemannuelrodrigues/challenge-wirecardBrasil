package com.github.wirecard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TypePaymentEnumException extends WirecardException{
    public TypePaymentEnumException(Integer cod) {
       toProblemDetail(cod);
    }

    public ProblemDetail toProblemDetail(Integer id) {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("This id is invalid."+ id);

        return pb;
    }
}
