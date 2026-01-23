package org.team2.roktokhoj_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LatitudeValidator.class)
@interface LatitudeValidation {
    String message() default "The latitude value must be between -90 and 90.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LongitudeValidator.class)
@interface LongitudeValidation {
    String message() default "The longitude value must be between -180 and 180.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class LatitudeValidator implements ConstraintValidator<LatitudeValidation, Double> {
    @Override
    public void initialize(LatitudeValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value >= -90.0 && value <= 90.0;
    }
}

class LongitudeValidator implements ConstraintValidator<LongitudeValidation, Double> {
    @Override
    public void initialize(LongitudeValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value >= -180.0 && value <= 180.0;
    }
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AddressSelection {
    @JsonProperty("lat")
    @LatitudeValidation
    private Double lat;

    @JsonProperty("lon")
    @LongitudeValidation
    private Double lon;

    @JsonProperty("radius")
    @NotNull(message = "Radius cannot be null")
    @DecimalMin(value = "500.0", message = "Radius must be at least 500.0")
    @DecimalMax(value = "50000.0", message = "Radius cannot exceed 50000.0")
    private Double radius;
}
