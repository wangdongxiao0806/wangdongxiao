package com.distributed.controller;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.distributed.util.Constants;
import com.distributed.util.CookieHelper;
import com.distributed.util.RedisService;

@Controller
public class LoginController {

	@Resource
	private RedisService redisService;
	@RequestMapping(value="/index")
	public String index(){
		return "/index";
	}
	@RequestMapping(value="/toLogin")
	public String toLogin(){
		return "/login";
	}
	@RequestMapping(value="/login")
	public String login(String username,String password,HttpServletRequest request, HttpServletResponse response,Map<String,Object> map){
		System.out.println("username:"+username+";password:"+password);
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
			return "/login";
		}
		String uuid = UUID.randomUUID().toString();
		CookieHelper.addCookie(Constants.COOKIE_NAME, uuid, "web.com", "/", 3600, response);
		redisService.setValue(uuid, username+password);
		return "redirect:/index";
	}
	
	@RequestMapping(value="logout")
	public String lougout(HttpServletRequest request, HttpServletResponse response){
		Cookie cookie = CookieHelper.getCookieByName(request, Constants.COOKIE_NAME);
		String key  = cookie.getValue();
		redisService.delValue(key);
		return "/login";
	}
}
