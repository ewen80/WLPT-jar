package pw.ewen.WLPT.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pw.ewen.WLPT.core.configs.property.PropertyConfig;
import pw.ewen.WLPT.core.domains.entities.*;
import pw.ewen.WLPT.core.repositories.*;
import pw.ewen.WLPT.core.services.PermissionService;

/**
 * Created by wen on 17-5-14.
 * 应用运行前的初始化
 */
@Component
public class ApplicationInitialization implements ApplicationRunner {

    @Autowired
    private PropertyConfig propertyConfig;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ResourceTypeRepository resourceTypeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResourceRangeRepository resourceRangeRepository;
    @Autowired
    private PermissionService permissionService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initialRolesAndUsers();
        this.initialMenu();
    }

    //初始化角色（默认为admin,guest）
    private void initialRoles(){
        String adminRoleId = propertyConfig.getDefaultAdminRoleId();
        String adminRoleName = propertyConfig.getDefaultAdminRoleName();

        String guestRoleId = propertyConfig.getDefaultGuestUserId();
        String guestRoleName = propertyConfig.getDefaultGuestRoleName();

        //当前数据库中是否已經存在该角色
        if( this.roleRepository.findByroleId(adminRoleId) == null ){
            Role adminRole = new Role(adminRoleId,adminRoleName);
            this.roleRepository.save(adminRole);
        }

        if( this.roleRepository.findByroleId(guestRoleId) == null ){
            Role guestRole = new Role(guestRoleId,guestRoleName);
            this.roleRepository.save(guestRole);
        }
    }

    //初始化用户（默认为admin,guest）
    private void initialUsers(){
        String adminRoleId = propertyConfig.getDefaultAdminRoleId();
        String adminUserId = propertyConfig.getDefaultAdminUserId();

        //保存管理员用户
        Role adminRole = this.roleRepository.findByroleId(adminRoleId);
        User adminUser = this.userRepository.findByuserId(adminUserId);

        if(adminRole != null && adminUser == null){
            adminUser = new User(propertyConfig.getDefaultAdminUserId(),
                    propertyConfig.getDefaultAdminUserName(),
                    propertyConfig.getDefaultAdminUserPassword(),
                    adminRole);
            this.userRepository.save(adminUser);
        }
        //保存来宾客户
        Role guestRole = this.roleRepository.findByroleId(propertyConfig.getDefaultGuestRoleId());
        User guestUser = this.userRepository.findByuserId(propertyConfig.getDefaultGuestUserId());

        if(guestRole != null && guestUser == null){
            guestUser = new User(propertyConfig.getDefaultGuestUserId(),
                    propertyConfig.getDefaultGuestUserName(),
                    propertyConfig.getDefaultGuestUserPassword(),
                    guestRole);
            this.userRepository.save(guestUser);
        }
    }

    //初始化角色和用户
    private void initialRolesAndUsers(){
        this.initialRoles();
        this.initialUsers();
    }

    //初始化菜单数据
    private void initialMenu() {

        String homeMenuName = "Home";
        Menu homeMenu = this.menuRepository.findByName(homeMenuName);
        if(homeMenu == null){
            homeMenu = new Menu();
            homeMenu.setName("Home");
            homeMenu.setPath("/");
            menuRepository.save(homeMenu);
        }

        String adminMenuName = "后台管理";
        Menu adminMenu = this.menuRepository.findByName(adminMenuName);
        if(adminMenu == null){
            adminMenu = new Menu();
            adminMenu.setName("后台管理");
            menuRepository.save(adminMenu);
        }

        String usersAdminMenuName = "用户管理";
        Menu usersAdminMenu = this.menuRepository.findByName(usersAdminMenuName);
        if(usersAdminMenu == null){
            usersAdminMenu = new Menu();
            usersAdminMenu.setName("用户管理");
            usersAdminMenu.setPath("/admin/users");
            usersAdminMenu.setParent(adminMenu);
            menuRepository.save(usersAdminMenu);
        }

        String rolesAdminMenuName = "角色管理";
        Menu rolesAdminMenu = this.menuRepository.findByName(rolesAdminMenuName);
        if(rolesAdminMenu == null){
            rolesAdminMenu = new Menu();
            rolesAdminMenu.setName("角色管理");
            rolesAdminMenu.setPath("/admin/roles");
            rolesAdminMenu.setParent(adminMenu);
            menuRepository.save(rolesAdminMenu);
        }

        String resourcesAdminMenuName = "资源管理";
        Menu resourcesAdminMenu = this.menuRepository.findByName(resourcesAdminMenuName);
        if(resourcesAdminMenu == null){
            resourcesAdminMenu = new Menu();
            resourcesAdminMenu.setName("资源管理");
            resourcesAdminMenu.setPath("/admin/resources");
            resourcesAdminMenu.setParent(adminMenu);
            menuRepository.save(resourcesAdminMenu);
        }

        String menusAdminMenuName = "菜单管理";
        Menu menusAdminMenu = this.menuRepository.findByName(menusAdminMenuName);
        if(menusAdminMenu == null){
            menusAdminMenu = new Menu();
            menusAdminMenu.setName("菜单管理");
            menusAdminMenu.setPath("/admin/resources/menus");
            menusAdminMenu.setParent(adminMenu);
            menuRepository.save(menusAdminMenu);
        }

    }
}
