/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wei.utillibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE) {
            return "刚刚";
        } else if (diff < 2 * MINUTE) {
            return "1分钟前";
        } else if (diff < 50 * MINUTE) {
            return diff / MINUTE + "分钟前";
        } else if (diff < 90 * MINUTE) {
            return "1小时前";
        } else if (diff < 24 * HOUR) {
            return diff / HOUR + "小时前";
        } else if (diff < 48 * HOUR) {
            return "昨天";
        } else {
            return diff / DAY + "天前";
        }
    }

    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd")
    };

    public static Date parseTimestamp(String timestamp) {
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
//            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            format.setTimeZone(TimeZone.getTimeZone("GMT+8")); // fix by WEI 应取东八区，北京时间
            try {
                return format.parse(timestamp);
            } catch (ParseException ex) {
                continue;
            }
        }

        // All attempts to parse have failed
        return null;
    }

    public static long timestampToMillis(String timestamp, long defaultValue) {
        if (TextUtils.isEmpty(timestamp)) {
            return defaultValue;
        }
        Date d = parseTimestamp(timestamp);
        return d == null ? defaultValue : d.getTime();
    }

//    public static String formatShortDate(Context context, Date date) {
//        StringBuilder sb = new StringBuilder();
//        Formatter formatter = new Formatter(sb);
//        return DateUtils.formatDateRange(context, formatter, date.getTime(), date.getTime(),
//                DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR,
//                PrefUtils.getDisplayTimeZone(context).getID()).toString();
//    }
//
//    public static String formatShortTime(Context context, Date time) {
//        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
//        TimeZone tz = PrefUtils.getDisplayTimeZone(context);
//        if (tz != null) {
//            format.setTimeZone(tz);
//        }
//        return format.format(time);
//    }

    /**
     * Returns "Today", "Tomorrow", "Yesterday", or a short date format.
     */
//    public static String formatHumanFriendlyShortDate(final Context context, long timestamp) {
//        long localTimestamp, localTime;
//        long now = System.currentTimeMillis();
//
//        TimeZone tz = PrefUtils.getDisplayTimeZone(context);
//        localTimestamp = timestamp + tz.getOffset(timestamp);
//        localTime = now + tz.getOffset(now);
//
//        long dayOrd = localTimestamp / 86400000L;
//        long nowOrd = localTime / 86400000L;
//
//        if (dayOrd == nowOrd) {
//            return context.getString(R.string.day_title_today);
//        } else if (dayOrd == nowOrd - 1) {
//            return context.getString(R.string.day_title_yesterday);
//        } else if (dayOrd == nowOrd + 1) {
//            return context.getString(R.string.day_title_tomorrow);
//        } else {
//            return formatShortDate(context, new Date(timestamp));
//        }
//    }
    
    /////////////////////////////
    public static String getDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			result = getTime(timesamp);
			break;
		}

		return result;
	}
	
	public static Date parse(String datestr, String pattern){
    	DateFormat dateFormat = new SimpleDateFormat(pattern);
    	Date date = null;
    	try{
    		date = dateFormat.parse(datestr);
    	}catch(ParseException e) {
            e.printStackTrace();
        }
    	return date;
    }
}
