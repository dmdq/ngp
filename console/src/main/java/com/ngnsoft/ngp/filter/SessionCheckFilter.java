/**
 * created on 2010-1-12
 */
package com.ngnsoft.ngp.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.util.ConvertUtil;

/**
 * 会话检查过滤器
 * @author wangnan
 *
 */
public class SessionCheckFilter implements Filter {

	private FilterConfig config;
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String currentUrl = request.getRequestURI();
		String failUrl = ConvertUtil.nvl(config.getInitParameter("failUrl"));
		String logonUrl = ConvertUtil.nvl(config.getInitParameter("logonUrl"));
		String indexUrl = ConvertUtil.nvl(config.getInitParameter("indexUrl"));
		String logoutUrl = ConvertUtil.nvl(config.getInitParameter("logoutUrl"));
		boolean client = Boolean.valueOf(config.getInitParameter("client"));
		HttpSession session = request.getSession(false);
		if(currentUrl != null 
				&& currentUrl.indexOf(logonUrl) < 0 
				&& currentUrl.indexOf(logoutUrl) < 0
				&& currentUrl.indexOf(failUrl) < 0
				&& currentUrl.indexOf(indexUrl) < 0 ) {
			if(session == null || session.getAttribute("user") == null) {
				if(client) {
					User user = new User();
					user.setName("X用户");
					user.setNick("X昵称");
					session = request.getSession();
					session.setAttribute("user", user);
				} else {
					request.setAttribute("message", "会话已超时");
					request.getRequestDispatcher("/" + indexUrl).forward(request, response);
					return;
				}
				
			}
		}
		arg2.doFilter(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		this.config = arg0;
	}

}
