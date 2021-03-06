package pw.ewen.WLPT.core.aops;


import pw.ewen.WLPT.core.domains.entities.ResourceType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.annotation.Cacheable;
import pw.ewen.WLPT.core.services.ResourceTypeService;

/**
 * Created by wenliang on 17-7-5.
 * Resource资源创建AOP切面
 * 在资源创建前判断该ResourceType是否已经保存，如果没有则保存
 */
@Aspect
public class ResourceTypeAspect {

    private ResourceTypeService resourceTypeService;

    public void setResourceTypeService(ResourceTypeService resourceTypeService) {
        this.resourceTypeService = resourceTypeService;
    }

    @Pointcut("execution(pw.ewen.WLPT.core.domains.Resource+.new(..))")
    private void resourceConstructor(){}

    @Pointcut("!cflow(execution(* pw.ewen.WLPT.core.aops.ResourceTypeAspect.initialResourceTypeInDB(..)))")
    private void noRecursive(){}

    @Pointcut("!this(pw.ewen.WLPT.core.domains.NeverMatchedResourceRange)")
    private void excludeResourceType(){}


    @Before("resourceConstructor() && noRecursive() && excludeResourceType()")
    public void saveResourceTypeInDB(JoinPoint joinPoint){

        String resourceClassName = joinPoint.getTarget().getClass().getCanonicalName();
        String resourceName = joinPoint.getTarget().getClass().getSimpleName();

        if(this.resourceTypeService != null){
            //判断系统中是否已经存在ResourceType,不存在则添加
            this.initialResourceTypeInDB(resourceName, resourceClassName);
        }
    }

    /**
     * 数据库中保存ResourceType信息
     * @param resourceTypeClassName 资源类的全限定名
     * @return
     */
    @Cacheable("resourceTypeInDBCache")
    public void initialResourceTypeInDB(String resourceTypeName, String resourceTypeClassName){
        ResourceType resourceType = this.resourceTypeService.findByClassName(resourceTypeClassName);
        if(resourceType == null){
            ResourceType newResourceType = new ResourceType(resourceTypeClassName, resourceTypeName);
            this.resourceTypeService.save(newResourceType);
        }
//        return true;
    }
}
