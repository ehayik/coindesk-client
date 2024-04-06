package com.github.ehayik.coindesk.adapter.in.shell;

import static org.jline.utils.AttributedStyle.DEFAULT;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;

@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ShellHelper {

    private final Terminal terminal;
    private final ShellColorProperties shellColors;

    /**
     * Print message to the console in the default color.
     *
     * @param message message to print
     */
    public void print(String message) {
        print(message, null);
    }

    /**
     * Generic Print to the console method.
     *
     * @param message message to print
     * @param color   (optional) prompt color
     */
    public void print(String message, PromptColor color) {
        String toPrint = message;
        if (color != null) {
            toPrint = getColored(message, color);
        }
        terminal.writer().println(toPrint);
        terminal.flush();
    }

    private String getColored(String message, @NonNull PromptColor color) {
        return (new AttributedStringBuilder())
                .append(message, DEFAULT.foreground(color.toJlineAttributedStyle()))
                .toAnsi();
    }

    /**
     * Print message to the console in the success color.
     *
     * @param message message to print
     */
    public void printSuccess(String message) {
        print(message, shellColors.success());
    }

    /**
     * Print message to the console in the info color.
     *
     * @param message message to print
     */
    public void printInfo(String message) {
        print(message, shellColors.info());
    }

    /**
     * Print message to the console in the warning color.
     *
     * @param message message to print
     */
    public void printWarning(String message) {
        print(message, shellColors.warning());
    }

    /**
     * Print message to the console in the error color.
     *
     * @param message message to print
     */
    public void printError(String message) {
        print(message, shellColors.error());
    }
}
