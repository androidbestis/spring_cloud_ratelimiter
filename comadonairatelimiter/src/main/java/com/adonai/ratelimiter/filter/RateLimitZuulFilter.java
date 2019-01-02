package com.adonai.ratelimiter.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;

/**
//在Zuul上实现限流只需要编写一个过滤器即可,关键在于如何实现[限流算法]
//常见的限流算法为:
  //1. 漏桶算法
  //2. 令牌桶算法
  @see <a href="https://www.cnblogs.com/LBSer/p/4083131.html">限流算法</a>
*/
//Google Guava 为我们提供了限流工具类RateLimiter
public class RateLimitZuulFilter extends ZuulFilter {

    //使用[令牌桶]算法
    //每秒只发出5个令牌
    private final RateLimiter rateLimiter = RateLimiter.create(10.0);


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public boolean shouldFilter() {
        // 这里可以考虑弄个限流开启的开关，开启限流返回true，关闭限流返回false，你懂的。
        return true;
    }

    @Override
    public Object run() {
      try{
          RequestContext currentContext = RequestContext.getCurrentContext();
          HttpServletResponse response = currentContext.getResponse();

          //尝试获取令牌
          if(!rateLimiter.tryAcquire()){
              HttpStatus httpStatus = HttpStatus.TOO_MANY_REQUESTS;
              response.setContentType(MediaType.TEXT_PLAIN_VALUE);
              response.setStatus(httpStatus.value());
              response.getWriter().append(httpStatus.getReasonPhrase());
              currentContext.setSendZuulResponse(false);
              throw new ZuulException(
                      httpStatus.getReasonPhrase(),
                      httpStatus.value(),
                      httpStatus.getReasonPhrase()
              );
          }
      }catch (Exception e){
          ReflectionUtils.rethrowRuntimeException(e);
      }
        return null;
    }
}
