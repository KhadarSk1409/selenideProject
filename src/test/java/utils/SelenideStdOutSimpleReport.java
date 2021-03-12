package utils;

import com.codeborne.selenide.logevents.EventsCollector;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.base.Joiner;
import com.vo.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.OptionalInt;

public class SelenideStdOutSimpleReport {
    private static final Logger log = LoggerFactory.getLogger(SelenideStdOutSimpleReport.class);

    private static String LISTENER_ID = "selenideStdOutSimpleReport";
    public void start() {
        SelenideLogger.addListener(LISTENER_ID + "_" + BaseTest.BROWSER_CONFIG.get(), new EventsCollector());
    }

    public void finish(String title) {
        EventsCollector logEventListener = SelenideLogger.removeListener(LISTENER_ID + "_" + BaseTest.BROWSER_CONFIG.get());

        if (logEventListener == null) {
            log.warn("Can not publish report because Selenide logger has not started.");
            return;
        }

        OptionalInt maxLineLength = logEventListener.events()
                .stream()
                .map(LogEvent::getElement)
                .map(String::length)
                .mapToInt(Integer::intValue)
                .max();

        int count = maxLineLength.orElse(0) >= 20 ? (maxLineLength.getAsInt() + 1) : 20;

        StringBuilder sb = new StringBuilder();
        sb.append("Report for ").append(title).append(" .... -> ").append(BaseTest.BROWSER_CONFIG.get()).append('\n');

        String delimiter = '+' + Joiner.on('+').join(line(count), line(70), line(10), line(10)) + "+\n";

        sb.append(delimiter);
        sb.append(String.format("|%-" + count + "s|%-70s|%-10s|%-10s|%n", "Element", "Subject", "Status", "ms."));
        sb.append(delimiter);

        for (LogEvent e : logEventListener.events()) {
            sb.append(String.format("|%-" + count + "s|%-70s|%-10s|%-10s|%n", e.getElement(), e.getSubject(),
                    e.getStatus(), e.getDuration()));
        }
        sb.append(delimiter);
        //log.info(sb.toString());
        System.out.println(sb.toString());
    }

    public void clean() {
        SelenideLogger.removeListener(LISTENER_ID);
    }

    private String line(int count) {
        return Joiner.on("").join(Collections.nCopies(count, "-"));
    }
}

