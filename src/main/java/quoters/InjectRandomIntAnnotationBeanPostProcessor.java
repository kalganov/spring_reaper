package quoters;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;
import sun.reflect.misc.ReflectUtil;

import java.util.Random;
import java.util.stream.Stream;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Stream.of(bean.getClass().getDeclaredFields())
                .filter(field -> field.getAnnotation(InjectRandomInt.class) != null)
                .forEach(field -> {
                    InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
                    int min = annotation.min();
                    int max = annotation.max();
                    Random random = new Random();
                    int i = min + random.nextInt(max - min);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field,bean,i);
                });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
