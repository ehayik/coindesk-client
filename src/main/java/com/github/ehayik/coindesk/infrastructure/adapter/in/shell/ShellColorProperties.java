package com.github.ehayik.coindesk.infrastructure.adapter.in.shell;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("unused")
@ConfigurationProperties("shell.out")
public record ShellColorProperties(PromptColor info, PromptColor success, PromptColor warning, PromptColor error) {}
