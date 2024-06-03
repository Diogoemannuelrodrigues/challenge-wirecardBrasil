package com.github.wirecard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BuyerNotFoundException extends WirecardException{
    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("This buyer not found.");

        return pb;
    }
}
