package br.com.github.wirecardBrasil.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UpdateCardCreditException extends WirecardException{
    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Update card credit cannot be alowed.");

        return pb;
    }
}
