package pw.ewen.WLPT.core.configs.cache;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

/**
 * Created by wenliang on 17-4-19.
 * 系统缓存配置
 */
@Configuration
@EnableCaching(mode = AdviceMode.ASPECTJ)
public class EhcacheConfig {

    @Bean
    EhCacheBasedAclCache getAclCache(CacheManager manager, PermissionGrantingStrategy permissionGrantingStrategy,
                                     AclAuthorizationStrategy aclAuthorizationStrategy){

        return new EhCacheBasedAclCache(manager.getCache("aclCache"), permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    /**
     * EhCache的EhCacheManager
     * @return
     */
    @Bean
    EhCacheManagerFactoryBean getEhCacheManager(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean =  new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setAcceptExisting(true);//此处不设置true，test会出现错误：spring testing: Another CacheManager with same name 'myCacheManager' already exists in the same VM
        return cacheManagerFactoryBean;
    }

    /**
     * Spring的ehCacheManager
     * @param cm
     * @return
     */
    @Bean
    EhCacheCacheManager getCacheManager(CacheManager cm){
        return new EhCacheCacheManager(cm);
    }
}
