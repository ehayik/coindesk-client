package com.github.ehayik.coindesk.adapter.shell;

import org.jline.terminal.Terminal;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@SuppressWarnings("unused")
@ConfigurationPropertiesScan
class SpringShellConfig {

    @Bean
    public ShellHelper shellHelper(@Lazy Terminal terminal, ShellColorProperties colorProperties) {
        return new ShellHelper(terminal, colorProperties);
    }
}
