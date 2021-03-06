package com.incadencecorp.coalesce.common.helpers;

import java.util.Date;

import org.apache.commons.lang.NullArgumentException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

/**
 * Provides helper methods for converting between {@link java.lang.String}
 * values and {@link org.joda.time.DateTime} objects.
 * 
 * @author InCadence
 *
 */
public final class JodaDateTimeHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(JodaDateTimeHelper.class);

    // Make static class
    private JodaDateTimeHelper()
    {
        // Do Nothing
    }

    // -----------------------------------------------------------------------'
    // Public Shared Methods
    // -----------------------------------------------------------------------'

    /**
     * Returns the {@link org.joda.time.DateTime} object based on the provided
     * value in the form of 'yyyyMMdd'. If the value cannot be correctly parsed
     * then <code>null</code> is returned.
     * 
     * @param value the date string in the form of 'yyyyMMdd'
     * @return the converted date object
     * @throws NullArgumentException
     */
    public static DateTime convertyyyyMMddDateStringToDateTime(String value)
    {
        if (value == null)
            throw new NullArgumentException("value");

        try
        {
            DateTimeFormatter dateFormat = ISODateTimeFormat.basicDate().withZone(DateTimeZone.UTC);

            return dateFormat.parseDateTime(value);

        }
        catch (IllegalArgumentException iae)
        {
            return null;
        }
    }

    /**
     * Returns a {@link String} representation of the value. If
     * <code>dateOnly</code> is true then only the date will be generated
     * otherwise the format will be 'yyyy-MM-dd HH:mm:ssZZ'.
     * 
     * @param value the date to be formatted as a string
     * @param dateOnly whether the time be included along with the date
     * @return the date converted to a string
     */
    public static String militaryFormat(DateTime value, boolean dateOnly)
    {
        if (value == null)
            throw new NullArgumentException("value");

        if (dateOnly)
        {
            return value.toString(ISODateTimeFormat.date());
        }
        else
        {
            return value.toString(ISODateTimeFormat.dateTimeNoMillis()).replace("T", " ");
        }
    }

    /**
     * Returns a {@link String} representation of the
     * {@link org.joda.time.DateTime} in the format used by MySQL.
     * 
     * @param value the date time to be converted.
     * @return the formatted string.
     */
    public static String toMySQLDateTime(DateTime value)
    {
        return value.toString().replace("T", " ").replace("Z", "");
    }

    /**
     * Returns a {@link org.joda.time.DateTime} converted from a date string
     * from MySQL.
     * 
     * @param value the MySQL string date.
     * @return the converted date
     */
    public static DateTime getMySQLDateTime(String value)
    {
        if (value.indexOf(" ") > 1)
        {
            value = value.replace(" ", "T") + "Z";
        }
        else if (value.indexOf("T") > 1 && value.indexOf("Z") == 0)
        {
            value = value + "Z";
        }

        DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
        return formatter.parseDateTime(value);

        // return DateTime.parse(value);
    }

    /**
     * Returns a {@link String} representation of the
     * {@link org.joda.time.DateTime} in the format used by PostGest.
     * 
     * @param value the date time to be converted.
     * @return the formatted string.
     */
    public static String toPostGestSQLDateTime(DateTime value)
    {
        return value.toString();
    }

    /**
     * Returns a {@link org.joda.time.DateTime} converted from a date string
     * from PostGres. If the string cannot be parsed correctly then
     * <code>null</code> is returned;
     * 
     * @param value the PostGres string date.
     * @return the converted date or <code>null</code> if the string cannot be
     *         parsed.
     */
    public static DateTime getPostGresDateTim(String value)
    {
        if (StringHelper.isNullOrEmpty(value))
        {
            return null;
        }

        try
        {
            DateTimeFormatter outputFormatter = ISODateTimeFormat.dateTimeParser().withZone(DateTimeZone.UTC);

            return outputFormatter.parseDateTime(value.replace(" ", "T"));
        }
        catch (Exception e)
        {
            LOGGER.warn("Invalid Time Format: ({})", value, e);
            return null;
        }
    }

    /**
     * Returns a string describing the elapsed time between the
     * <code>forDate</code> and the current time using GMT.
     * 
     * <pre>
     * Examples:
     * 
     *   2014-05-06 06:08:09Z (1 minute ago)
     *   (1 hour till)
     *   (Yesterday)
     *   (30 days ago)
     *   (5 years till)
     * </pre>
     * 
     * @param forDate the date to compare for elapsed time.
     * @param includeParenthesis if surrounds parenthesis should be included.
     * @param includeTime if the date/time should be included.
     * @return the string representing the elapsed time.
     */
    public static String getElapsedGMTTimeString(DateTime forDate, boolean includeParenthesis, boolean includeTime)
    {
        return getElapsedGMTTimeString(forDate, includeParenthesis, includeTime, includeTime);
    }

    /**
     * Returns a string describing the elapsed time between the
     * <code>forDate</code> and the current time using GMT.
     * 
     * <pre>
     * Examples:
     * 
     *   2014-05-06 06:08:09Z (1 minute ago)
     *   2014-05-06 (1 hour till)
     *   (Yesterday)
     *   (30 days ago)
     *   (5 years till)
     * </pre>
     * 
     * @param forDate the date to compare for elapsed time.
     * @param includeParenthesis if surrounds parenthesis should be included.
     * @param includeDateTime if the date/time should be included.
     * @param dateOnly if only the date of the date/time should be included.
     * @return the string representing the elapsed time.
     */
    public static String getElapsedGMTTimeString(DateTime forDate,
                                                 boolean includeParenthesis,
                                                 boolean includeDateTime,
                                                 boolean dateOnly)
    {
        return getElapsedGMTTimeString(forDate,
                                       new DateTime(DateTimeZone.UTC),
                                       includeParenthesis,
                                       includeDateTime,
                                       dateOnly);

    }

    /**
     * Returns a string describing the elapsed time between the two dates using
     * GMT.
     * 
     * <pre>
     * Examples:
     * 
     *   2014-05-06 06:08:09Z (1 minute ago)
     *   2014-05-06 (1 hour till)
     *   (Yesterday)
     *   (30 days ago)
     *   (5 years till)
     * </pre>
     * 
     * @param firstDate the date beginning the elapsed time.
     * @param secondDate the date ending the elapsed time.
     * @param includeParenthesis if surrounds parenthesis should be included.
     * @param includeDateTime if the date/time should be included.
     * @param dateOnly if only the date of the date/time should be included.
     * @return the string representing the elapsed time.
     */
    public static String getElapsedGMTTimeString(DateTime firstDate,
                                                 DateTime secondDate,
                                                 boolean includeParenthesis,
                                                 boolean includeDateTime,
                                                 boolean dateOnly)
    {
        if (firstDate == null)
            throw new NullArgumentException("firstDate");
        if (secondDate == null)
            throw new NullArgumentException("secondDate");

        boolean isFutureDate = false;
        String elapsedString = "";

        Duration dateDiff = new Duration(firstDate, secondDate);
        long totalSeconds = dateDiff.getStandardSeconds();

        if (totalSeconds < 0)
        {
            totalSeconds = totalSeconds * -1;
            isFutureDate = true;
        }

        if (totalSeconds < 60)
        {
            elapsedString = getLessThanMinuteElapsedTime(elapsedString, totalSeconds, isFutureDate);
        }
        else if (totalSeconds < 3600)
        {
            elapsedString = getLessThanHourElapsedTime(elapsedString, totalSeconds, isFutureDate);
        }
        else if (totalSeconds < 86400)
        {
            elapsedString = getLessThanDayElapsedTime(elapsedString, totalSeconds, isFutureDate);
        }
        else if (totalSeconds < 172800)
        {
            if (isFutureDate)
            {
                elapsedString = "Tomorrow";
            }
            else
            {
                elapsedString = "Yesterday";
            }
        }
        else if (totalSeconds < 31536000)
        {
            long totalDays = totalSeconds / 86400;

            if (isFutureDate)
            {
                elapsedString = totalDays + " days till";
            }
            else
            {
                elapsedString = totalDays + " days ago";
            }

        }
        else
        {
            elapsedString = getYearOrMoreElapsedTime(elapsedString, totalSeconds, isFutureDate);
        }

        // Trim
        elapsedString = elapsedString.trim();

        // Parenthesis?
        if (includeParenthesis)
        {
            elapsedString = "(" + elapsedString + ")";
        }

        if (includeDateTime)
        {
            elapsedString = militaryFormat(firstDate, dateOnly) + " " + elapsedString;
        }

        return elapsedString;
    }

    /**
     * Converts a {@link org.joda.time.DateTime} to the string format used in
     * the serialization of {@link CoalesceEntity} to XML.
     * 
     * @param forDate the date.
     * @return the string representation of the {@link org.joda.time.DateTime}.
     */
    public static String toXmlDateTimeUTC(DateTime forDate)
    {
        if (forDate == null)
        {
            throw new NullArgumentException("forDate");
        }

        DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

        return formatter.print(forDate);

    }

    /**
     * Converts a date/time stored in the XML format used to serialize
     * {@link CoalesceEntity} to a {@link org.joda.time.DateTime}. If the string
     * cannot be parsed then <code>null</code> is returned.
     * 
     * @param xmlDate the XML date string.
     * @return the {@link org.joda.time.DateTime} representation of the XML
     *         string or <code>null</code> if the string cannot be parsed.
     */
    public static DateTime fromXmlDateTimeUTC(String xmlDate)
    {
        if (StringHelper.isNullOrEmpty(xmlDate))
        {
            return null;
        }

        try
        {
            DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

            return formatter.parseDateTime(xmlDate);

        }
        catch (IllegalArgumentException e)
        {
            LOGGER.warn("Invalid Time Format: ({})", xmlDate, e);
            return null;
        }
    }

    /**
     * Returns a {@link org.joda.time.DateTime} for the current time and a time
     * zone set to UTC.
     * 
     * @return the {@link org.joda.time.DateTime} for the current time and a
     *         time zone set to UTC.
     */
    public static DateTime nowInUtc()
    {
        return new DateTime(DateTimeZone.UTC);
    }

    /**
     * Returns a {@link org.joda.time.DateTime} for the current time and a time
     * zone set to UTC.
     * 
     * @return the {@link org.joda.time.DateTime} for the current time and a
     *         time zone set to UTC.
     */
    public static Date nowInUtcAsDate()
    {
        return new Date(nowInUtc().getMillis());
    }

    // -----------------------------------------------------------------------'
    // Private Shared Methods
    // -----------------------------------------------------------------------'

    private static String getLessThanMinuteElapsedTime(String elapsedString, long totalSeconds, boolean isFutureDate)
    {

        if (totalSeconds == 1)
        {
            elapsedString = "1 second";
        }
        else
        {
            elapsedString = totalSeconds + " seconds";
        }

        if (isFutureDate)
        {
            elapsedString += " till";
        }
        else
        {
            elapsedString += " ago";
        }

        return elapsedString;

    }

    private static String getLessThanHourElapsedTime(String elapsedString, long totalSeconds, boolean isFutureDate)
    {

        long totalMinutes = totalSeconds / 60;

        if (totalMinutes == 1)
        {
            elapsedString = "1 minute";
        }
        else
        {
            elapsedString = totalMinutes + " minutes";
        }

        if (isFutureDate)
        {
            elapsedString += " till";
        }
        else
        {
            elapsedString += " ago";
        }

        return elapsedString;

    }

    private static String getLessThanDayElapsedTime(String elapsedString, long totalSeconds, boolean isFutureDate)
    {

        long totalHours = (totalSeconds / 3600);

        if (totalHours == 1)
        {
            elapsedString = "1 hour";
        }
        else
        {
            elapsedString = totalHours + " hours";
        }

        if (isFutureDate)
        {
            elapsedString = elapsedString + " till";
        }
        else
        {
            elapsedString = elapsedString + " ago";
        }

        return elapsedString;

    }

    private static String getYearOrMoreElapsedTime(String elapsedString, long totalSeconds, boolean isFutureDate)
    {

        long totalYears = totalSeconds / 31536000;

        if (totalYears == 1)
        {
            elapsedString = "1 year";
        }
        else
        {
            elapsedString = totalYears + " years";
        }

        if (isFutureDate)
        {
            elapsedString = elapsedString + " till";
        }
        else
        {
            elapsedString = elapsedString + " ago";
        }

        return elapsedString;

    }

}
