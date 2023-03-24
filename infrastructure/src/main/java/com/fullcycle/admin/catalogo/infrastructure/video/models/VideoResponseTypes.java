package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes({
    @JsonSubTypes.Type(value = VideoEncoderCompleted.class, name = "COMPLETED"),
    @JsonSubTypes.Type(value = VideoEncoderError.class, name = "ERROR")
})
public @interface VideoResponseTypes {

}
