package org.nic.stockCharts.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil 
{
	private static final String TIME_FORMAT = "HH:mm:ss";
	
	
	public static String getFormattedTime()
	{
		Calendar cal = Calendar.getInstance();
		
		Date date = new Date();
		date = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
		
		return df.format(date);
	}
	
	
}
