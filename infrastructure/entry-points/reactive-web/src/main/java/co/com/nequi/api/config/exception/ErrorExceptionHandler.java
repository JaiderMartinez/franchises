package co.com.nequi.api.config.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.nequi.model.exception.FranchiseException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Slf4j
@Order(-2)
@Component
public class ErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ErrorExceptionHandler(ErrorAttributes errorAttributes,
                                 WebProperties resources,
                                 ApplicationContext applicationContext,
                                 ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, resources.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::buildErrorResponse);
    }

    private Mono<ServerResponse> buildErrorResponse(ServerRequest serverRequest) {
        return Mono.just(serverRequest)
                .map(this::getError)
                .doOnNext(error -> log.error("Error handling: {}", error.getClass().getName(), error))
                .flatMap(Mono::error)
                .onErrorResume(FranchiseException.class, this::buildResponseBody)
                .onErrorResume(this::buildResponseBody)
                .cast(Tuple2.class)
                .flatMap(tuple -> this.buildResponse((ErrorDto) tuple.getT1(), (HttpStatus) tuple.getT2()));
    }

    private Mono<Tuple2<ErrorDto, HttpStatus>> buildResponseBody(FranchiseException franchiseException) {
        return Mono.just(franchiseException.getErrorCode())
                .map(errorCode -> new ErrorDto(errorCode.getCode(), errorCode.getMessage()))
                .zipWith(Mono.just(HttpStatus.valueOf(franchiseException.getErrorCode().getStatusCode())));
    }

    private Mono<Tuple2<ErrorDto, HttpStatus>> buildResponseBody(Throwable throwable) {
        return Mono.just(new ErrorDto(HttpStatus.EXPECTATION_FAILED.name(), throwable.getMessage()))
                .zipWith(Mono.just(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public Mono<ServerResponse> buildResponse(ErrorDto body, HttpStatus httpStatus) {
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(body), ErrorDto.class);
    }

}
