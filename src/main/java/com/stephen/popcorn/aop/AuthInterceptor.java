package com.stephen.popcorn.aop;

import com.stephen.popcorn.annotation.AuthCheck;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.enums.UserRoleEnum;
import com.stephen.popcorn.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验 AOP
 *
 * @author stephen qiu
 */
@Aspect
@Component
public class AuthInterceptor {
	
	@Resource
	private UserService userService;
	
	/**
	 * 执行拦截
	 *
	 * @param joinPoint joinPoint
	 * @param authCheck authCheck
	 * @return Object
	 */
	@Around("@annotation(authCheck)")
	public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
		String mustRole = authCheck.mustRole();
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		User loginUser = userService.getLoginUser(request);
		// 必须有该权限才通过
		if (StringUtils.isNotBlank(mustRole)) {
			UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
			if (mustUserRoleEnum == null) {
				throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
			}
			String userRole = loginUser.getUserRole();
			// 如果被封号，直接拒绝
			if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
				throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
			}
			// 必须有管理员权限
			if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
				if (!mustRole.equals(userRole)) {
					throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
				}
			}
		}
		// 通过权限校验，放行
		return joinPoint.proceed();
	}
}

