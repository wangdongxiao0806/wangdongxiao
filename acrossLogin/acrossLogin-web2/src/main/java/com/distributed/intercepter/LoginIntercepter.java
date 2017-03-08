package com.distributed.intercepter;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.distributed.util.Constants;
import com.distributed.util.CookieHelper;
import com.distributed.util.RedisService;

public class LoginIntercepter implements HandlerInterceptor{

	@Resource
	private RedisService redisService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView model)
			throws Exception {
		if (model != null) {
			Cookie cookie = CookieHelper.getCookieByName(request, Constants.COOKIE_NAME);
			String key = cookie.getValue();
			String value = redisService.getValue(key);
			model.addObject("username", value);
		}
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		Cookie cookie = CookieHelper.getCookieByName(request, Constants.COOKIE_NAME);
		if(cookie == null){
			dispose(request, response);
			return false;
		}
		String key = cookie.getValue();
		String value = redisService.getValue(key);
		if(StringUtils.isEmpty(value)){
			dispose(request, response);
			return false;
		}
		return true;
	}
	/**
	 * 
	 * <p>Discription:[无token跳转登录页面]</p>
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author:[]
	 */
	private void dispose(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(isAjaxRequest(request)){
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(600);
			response.getWriter().write("");
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration enumeration = request.getParameterNames();
			String name;
			String[] values;
			StringBuffer url = new StringBuffer();
			url.append(request.getRequestURI());
			url.append("?");
			while (enumeration.hasMoreElements()) {
				name = (String) enumeration.nextElement();
				values = request.getParameterValues(name);
				for (int i = 0; i < values.length; i++) {
					url.append(name);
					url.append("=");
					url.append(values[i]);
					url.append("&");
				}
			}
            String urlStr = url.toString();
			request.setAttribute("url", urlStr);
			request.getRequestDispatcher("/toLogin").forward(request, response);
		}
	}
	/**
	 * <p>Discription:判断是否为Ajax请求</p>
	 * Created on 2015年3月20日
	 * @param request
	 * @return 是true, 否false
	 * @author:胡恒心
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
	    String requestType = request.getHeader("X-Requested-With");
	    if (requestType != null && requestType.equals("XMLHttpRequest")) {
	        return true;
	    } else {
	        return false;
	    }
	}
}
