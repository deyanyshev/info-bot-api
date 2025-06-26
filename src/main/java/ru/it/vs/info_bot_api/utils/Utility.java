package ru.it.vs.info_bot_api.utils;

import ru.it.vs.info_bot_api.model.dto.ReportDto;

import java.util.List;

public class Utility {

    public static String formatMessageText(String text) {
        return text.replaceAll("\\.", "\\\\.")
                .replaceAll("\\-", "\\\\-")
                .replaceAll("\\#", "\\\\#")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)")
                .replaceAll("\\+", "\\\\+")
                .replaceAll("\\!", "\\\\!");
    }

    public static String formatPhone(String phone) {
        if (phone.charAt(0) == '8') return "+7" + phone.substring(1);
        return "+" + phone;
    }

    public static String getReportMessage(List<ReportDto> reports) {
        StringBuilder message = new StringBuilder("*Отчеты:*");

        for (int i = 0; i < reports.size(); ++i) {
            ReportDto report = reports.get(i);

            String status = report.isApproved() ? "Проверен" : "На валидации";
            message.append(String.format("\n\n*%s. Организация:* %s\n*Статус:* %s",
                    i + 1, report.getOrganisationName(), status
            ));
        }

        return message.toString();
    }

}
