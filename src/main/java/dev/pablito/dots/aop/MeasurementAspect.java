package dev.pablito.dots.aop;

import dev.pablito.dots.controller.OrderController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MeasurementAspect {

    @Around("@annotation(Timed)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String label = this.getLabel(joinPoint.getTarget().getClass().getName());
        String arguments = this.getArguments(joinPoint);

        logger.info("["+label+" START] "+joinPoint.getSignature().getName()+"("+arguments+")");
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.info("["+label+" END] "+joinPoint.getSignature().getName() + " - " + executionTime + "ms");
        return proceed;
    }

    private String getLabel(String className) {
        if (className.contains("controller")) return "TASK";
        else if (className.contains("scheduler")) return "SCHEDULED";
        else if (className.contains("service")) return "SERVICE";
        else return "";
    }

    private String getArguments (ProceedingJoinPoint joinPoint) {
        if (joinPoint.getArgs() == null)
            return "";

        String arguments = "";
        Object[] signatureArgs = joinPoint.getArgs();
        for (Object signatureArg : signatureArgs) {
            arguments += signatureArg.toString() + " ";
        }

        return arguments.trim();
    }

    /*
2025-02-22 13:38:54 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED START] checkOrdersInDiscogs()
2025-02-22 13:38:55 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED END] checkOrdersInDiscogs() - 1 s
2025-02-22 13:39:00 [http-nio-8081-exec-1] INFO  d.p.dots.controller.OrderController - [TASK START] getUnarchivedOrders(0, 50)
2025-02-22 13:39:00 [http-nio-8081-exec-1] INFO  d.p.dots.controller.OrderController - [TASK END] getUnarchivedOrders(0, 50) - 0 s
2025-02-22 13:39:00 [http-nio-8081-exec-1] INFO  d.p.dots.controller.OrderController - OrderController.getUnarchivedOrders(..) executed in 190ms
2025-02-22 13:39:54 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED START] checkOrdersInDiscogs()
2025-02-22 13:39:54 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED END] checkOrdersInDiscogs() - 0 s
2025-02-22 13:40:54 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED START] checkOrdersInDiscogs()
2025-02-22 13:40:54 [scheduling-1] INFO  d.p.dots.scheduler.OrderScheduler - [SCHEDULED END] checkOrdersInDiscogs() - 0 s

     */
}