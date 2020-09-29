package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;


/**
 *@author 作者  李涛
 *@version v1
 *创建时间：2014年11月23日下午4:16:25
 *类说明：
 */
public class FormatDateUtil {
	private static String defaultDatePattern = "yyyy-MM-dd";
	@SuppressWarnings("unused")
	private static  String FM_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
	
	
	
	
	public static Date formatDate2(String dateString){
		if(!StringUtils.isEmpty(dateString)){
			SimpleDateFormat sd=new SimpleDateFormat(FM_DATE_AND_TIME);
			try {
				return sd.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static Date formatStringToDate(String pattern,String dateString){
		if(StringUtils.isEmpty(pattern)){
			pattern=defaultDatePattern;
		}
		if(!StringUtils.isEmpty(dateString)){
			SimpleDateFormat sd=new SimpleDateFormat(pattern);
			try {
				return sd.parse(dateString);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static String formatString1(String StringDate) throws ParseException{
		if(!StringUtils.isEmpty(StringDate)){
			SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date data=(Date) myFmt.parse(StringDate);
			String time = myFmt.format(data);
			return time;
		}else{
			return StringDate;
		}
		
	}
	
	

	
	public static String formatDateToString(String pattern,Date dateString){
		if(StringUtils.isEmpty(pattern)){
			pattern=defaultDatePattern;
		}
		if(null != dateString){
			SimpleDateFormat sd=new SimpleDateFormat(pattern);
			try {
				return sd.format(dateString);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public static String formatDataStringToString(String pattern,String pattern1,String dateString){
		if(StringUtils.isEmpty(pattern)){
			pattern=defaultDatePattern;
		}
		if(!StringUtils.isEmpty(dateString)){
			SimpleDateFormat sd=new SimpleDateFormat(pattern);
			try {
				Date data=(Date) sd.parse(dateString);
				SimpleDateFormat sd1=new SimpleDateFormat(pattern1);
				return sd1.format(data);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public static String formatString(String date2)  {  
		if(!StringUtils.isEmpty(date2)){
			try {
				SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = sd.parse(date2);
			    String todySDF = "今天 HH:mm";  
			    String yesterDaySDF = "昨天 HH:mm";  
			    String otherSDF = "M月d日 HH:mm";  
			    String yearSDF = "Y年M月d日 HH:mm";  
			    SimpleDateFormat sfd=null ;  
			    String time = "";  
			    Calendar dateCalendar = Calendar.getInstance();  
			    dateCalendar.setTime(date); 
			    int year = dateCalendar.get(Calendar.YEAR);//获取年份
			    int month = dateCalendar.get(Calendar.MONTH)+1;//获取月份
			    int day = dateCalendar.get(Calendar.DAY_OF_MONTH);//获取日
			    
			    Date now = new Date();  
			    Calendar targetCalendar = Calendar.getInstance();  
			    targetCalendar.setTime(now);  
			    int year1 = targetCalendar.get(Calendar.YEAR);//获取年份   系统当年年份
			    int month1 = targetCalendar.get(Calendar.MONTH)+1;//获取月份
			    int day1 = targetCalendar.get(Calendar.DAY_OF_MONTH);//获取日
			    
			    if(year1==year){
			    	if(month1==month){
				    		if(day==day1){
				    			 sfd = new SimpleDateFormat(todySDF);  
							        time = sfd.format(date);  
							        return time;  
					    		}else{
							        if(day1-day==1) {  
							            sfd = new SimpleDateFormat(yesterDaySDF);  
							            time = sfd.format(date);  
							            return time;  
							        }else{
							        	sfd = new SimpleDateFormat(otherSDF);  
						        		time = sfd.format(date);  
						        		return time; 
							        }
				    		}
			    	}else{
		        		sfd = new SimpleDateFormat(otherSDF);  
		        		time = sfd.format(date);  
		        		return time; 
			    	}
			    }else{
			    	  sfd = new SimpleDateFormat(yearSDF);  
					    time = sfd.format(date);  
					    return time; 
			    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			return null;
		}
	
		return null;
		
	} 
	
	
	// 将传入时间与当前时间进行对比，是否今天昨天  
	public static String formatDate3(Date date) {  
		if(null != date){
			String todySDF = " HH:mm";  
		    String yesterDaySDF = "昨天 HH:mm";  
		    String otherSDF = "M月d日 HH:mm";  
		    String yearSDF = "Y年M月d日 HH:mm";  
		    SimpleDateFormat sfd=null ;  
		    String time = "";  
		    try {
		    	 Calendar dateCalendar = Calendar.getInstance();  
				    dateCalendar.setTime(date); 
				    int year = dateCalendar.get(Calendar.YEAR);//获取年份
				    int month = dateCalendar.get(Calendar.MONTH)+1;//获取月份
				    int day = dateCalendar.get(Calendar.DAY_OF_MONTH);//获取日
				    
				    Date now = new Date();  
				    Calendar targetCalendar = Calendar.getInstance();  
				    targetCalendar.setTime(now);  
				    int year1 = targetCalendar.get(Calendar.YEAR);//获取年份   系统当年年份
				    int month1 = targetCalendar.get(Calendar.MONTH)+1;//获取月份
				    int day1 = targetCalendar.get(Calendar.DAY_OF_MONTH);//获取日
				    
				    if(year1==year){
				    	if(month1==month){
					    		if(day==day1){
					    			 sfd = new SimpleDateFormat(todySDF);  
								        time = sfd.format(date);  
								        return time;  
						    		}else{
								        if(day1-day==1) {  
								            sfd = new SimpleDateFormat(yesterDaySDF);  
								            time = sfd.format(date);  
								            return time;  
								        }else{
								        	sfd = new SimpleDateFormat(otherSDF);  
							        		time = sfd.format(date);  
							        		return time; 
								        }
					    		 
					    		}
				    	}else{
			        		sfd = new SimpleDateFormat(otherSDF);  
			        		time = sfd.format(date);  
			        		return time; 
				    	}
					      
				    }else{
				    	  sfd = new SimpleDateFormat(yearSDF);  
						    time = sfd.format(date);  
						    return time; 
				    }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			   return null;
		}
		  
		    
		   return null;
		} 
	
	
	
	
	// 将传入时间与当前时间进行对比，是否今天昨天  
		public static String formatDate(Date date) {  
			if(null != date){
				String todySDF = "今天 HH:mm";  
			    String yesterDaySDF = "昨天 HH:mm";  
			    String otherSDF = "M月d日 HH:mm";  
			    String yearSDF = "Y年M月d日 HH:mm";  
			    SimpleDateFormat sfd=null ;  
			    String time = "";  
			    try {
			    	 Calendar dateCalendar = Calendar.getInstance();  
					    dateCalendar.setTime(date); 
					    int year = dateCalendar.get(Calendar.YEAR);//获取年份
					    int month = dateCalendar.get(Calendar.MONTH)+1;//获取月份
					    int day = dateCalendar.get(Calendar.DAY_OF_MONTH);//获取日
					    
					    Date now = new Date();  
					    Calendar targetCalendar = Calendar.getInstance();  
					    targetCalendar.setTime(now);  
					    int year1 = targetCalendar.get(Calendar.YEAR);//获取年份   系统当年年份
					    int month1 = targetCalendar.get(Calendar.MONTH)+1;//获取月份
					    int day1 = targetCalendar.get(Calendar.DAY_OF_MONTH);//获取日
					    
					    if(year1==year){
					    	if(month1==month){
						    		if(day==day1){
						    			 sfd = new SimpleDateFormat(todySDF);  
									        time = sfd.format(date);  
									        return time;  
							    		}else{
									        if(day1-day==1) {  
									            sfd = new SimpleDateFormat(yesterDaySDF);  
									            time = sfd.format(date);  
									            return time;  
									        }else{
									        	sfd = new SimpleDateFormat(otherSDF);  
								        		time = sfd.format(date);  
								        		return time; 
									        }
						    		 
						    		}
					    	}else{
				        		sfd = new SimpleDateFormat(otherSDF);  
				        		time = sfd.format(date);  
				        		return time; 
					    	}
						      
					    }else{
					    	  sfd = new SimpleDateFormat(yearSDF);  
							    time = sfd.format(date);  
							    return time; 
					    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				   return null;
			}
			  
			    
			   return null;
			} 
	
		//日程 时间初始化不显示 "今天 HH:mm";  
		public static String workDate(String date2)  {
			if(!StringUtils.isEmpty(date2)){
				try {
					SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date date = sd.parse(date2);
				    String todySDF = "今天 HH:mm";  
				    String yesterDaySDF = "昨天 HH:mm";  
				    String otherSDF = "M月d日 HH:mm";  
				    String yearSDF = "Y年M月d日 HH:mm";  
				    SimpleDateFormat sfd=null ;  
				    String time = "";  
				    Calendar dateCalendar = Calendar.getInstance();  
				    dateCalendar.setTime(date); 
				    int year = dateCalendar.get(Calendar.YEAR);//获取年份
				    int month = dateCalendar.get(Calendar.MONTH)+1;//获取月份
				    int day = dateCalendar.get(Calendar.DAY_OF_MONTH);//获取日
				    
				    Date now = new Date();  
				    Calendar targetCalendar = Calendar.getInstance();  
				    targetCalendar.setTime(now);  
				    int year1 = targetCalendar.get(Calendar.YEAR);//获取年份   系统当年年份
				    int month1 = targetCalendar.get(Calendar.MONTH)+1;//获取月份
				    int day1 = targetCalendar.get(Calendar.DAY_OF_MONTH);//获取日
				    
				    if(year1==year){
				    	if(month1==month){
					    		if(day==day1){
					    			 sfd = new SimpleDateFormat(otherSDF);  
								        time = sfd.format(date);  
								        return time;  
						    		}else{
								        if(day1-day==1) {  
								            sfd = new SimpleDateFormat(yesterDaySDF);  
								            time = sfd.format(date);  
								            return time;  
								        }else{
								        	sfd = new SimpleDateFormat(otherSDF);  
							        		time = sfd.format(date);  
							        		return time; 
								        }
					    		 
					    		}
				    	}else{
			        		sfd = new SimpleDateFormat(otherSDF);  
			        		time = sfd.format(date);  
			        		return time; 
				    	}
					      
				    }else{
				    	  sfd = new SimpleDateFormat(yearSDF);  
						    time = sfd.format(date);  
						    return time; 
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				return null;
			}
			return null;
		} 
		
		/** * 获取指定日期是星期几

		  * 参数为null时表示获取当前日期是星期几

		  * @param date

		  * @return

		*/

		public static String getWeekOfDate(Date date) {  
			if(null != date){
				return "";
			}else{
				 String[] weekOfDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};        
				    Calendar calendar = Calendar.getInstance();      
				    if(date != null){        
				         calendar.setTime(date);      
				    }        
				    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
				    if (w < 0){        
				        w = 0;      
				    }      
				    String string = formatDateToString("yyyy-MM-dd HH:mm",date);
				    String[] split = string.split(" ");
				    String s=  split[0]+" "+weekOfDays[w]+" "+split[1];
				    return s;    
			}
		   
		}
		
		public static String getUUid(){ 
			String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");    
	        return uuid;
		}
		
		public static String getNowDate(){
			SimpleDateFormat sd=new SimpleDateFormat(defaultDatePattern);
			return sd.format(new Date());
		}

}

