package pw.ewen.WLPT.acl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pw.ewen.WLPT.annotations.WithAdminUser;
import pw.ewen.WLPT.domains.NeverMatchedResourceRange;
import pw.ewen.WLPT.domains.entities.*;
import pw.ewen.WLPT.repositories.*;
import pw.ewen.WLPT.security.UserContext;
import pw.ewen.WLPT.security.acl.ObjectIdentityRetrievalStrategyWLPTImpl;
import pw.ewen.WLPT.services.PermissionService;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by wenliang on 17-3-3.
 * 测试ACL中获取ObjectIdentity的方法
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@WithAdminUser
public class ObjectIdentityRetrievalStrategyWLPTImplTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MyResourceRepository myResourceRepository;
    @Autowired
    private ResourceRangeRepository resourceRangeRepository;
    @Autowired
    private ResourceTypeRepository resourceTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectIdentityRetrievalStrategyWLPTImpl objectIdentityRetrieval;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserContext userContext;

    private Role role1;
    private User user1;
    private GrantedAuthoritySid sid1;

    @Before
    public  void setup(){
        role1 = new Role("role1", "role1");
        roleRepository.save(role1);
        user1 = new User("user1", "user1", "user1", role1);
        userRepository.save(user1);

        sid1 = new GrantedAuthoritySid(role1.getRoleId());

        ResourceType rt = new ResourceType(MyResource.class.getName(), "MyResource", "");
        resourceTypeRepository.save(rt);

    }

    /**
     * 测试角色对指定资源没有访问权时，从资源获取资源范围策略是否正确
     */
    @Test
    public void haveNoPermissionToResource(){
        MyResource resource = new MyResource(100);
        myResourceRepository.save(resource);

        ObjectIdentity oi = objectIdentityRetrieval.getObjectIdentity(resource);
        ResourceRange neverMatchedRange = new NeverMatchedResourceRange();
        assertThat(oi).isEqualTo(new ObjectIdentityImpl(neverMatchedRange));
    }

    /**
     * 测试角色对指定资源有全部访问权时，从资源获取资源范围策略是否正确
     */
    @Test
    public void haveAllPermissionToResource() throws Exception{
        //全匹配范围
        MyResource resource = new MyResource(200);

        ResourceRange matchAllResourceRange = new ResourceRange();
        matchAllResourceRange.setRole(this.userContext.getCurrentUser().getRole());
        matchAllResourceRange.setResourceType(ResourceType.getFromResource(resource, resourceTypeRepository));
        matchAllResourceRange.setMatchAll(true);
        matchAllResourceRange = resourceRangeRepository.save(matchAllResourceRange);

//        GrantedAuthoritySid adminSid = new GrantedAuthoritySid("admin");
        permissionService.insertPermission(matchAllResourceRange.getId(), BasePermission.READ);

        ObjectIdentity oi = objectIdentityRetrieval.getObjectIdentity(resource);

        assertThat(oi).isEqualTo(new ObjectIdentityImpl(matchAllResourceRange));
    }

    /**
     * 测试角色对指定资源有部分访问权时，从资源获取资源范围策略是否正确
     */
    @Test
    public void havePartPermissionToResource(){
        MyResource resource100 = new MyResource(100);
        myResourceRepository.save(resource100);
        MyResource resource200 = new MyResource(200);
        myResourceRepository.save(resource200);

//        ResourceType rt = resourceTypeRepository.findByClassName(resource100.getClass().getTypeName());

        ResourceRange rr_less_than_150 = new ResourceRange("number < 150", this.userContext.getCurrentUser().getRole(), ResourceType.getFromResource(resource100,resourceTypeRepository));
        resourceRangeRepository.save(rr_less_than_150);
        ResourceRange rr_more_than_150 = new ResourceRange("number > 150",this.userContext.getCurrentUser().getRole(), ResourceType.getFromResource(resource200,resourceTypeRepository));
        resourceRangeRepository.save(rr_more_than_150);

        ObjectIdentity oi1 = objectIdentityRetrieval.getObjectIdentity(resource100);
        assertThat(oi1).isEqualTo(new ObjectIdentityImpl(rr_less_than_150));

        ObjectIdentity oi2 = objectIdentityRetrieval.getObjectIdentity(resource200);
        assertThat(oi2).isEqualTo(new ObjectIdentityImpl(rr_more_than_150));
    }

//    private void executeSqlScript(String scriptPath, DataSource dataSource){
//        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//        resourceDatabasePopulator.addScript(new ClassPathResource(scriptPath));
//        DatabasePopulatorUtils.execute(resourceDatabasePopulator , dataSource);
//    }
}
