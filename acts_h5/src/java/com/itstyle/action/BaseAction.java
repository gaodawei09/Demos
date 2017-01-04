package com.itstyle.action;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Transient log to prevent session synchronization issues - children can
	 * use instance for logging.
	 */
	protected transient final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Convenience method to get the request
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * Convenience method to get the response
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/**
	 * Convenience method to get the session. This will create a session if one
	 * doesn't exist.
	 * 
	 * @return the session from the request (request.getSession()).
	 */
	protected HttpSession getSession() {
		return getRequest().getSession();
	}
	/**
	 * 将结果返回给xmlRequest
	 * @Author	张志朋
	 * @param result
	 * @throws Exception  void
	 * @Date	2015年5月8日
	 * 更新日志
	 * 2015年5月8日 张志朋  首次创建
	 *
	 */
	public void printMsgToClient(String result) throws Exception {
		getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = getResponse().getWriter();
		try {
			out.print(result);
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
}
