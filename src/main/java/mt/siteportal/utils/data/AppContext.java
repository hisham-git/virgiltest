package mt.siteportal.utils.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring's app context singleton.
 * Here http://javapostsforlearning.blogspot.com/2012/08/spring-applicationcontext.html could be something useful about that.
 */
public abstract class AppContext {
    private static final Logger logger = LoggerFactory.getLogger(AppContext.class);

    private static AbstractApplicationContext ctx;

    public static AbstractApplicationContext getContext()
    {
        if (ctx == null){
            try {
                ctx = new ClassPathXmlApplicationContext("smoke_tests.config.xml");

                System.out.println(new java.io.File("smoke_tests.config.xml").getAbsolutePath());
            } catch (BeansException e) {
                logger.error("Bean exception: ", e);
            }
        }

        return ctx;
    }
}