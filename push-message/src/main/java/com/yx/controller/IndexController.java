package com.yx.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yx.service.PushMessageService;

import cn.hutool.core.util.RandomUtil;

@Controller
public class IndexController {
	@Resource
	 private PushMessageService pushMessageService;
	
	@GetMapping("/index")
	public ModelAndView  index(){
		ModelAndView mav=new ModelAndView("socket");
		String random = RandomUtil.randomNumbers(6);
		System.out.println("随机数="+ random);
		mav.addObject("uid", random);
		return mav;
	}
	
	@GetMapping("/index1")
	public ModelAndView  index1(){
		ModelAndView mav=new ModelAndView("socket1");
		String random = RandomUtil.randomNumbers(6);
		System.out.println("随机数="+ random);
		mav.addObject("uid", random);
		return mav;
	}
}
