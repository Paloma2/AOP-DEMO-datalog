package com.log.aop;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.log.dao.OperationDao;
import com.log.pojo.LogEntity;

@Aspect
@Component
public class OperationlogAspect {

    @Resource
    OperationDao operationDao;
    public OperationlogAspect(){
    	System.out.println("****************OperationlogAspect****************");
    }
    
	@Pointcut("execution(public * com.product.dao.*.insert*(..))")
	public void insert() {
	}

	@Pointcut("execution(public * com.product.dao.*.delete*(..))")
	public void delete() {
	}

	@Pointcut("execution(public * com.product.dao.*.update*(..))")
	public void update() {
	}

	@Around("insert() || delete() || update()")
	public Object addOperateLog(ProceedingJoinPoint pjp) throws Throwable {
    	System.out.println("****************addOperateLog****************");
		LogEntity log = new LogEntity();

		// 获取登录用户账户
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		String userId = getUserId(request);
		String userName = getUserName(request);
		log.setUserId(userId);
		log.setUserName(userName);
        //获取系统时间
        String occur_time = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        log.setOccur_time(occur_time);
        
      //获取系统ip
        String ip =getIpAddress(request);
        log.setIp(ip);

        //方法通知前获取时间
         long start = System.currentTimeMillis();

         // 拦截的实体类
         Object target = pjp.getTarget();
         // 拦截的方法名称。当前正在执行的方法
         String methodName = pjp.getSignature().getName();
         // 拦截的放参数类型
         Signature sig = pjp.getSignature();
         MethodSignature msig = null;
         if (!(sig instanceof MethodSignature)) {
             throw new IllegalArgumentException("该注解只能用于方法");
         }
         msig = (MethodSignature) sig;
         Class[] parameterTypes = msig.getMethod().getParameterTypes();
         Object object = null;
         // 获得被拦截的方法
         Method method = null;
         try {
             method = target.getClass().getMethod(methodName, parameterTypes);
         } catch (NoSuchMethodException e1) {
             e1.printStackTrace();
         } catch (SecurityException e1) {
             e1.printStackTrace();
         }
         if (null != method) {
             // 判断是否包含自定义的注解，说明一下这里的SystemLog就是我自己自定义的注解
             if (method.isAnnotationPresent(SystemLog.class)) {
                 SystemLog systemlog = method.getAnnotation(SystemLog.class);
                 log.setModule(systemlog.module());
                 log.setMethod(systemlog.methods());
                 try {
                     object = pjp.proceed();
                     long end = System.currentTimeMillis();
                     //将计算好的时间保存在实体中
                     log.setRsponse_time(""+(end-start));
                     int result = (Integer) object;
                     if(result >0){
                         log.setCommite( " 执行成功！");
                     }
                     else{
                         log.setCommite( " 执行失败！");
                     }
                     //保存进数据库
                     operationDao.insert(log);
                     System.out.println("save:" + log.toString());
                 } catch (Throwable e) {
                     long end = System.currentTimeMillis();
                     log.setRsponse_time(""+(end-start));
                     log.setCommite(" 执行失败");
                     operationDao.insert(log);
                     System.out.println("save:" + log.toString());
                 }
             } else {//没有包含注解
                 System.out.println("*******没有包含注解,直接执行***********");
                 object = pjp.proceed();
             }
         } else { //不需要拦截直接执行
             System.out.println("*******不需要拦截直接执行***********");
             object = pjp.proceed();
         }
         return object;
	}

	private String getUserId(HttpServletRequest request) {
		Assertion assertion = (Assertion) request.getSession().getAttribute(
				AbstractCasFilter.CONST_CAS_ASSERTION);
		String userId = null;
		if (null != assertion) {
			AttributePrincipal principal = assertion.getPrincipal();
			Map<String, Object> attributes = principal.getAttributes();
			Object loginUserId = attributes.get("userId");
			if (loginUserId != null) {
				userId = loginUserId.toString();
			}
		}
		return userId;
	}
	
	private String getUserName(HttpServletRequest request) {
		Assertion assertion = (Assertion) request.getSession().getAttribute(
				AbstractCasFilter.CONST_CAS_ASSERTION);
		String userId = null;
		if (null != assertion) {
			AttributePrincipal principal = assertion.getPrincipal();
			Map<String, Object> attributes = principal.getAttributes();
			Object loginUserId = attributes.get("userId");
			if (loginUserId != null) {
				userId = loginUserId.toString();
			}
		}
		return userId;
	}
	
	private String getIpAddress(HttpServletRequest request){
	    
	    String ipAddress = request.getHeader("x-forwarded-for");
	    
	    if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getRemoteAddr();
	        
	        if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
	            //根据网卡获取本机配置的IP地址
	            InetAddress inetAddress = null;
	            try {
	                inetAddress = InetAddress.getLocalHost();
	            } catch (UnknownHostException e) {
	                e.printStackTrace();
	            }
	            ipAddress = inetAddress.getHostAddress();
	        }
	    }
	    
	    //对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割
	    if(null != ipAddress && ipAddress.length() > 15){
	        //"***.***.***.***".length() = 15
	        if(ipAddress.indexOf(",") > 0){
	            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
	        }
	    }
	    
	    return ipAddress;
	}
}
