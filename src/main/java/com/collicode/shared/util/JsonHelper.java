package com.collicode.shared.util;

import com.collicode.shared.exception.APIDataValidationException;
import com.collicode.shared.response.ErrorMessage;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.number.NumberStyleFormatter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.*;

public class JsonHelper {

    public static String toJson(Object object) {
        return gson().toJson(object);
    }

    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())

                .create();
    }

    public static <T> T toObject(String json, Class<T> tClass) {
        return gson()
                .fromJson(json, tClass);
    }

    public static <T> T toObject(String jsonString, Type typeOfT) {
        return gson().fromJson(jsonString, typeOfT);
    }

    public static Long extractLongNamed(final String parameterName, final JsonElement element,
                                        final Set<String> parametersPassedInRequest) {
        Long longValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                parametersPassedInRequest.add(parameterName);
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String stringValue = primitive.getAsString();
                if (StringUtils.isNotBlank(stringValue)) {
                    longValue = Long.valueOf(stringValue);
                }
            }
        }
        return longValue;
    }

    public static String extractStringNamed(final String parameterName, final JsonElement element,
                                            final Set<String> parametersPassedInRequest) {
        String stringValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                parametersPassedInRequest.add(parameterName);
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    stringValue = valueAsString;
                }
            }
        }
        return stringValue;
    }

    public static LocalDate convertFrom(final String dateAsString, final String parameterName,
                                        final String dateFormat,
                                        final Locale clientApplicationLocale) {

        return convertDateTimeFrom(dateAsString, parameterName, dateFormat,
                clientApplicationLocale).toLocalDate();
    }

    public static LocalDateTime convertDateTimeFrom(final String dateTimeAsString,
                                                    final String parameterName, final String dateTimeFormat,
                                                    final Locale clientApplicationLocale) {

        validateDateFormatAndLocale(parameterName, dateTimeFormat, clientApplicationLocale);
        LocalDateTime eventLocalDateTime = null;
        if (StringUtils.isNotBlank(dateTimeAsString)) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                        .parseLenient()
                        .appendPattern(dateTimeFormat).optionalStart().appendPattern(" HH:mm:ss").optionalEnd()
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter(clientApplicationLocale);
                eventLocalDateTime = LocalDateTime.parse(dateTimeAsString, formatter);
            } catch (final IllegalArgumentException | DateTimeParseException e) {

                final ErrorMessage error = ErrorMessage
                        .of("API.400",
                                "The parameter `" + parameterName + "` is invalid based on the dateFormat: `"
                                        + dateTimeFormat
                                        + "` and locale: `" + clientApplicationLocale + "` provided:" + parameterName
                                        + eventLocalDateTime + dateTimeFormat);

                throw new APIDataValidationException(error);
            }
        }

        return eventLocalDateTime;
    }

    private static void validateDateFormatAndLocale(final String parameterName,
                                                    final String dateFormat,
                                                    final Locale clientApplicationLocale) {
        if (StringUtils.isBlank(dateFormat) || clientApplicationLocale == null) {

            if (StringUtils.isBlank(dateFormat)) {
                final String defaultMessage = "The parameter `" + parameterName
                        + "` requires a `dateFormat` parameter to be passed with it.";
                final ErrorMessage error = ErrorMessage.of("API.400", defaultMessage);
                throw new APIDataValidationException(error);
            }
            if (clientApplicationLocale == null) {
                final String defaultMessage = "The parameter `" + parameterName
                        + "` requires a `locale` parameter to be passed with it.";
                final ErrorMessage error = ErrorMessage.of("API.400", defaultMessage);
                throw new APIDataValidationException(error);

            }

            final String defaultMessage = "The parameter `" + parameterName
                    + "` requires a `dateFormat` parameter to be passed with it.";

            final ErrorMessage error = ErrorMessage.of("API.400", defaultMessage);
            throw new APIDataValidationException(error);
        }

    }

    /***
     * TODO: Vishwas move all Locale related code to a separate Utils class
     ***/
    public static Locale localeFromString(final String localeAsString) {

        if (StringUtils.isBlank(localeAsString)) {

            final ErrorMessage error = ErrorMessage.of(
                    "API.400",
                    "The parameter `locale` is invalid. It cannot be blank. locale");

            throw new APIDataValidationException(error);
        }

        String languageCode = "";
        String countryCode = "";
        String variantCode = "";

        final String[] localeParts = localeAsString.split("_");

        if (localeParts != null && localeParts.length == 1) {
            languageCode = localeParts[0];
        }

        if (localeParts != null && localeParts.length == 2) {
            languageCode = localeParts[0];
            countryCode = localeParts[1];
        }

        if (localeParts != null && localeParts.length == 3) {
            languageCode = localeParts[0];
            countryCode = localeParts[1];
            variantCode = localeParts[2];
        }

        return localeFrom(languageCode, countryCode, variantCode);
    }

    private static Locale localeFrom(final String languageCode, final String courntryCode,
                                     final String variantCode) {

        final List<String> allowedLanguages = Arrays.asList(Locale.getISOLanguages());
        if (!allowedLanguages.contains(languageCode.toLowerCase())) {
            final ErrorMessage error = ErrorMessage.of(
                    "API.400",
                    "The parameter `locale` has an invalid language value " + languageCode + " ." + "locale"
                            + languageCode);
            throw new APIDataValidationException(error);

        }

        if (StringUtils.isNotBlank(courntryCode.toUpperCase())) {
            final List<String> allowedCountries = Arrays.asList(Locale.getISOCountries());
            if (!allowedCountries.contains(courntryCode)) {
                final ErrorMessage error = ErrorMessage.of(
                        "API.400",
                        "The parameter `locale` has an invalid country value " + courntryCode + " ." + "locale"
                                + courntryCode);

                throw new APIDataValidationException(error);

            }
        }

        return new Locale(languageCode.toLowerCase(), courntryCode.toUpperCase(), variantCode);
    }

    public boolean parameterExists(final String parameterName, final JsonElement element) {
        if (element == null) {
            return false;
        }
        return element.getAsJsonObject().has(parameterName);
    }

    public Boolean extractBooleanNamed(final String parameterName, final JsonElement element,
                                       final Set<String> requestParamatersDetected) {
        Boolean value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                requestParamatersDetected.add(parameterName);

                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                value = primitive.getAsBoolean();
            }
        }
        return value;
    }

    public BigDecimal extractBigDecimalWithLocaleNamed(final String parameterName,
                                                       final JsonElement element,
                                                       final Set<String> modifiedParameters) {
        BigDecimal value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final Locale locale = extractLocaleValue(object);
            value = extractBigDecimalNamed(parameterName, object, locale, modifiedParameters);
        }
        return value;
    }

    public BigDecimal extractBigDecimalNamed(final String parameterName, final JsonObject element,
                                             final Locale locale,
                                             final Set<String> modifiedParameters) {
        BigDecimal value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                modifiedParameters.add(parameterName);
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                if (!primitive.isJsonNull()) {
                    if (primitive.isNumber()) {
                        value = primitive.getAsBigDecimal();
                    } else {
                        final String valueAsString = primitive.getAsString();
                        if (StringUtils.isNotBlank(valueAsString)) {
                            value = convertFrom(valueAsString, parameterName, locale);
                        }
                    }
                }
            }
        }
        return value;
    }

    public Integer extractIntegerWithLocaleNamed(final String parameterName,
                                                 final JsonElement element,
                                                 final Set<String> modifiedParameters) {
        Integer value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final Locale locale = extractLocaleValue(object);
            value = extractIntegerNamed(parameterName, object, locale, modifiedParameters);
        }
        return value;
    }

    public Integer extractIntegerNamed(final String parameterName, final JsonElement element,
                                       final Locale locale,
                                       final Set<String> modifiedParameters) {
        Integer value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                modifiedParameters.add(parameterName);
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    value = convertToInteger(valueAsString, parameterName, locale);
                }
            }
        }
        return value;
    }

    /**
     * Method used to extract integers from unformatted strings. Ex: "1" , "100002" etc
     * <p>
     * Please note that this method does not support extracting Integers from locale specific
     * formatted strings Ex "1,000" etc
     *
     * @param parameterName
     * @param element
     * @param parametersPassedInRequest
     * @return
     */
    public Integer extractIntegerSansLocaleNamed(final String parameterName,
                                                 final JsonElement element,
                                                 final Set<String> parametersPassedInRequest) {
        Integer intValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                parametersPassedInRequest.add(parameterName);
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String stringValue = primitive.getAsString();
                if (StringUtils.isNotBlank(stringValue)) {
                    intValue = convertToIntegerSanLocale(stringValue, parameterName);
                }
            }
        }
        return intValue;
    }

    public String extractDateFormatParameter(final JsonObject element) {
        String value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String dateFormatParameter = "dateFormat";
            if (object.has(dateFormatParameter) && object.get(dateFormatParameter).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(dateFormatParameter).getAsJsonPrimitive();
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public String extractTimeFormatParameter(final JsonObject element) {
        String value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String timeFormatParameter = "timeFormat";
            if (object.has(timeFormatParameter) && object.get(timeFormatParameter).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(timeFormatParameter).getAsJsonPrimitive();
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public String extractMonthDayFormatParameter(final JsonObject element) {
        String value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String monthDayFormatParameter = "monthDayFormat";
            if (object.has(monthDayFormatParameter) && object.get(monthDayFormatParameter)
                    .isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(monthDayFormatParameter).getAsJsonPrimitive();
                value = primitive.getAsString();
            }
        }
        return value;
    }

    public Locale extractLocaleParameter(final JsonObject element) {
        Locale clientApplicationLocale = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            String locale = null;
            final String localeParameter = "locale";
            if (object.has(localeParameter) && object.get(localeParameter).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(localeParameter).getAsJsonPrimitive();
                locale = primitive.getAsString();
                clientApplicationLocale = localeFromString(locale);
            }
        }
        return clientApplicationLocale;
    }

    public String[] extractArrayNamed(final String parameterName, final JsonElement element,
                                      final Set<String> parametersPassedInRequest) {
        String[] arrayValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName)) {
                parametersPassedInRequest.add(parameterName);
                final JsonArray array = object.get(parameterName).getAsJsonArray();
                arrayValue = new String[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    arrayValue[i] = array.get(i).getAsString();
                }
            }
        }
        return arrayValue;
    }

    public JsonArray extractJsonArrayNamed(final String parameterName, final JsonElement element) {
        JsonArray jsonArray = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName)) {
                jsonArray = object.get(parameterName).getAsJsonArray();
            }
        }

        return jsonArray;
    }

    public JsonObject extractJsonObjectNamed(final String parameterName, final JsonElement element) {
        JsonObject jsonObject = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName)) {
                jsonObject = object.get(parameterName).getAsJsonObject();
            }
        }

        return jsonObject;
    }

    /**
     * Used with the local date is in array format
     */
    public LocalDate extractLocalDateAsArrayNamed(final String parameterName,
                                                  final JsonElement element,
                                                  final Set<String> parametersPassedInCommand) {
        LocalDate value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            if (object.has(parameterName) && object.get(parameterName).isJsonArray()) {
                parametersPassedInCommand.add(parameterName);
                final JsonArray dateArray = object.get(parameterName).getAsJsonArray();
                final int year = dateArray.get(0).getAsInt();
                final int month = dateArray.get(1).getAsInt();
                final int day = dateArray.get(2).getAsInt();

                value = LocalDate.of(year, month, day);
            }
        }
        return value;
    }

    public MonthDay extractMonthDayNamed(final String parameterName, final JsonElement element) {

        MonthDay value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String monthDayFormat = extractMonthDayFormatParameter(object);
            final Locale clientApplicationLocale = extractLocaleParameter(object);
            value = extractMonthDayNamed(parameterName, object, monthDayFormat, clientApplicationLocale);
        }
        return value;
    }

    public MonthDay extractMonthDayNamed(final String parameterName, final JsonObject element,
                                         final String dateFormat,
                                         final Locale clientApplicationLocale) {
        MonthDay value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {

                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    try {
                        final DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
                                .parseLenient()
                                .appendPattern(dateFormat).toFormatter(clientApplicationLocale);
                        value = MonthDay.parse(valueAsString, formatter);
                    } catch (final IllegalArgumentException e) {

                        final ErrorMessage error = ErrorMessage.of("",
                                "validation.msg.invalid.month.day" +
                                        "The parameter `" + parameterName
                                        + "` is invalid based on the monthDayFormat: `"
                                        + dateFormat
                                        + "` and locale: `" + clientApplicationLocale + "` provided:" + parameterName);

                        throw new APIDataValidationException(error);
                    }
                }
            }

        }
        return value;
    }

    public LocalDate extractLocalDateNamed(final String parameterName, final JsonElement element,
                                           final Set<String> parametersPassedInCommand) {

        LocalDate value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            final String dateFormat = extractDateFormatParameter(object);
            final Locale clientApplicationLocale = extractLocaleParameter(object);
            value = extractLocalDateNamed(parameterName, object, dateFormat, clientApplicationLocale,
                    parametersPassedInCommand);
        }
        return value;
    }

    public LocalTime extractLocalTimeNamed(final String parameterName, final JsonElement element,
                                           final Set<String> parametersPassedInCommand) {

        LocalTime value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            value = extractLocalTimeNamed(parameterName, element, extractTimeFormatParameter(object),
                    parametersPassedInCommand);
        }
        return value;
    }

    public LocalDateTime extractLocalDateTimeNamed(final String parameterName,
                                                   final JsonElement element,
                                                   final Set<String> parametersPassedInCommand) {

        LocalDateTime value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            value = extractLocalDateTimeNamed(parameterName, element, extractTimeFormatParameter(object),
                    parametersPassedInCommand);
        }
        return value;
    }

    public LocalTime extractLocalTimeNamed(final String parameterName, final JsonElement element,
                                           String timeFormat,
                                           final Set<String> parametersPassedInCommand) {

        LocalTime value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final Locale clientApplicationLocale = extractLocaleParameter(object);
            value = extractLocalTimeNamed(parameterName, object, timeFormat, clientApplicationLocale,
                    parametersPassedInCommand);
        }
        return value;
    }

    public LocalDateTime extractLocalDateTimeNamed(final String parameterName,
                                                   final JsonElement element, String timeFormat,
                                                   final Set<String> parametersPassedInCommand) {

        LocalDateTime value = null;

        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            final Locale clientApplicationLocale = extractLocaleParameter(object);
            value = extractLocalDateTimeNamed(parameterName, object, timeFormat, clientApplicationLocale,
                    parametersPassedInCommand);
        }
        return value;
    }

    public LocalTime extractLocalTimeNamed(final String parameterName, final JsonElement element,
                                           final String timeFormat,
                                           final Locale clientApplicationLocale, final Set<String> parametersPassedInCommand) {
        LocalTime value = null;
        String timeValueAsString = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                parametersPassedInCommand.add(parameterName);

                try {
                    DateTimeFormatter timeFormtter = DateTimeFormatter.ofPattern(timeFormat);
                    final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                    timeValueAsString = primitive.getAsString();
                    if (StringUtils.isNotBlank(timeValueAsString)) {
                        value = LocalTime.parse(timeValueAsString, timeFormtter);
                    }
                } catch (IllegalArgumentException e) {
                    final List<ErrorMessage> dataValidationErrors = new ArrayList<>();
                    final String defaultMessage = "The parameter `" + timeValueAsString + "` is not in correct format.";
                    final ErrorMessage error = ErrorMessage.of(
                            "validation.msg.invalid.TimeFormat", defaultMessage + parameterName);

                    dataValidationErrors.add(error);
                    throw new APIDataValidationException(error);
                }

            }
        }
        return value;
    }

    public LocalDateTime extractLocalDateTimeNamed(final String parameterName,
                                                   final JsonElement element, final String timeFormat,
                                                   final Locale clientApplicationLocale, final Set<String> parametersPassedInCommand) {
        LocalDateTime value = null;
        String timeValueAsString = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                parametersPassedInCommand.add(parameterName);

                try {
                    DateTimeFormatter timeFormtter = DateTimeFormatter.ofPattern(timeFormat);
                    final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                    timeValueAsString = primitive.getAsString();
                    if (StringUtils.isNotBlank(timeValueAsString)) {
                        value = LocalDateTime.parse(timeValueAsString, timeFormtter);
                    }
                } catch (IllegalArgumentException e) {
                    final String defaultMessage = "The parameter `" + timeValueAsString + "` is not in correct format.";
                    final ErrorMessage error = ErrorMessage.of(
                            "validation.msg.invalid.TimeFormat", defaultMessage + parameterName);
                    throw new APIDataValidationException(error);
                }
            }
        }
        return value;
    }

    public LocalDate extractLocalDateNamed(final String parameterName, final JsonElement element,
                                           final String dateFormat,
                                           final Locale clientApplicationLocale, final Set<String> parametersPassedInCommand) {
        LocalDate value = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();

            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {

                parametersPassedInCommand.add(parameterName);

                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    value = convertFrom(valueAsString, parameterName, dateFormat, clientApplicationLocale);
                }
            }

        }
        return value;
    }

    public Integer convertToInteger(final String numericalValueFormatted, final String parameterName,
                                    final Locale clientApplicationLocale) {

        if (clientApplicationLocale == null) {

            final String defaultMessage = "The parameter `" + parameterName
                    + "` requires a `locale` parameter to be passed with it.";

            final ErrorMessage error = ErrorMessage.of("API.400", defaultMessage);
            throw new APIDataValidationException(error);
        }

        try {
            Integer number = null;

            if (StringUtils.isNotBlank(numericalValueFormatted)) {

                String source = numericalValueFormatted.trim();

                final NumberFormat format = NumberFormat.getInstance(clientApplicationLocale);
                final DecimalFormat df = (DecimalFormat) format;
                final DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
                df.setParseBigDecimal(true);

                // http://bugs.sun.com/view_bug.do?bug_id=4510618
                final char groupingSeparator = symbols.getGroupingSeparator();
                if (groupingSeparator == '\u00a0') {
                    source = source.replaceAll(" ", Character.toString('\u00a0'));
                }

                final Number parsedNumber = df.parse(source);

                final double parsedNumberDouble = parsedNumber.doubleValue();
                final int parsedNumberInteger = parsedNumber.intValue();

                if (source.contains(Character.toString(symbols.getDecimalSeparator()))) {
                    throw new ParseException(source, 0);
                }

                if (!Double.valueOf(parsedNumberDouble)
                        .equals(Double.valueOf(Integer.valueOf(parsedNumberInteger)))) {
                    throw new ParseException(source, 0);
                }

                number = parsedNumber.intValue();
            }

            return number;
        } catch (final ParseException e) {

            final ErrorMessage error = ErrorMessage.of(
                    "API.400",
                    "The parameter `" + parameterName + "` has value: " + numericalValueFormatted
                            + " which is invalid integer value for provided locale of ["
                            + clientApplicationLocale + "].");
            throw new APIDataValidationException(error);
        }
    }

    public Integer convertToIntegerSanLocale(final String numericalValueFormatted,
                                             final String parameterName) {

        try {
            Integer number = null;

            if (StringUtils.isNotBlank(numericalValueFormatted)) {
                number = Integer.valueOf(numericalValueFormatted);
            }

            return number;
        } catch (final NumberFormatException e) {

            final ErrorMessage error = ErrorMessage.of(
                    "API.400",
                    "The parameter `" + parameterName + "` has value: " + numericalValueFormatted
                            + " which is invalid integer." + parameterName);

            throw new APIDataValidationException(error);
        }
    }

    public BigDecimal convertFrom(final String numericalValueFormatted, final String parameterName,
                                  final Locale clientApplicationLocale) {

        if (clientApplicationLocale == null) {

            final String defaultMessage = "The parameter `" + parameterName
                    + "` requires a `locale` parameter to be passed with it.";
            final ErrorMessage error = ErrorMessage.of("API.400", defaultMessage);
            throw new APIDataValidationException(error);
        }

        try {
            BigDecimal number = null;

            if (StringUtils.isNotBlank(numericalValueFormatted)) {

                String source = numericalValueFormatted.trim();

                final NumberFormat format = NumberFormat.getNumberInstance(clientApplicationLocale);
                final DecimalFormat df = (DecimalFormat) format;
                final DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
                final char groupingSeparator = symbols.getGroupingSeparator();
                if (groupingSeparator == '\u00a0') {
                    source = source.replaceAll(" ", Character.toString('\u00a0'));
                }

                final NumberStyleFormatter numberFormatter = new NumberStyleFormatter();
                final Number parsedNumber = numberFormatter.parse(source, clientApplicationLocale);
                if (parsedNumber instanceof BigDecimal) {
                    number = (BigDecimal) parsedNumber;
                } else {
                    number = BigDecimal.valueOf(parsedNumber.doubleValue());
                }
            }

            return number;
        } catch (final ParseException e) {

            final ErrorMessage error = ErrorMessage.of(
                    "API.400",
                    "The parameter `" + parameterName + "` has value: " + numericalValueFormatted
                            + " which is invalid decimal value for provided locale of [" + clientApplicationLocale
                            + "].");
            throw new APIDataValidationException(error);
        }
    }

    private Locale extractLocaleValue(final JsonObject object) {
        Locale clientApplicationLocale = null;
        String locale = null;
        if (object.has("locale") && object.get("locale").isJsonPrimitive()) {
            final JsonPrimitive primitive = object.get("locale").getAsJsonPrimitive();
            locale = primitive.getAsString();
            clientApplicationLocale = localeFromString(locale);
        }
        return clientApplicationLocale;
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == null) {
                return null;
            }
            String dateStr = in.nextString();
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    public static class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
                                     final JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter));
        }

        @Override
        public LocalDate deserialize(final JsonElement json, final Type typeOfT,
                                     final JsonDeserializationContext context) throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}
