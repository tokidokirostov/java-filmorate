package ru.yandex.practicum.filmorate.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmDateReleaseValidator implements ConstraintValidator<FilmDateRelease, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return (!localDate.isBefore(LocalDate.of(1895, 12, 28)));
    }
}
