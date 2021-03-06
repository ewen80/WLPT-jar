package pw.ewen.WLPT.core.services;

import pw.ewen.WLPT.core.domains.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pw.ewen.WLPT.core.exceptions.domain.DeleteHaveUsersRoleException;
import pw.ewen.WLPT.core.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wenliang on 17-7-17.
 */
@Service("roleService")
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * 删除角色,如果角色下有用户则不允许删除,即便有一个有用户则整批都不删除
     * @param ids
     */
    public void delete(Collection<Long> ids){
        //检查角色下是否有用户，有则不允许删除
        List<Role> roles = this.roleRepository.findAll(ids);
        for(Role role: roles){
            if(!role.getUsers().isEmpty()){
                //角色下面有用户
                throw new DeleteHaveUsersRoleException(role.getRoleId());
            }
        }

        this.roleRepository.delete(roles);
    }

    public Role save(Role role){
        return this.roleRepository.save(role);
    }

    public Role getOneRole(String roleId){
        return this.roleRepository.findByroleId(roleId);
    }

    public Page<Role> getRolesWithPage(int pageIndex, int pageSize){
        return  this.roleRepository.findAll(new PageRequest(pageIndex, pageSize, new Sort(Sort.Direction.ASC, "name")));
    }

    public List<Role> getAllRoles(){
        return this.roleRepository.findAll();
    }

    public Collection<Role> findByIds(String ids){
        String[] arrIds = ids.split(",");
        ArrayList<Role> arrRoles = new ArrayList<>();
        for(String id : arrIds){
            Role role = this.roleRepository.findOne(Long.valueOf(id));
            arrRoles.add(role);
        }
        return arrRoles;
    }
}
