package com.github.ehayik.coindesk.adapter.shell;

import static org.jline.utils.AttributedStyle.BLUE;
import static org.jline.utils.AttributedStyle.DEFAULT;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
class CoinDeskPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("coindesk:>", DEFAULT.foreground(BLUE));
    }
}
