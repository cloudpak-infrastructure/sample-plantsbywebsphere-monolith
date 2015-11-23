/*******************************************************************************
 * Copyright (c) 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ibm.websphere.samples.pbw.migration;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Filter configuration to intercept calls for images either from
 * - JSF templates / libraries
 * - Direct references to the ImageServlet
 */
@WebFilter(filterName="RedirectFilter", 
			servletNames={"com.ibm.websphere.samples.pbw.war.ImageServlet", "FacesServlet"},
			urlPatterns="*",
			initParams={@WebInitParam(name="PBW_SERVICES_IMAGE", value="http://localhost:9081")})
public class RedirectionFilter implements Filter {
	private static final String IMAGE_SERVICE_CONFIG = "PBW_SERVICES_IMAGE";
	private String IMAGE_SERVICE = null;

	@Override
	public void destroy() {
		// clean up		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(response.isCommitted() || (IMAGE_SERVICE == null)) {
			//can't do anything as the response has already been committed
			chain.doFilter(request, response);
			return;
		}
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		if("ln=images".equals(req.getQueryString())) {
			String path = req.getServletPath();
			if(path.endsWith(".jsf")) {
				String resource = path.substring(path.lastIndexOf('/'), path.lastIndexOf('.'));
				resp.sendRedirect(IMAGE_SERVICE + "/images/resources" + resource);
				return;
			}
		}
		if(req.getServletPath().endsWith("/ImageServlet")) {
			resp.sendRedirect(IMAGE_SERVICE + "/images/product/inventory/" + req.getParameterMap().get("inventoryID")[0]);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		//read config for the server in order of preference
		try {
			InitialContext ctx = new InitialContext();
			IMAGE_SERVICE = ctx.lookup("java:comp/env/services/images").toString();
		} catch (NamingException e) {
			System.out.println("Unable to get value from JNDI, falling back to system / env properties");
		}
		IMAGE_SERVICE = System.getProperty(IMAGE_SERVICE_CONFIG.replace('_',  '.'), System.getenv(IMAGE_SERVICE_CONFIG));
		if(IMAGE_SERVICE != null) {
			return;
		}
		System.out.println("Unable to get value from system / env properties, falling back to filter config");
		IMAGE_SERVICE = config.getInitParameter(IMAGE_SERVICE_CONFIG);
	}

}
