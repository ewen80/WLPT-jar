package pw.ewen.WLPT.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;
import pw.ewen.WLPT.core.domains.entities.Role;
import pw.ewen.WLPT.core.domains.entities.User;
import pw.ewen.WLPT.core.exceptions.domain.DeleteHaveUsersRoleException;
import pw.ewen.WLPT.core.repositories.RoleRepository;
import pw.ewen.WLPT.core.services.RoleService;

import java.util.List;


@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;
    /**
     * 获取全部角色
     * @return
     */
    @RequestMapping(value="/all", method=RequestMethod.GET, produces="application/json")
    @PostFilter("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(filterObject, 'read')")
    public List<Role> getAllRoles(){
        return  this.roleService.getAllRoles();
    }

//    /**
//     * 获取角色（分页）
//     * @param pageIndex 第几页
//     * @param pageSize  每页多少条
//     * @return 角色数据
//     */
//    @RequestMapping(method = RequestMethod.GET, produces="application/json")
//    public Page<Role> getRolesWithPage(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
//                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
//        return this.roleService.getRolesWithPage(pageIndex, pageSize);
//    }

    /**
     * 获取一个角色
     * @param roleId 角色Id
     * @return  角色数据
     */
    @RequestMapping(value="/{roleId}", method=RequestMethod.GET, produces="application/json")
    @PostAuthorize("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(returnObject, 'read')")
    public Role getOneRole(@PathVariable("roleId") String roleId){
        return this.roleService.getOneRole(roleId);
    }

    /**
     * 保存角色信息
     * @param role  角色数据
     * @return  保存的角色数据
     */
    @RequestMapping(method=RequestMethod.POST, produces = "application/json")
    @PreAuthorize("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(#role, 'write')")
    public Role save(@RequestBody Role role){
        return this.roleService.save(role);
    }

    /**
     * 删除角色,如果有一个角色下有用户则整批角色不允许删除
     * @param roleIds   角色roleIds
     */
    @RequestMapping(value = "/{ids}", method=RequestMethod.DELETE, produces = "application/json")
    @PreFilter("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) ||  hasPermission(@roleService.findByIds(#ids), 'write')")
    public void delete(@PathVariable("ids") String roleIds){
//        String[] arrRoleIds = roleIds.split(",");
//        ArrayList<Long> longIds = new ArrayList<>();
//        for(String roleId: arrRoleIds){
//            longIds.add(Long.valueOf(id));
//        }
//        this.roleService.delete(longIds);

    }
}
