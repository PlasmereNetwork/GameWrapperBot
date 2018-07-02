package info.addisoncrump.oklahoma.bot.aspect;

import lombok.NonNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Aspect
public class EventLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger("EventTrace");

    @Pointcut("@annotation(org.springframework.context.event.EventListener) && !@annotation(info.addisoncrump.oklahoma.bot.aspect.ExcludeFromLogging)")
    public void executeLogging() {
    }

    @Around("executeLogging()")
    public Object logMethod(final @NonNull ProceedingJoinPoint joinPoint) {
        Object object = null;
        long time = System.nanoTime();
        try {
            object = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.warn(
                    String.format(
                            "Event handling failed for event type %s",
                            joinPoint.getArgs()[0].getClass().getSimpleName()
                    ),
                    throwable
            );
        }
        time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time);
        logger.info(String.format(
                "%s(%s) finished in %dms",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs()[0],
                time
        ));
        return object;
    }
}
