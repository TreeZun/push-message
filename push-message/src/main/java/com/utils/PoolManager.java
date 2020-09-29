package com.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/*
 * 线程池的创建与管理类
 */
public class PoolManager {
	private static int corePoolSize=3;//线程池维护线程的最少数量
	private static int maximumPoolSize=20;//线程池维护线程的最大数量
	private static long keepAliveTime=6;//线程池维护线程所允许的空闲时间
	private static TimeUnit unit=TimeUnit.SECONDS;//线程池维护线程所允许的空闲时间的单位
	//private BlockingQueue<Runnable> workQueue;//线程池所使用的缓冲队列(任务排队有3种基本方法：无限队列、有限队列和同步移交。
	//private RejectedExecutionHandler handler;// 线程池对拒绝任务的处理策略
	private static int arrayListSize=30000;//缓冲队列大小
	private static ThreadPoolExecutor threadPool=null;
	private static PoolManager tzoaPoolManager=null;
	
	
	private  PoolManager(){
		
	}
	
	/**
	 * 为了支持多线程，使其线程是安全的，采用单例模式
	 * @return
	 */
	public static synchronized PoolManager getInstance(){
		if(tzoaPoolManager==null){
			tzoaPoolManager=new PoolManager();
		}
		return tzoaPoolManager;
	}
	
	/**
	 * 
	 * 此线程池ThreadPoolExecutor是java5（包含java5）以上版本才具有的特性
	 * @return
	 */
	public  static  synchronized ThreadPoolExecutor getThreadPool(){
		 try {
			if(threadPool==null){
				return threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,  
				        unit, new ArrayBlockingQueue<Runnable>(arrayListSize),  
				        new ThreadPoolExecutor.DiscardOldestPolicy());
			}else{
				return threadPool;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
}
