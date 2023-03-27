package com.github.ehayik.coindesk.shell;

import java.time.Duration;
import lombok.NonNull;
import org.springframework.cloud.skipper.support.DurationUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
class StringToDurationConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(@NonNull String expression) {
        return DurationUtils.convert(expression);
    }
}
