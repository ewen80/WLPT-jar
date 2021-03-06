package pw.ewen.WLPT.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pw.ewen.WLPT.core.domains.DTOs.permissions.PermissionDTO;
import pw.ewen.WLPT.core.domains.DTOs.permissions.ResourceRangePermissionWrapperDTO;
import pw.ewen.WLPT.core.domains.ResourceRangePermissionWrapper;
import pw.ewen.WLPT.core.exceptions.domain.PermissionNotFoundException;
import pw.ewen.WLPT.core.repositories.ResourceRangeRepository;
import pw.ewen.WLPT.core.services.PermissionService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by wen on 17-3-5.
 * 权限控制Controller
 * 对Permission的操作权限对应相应的ResourceRange的权限
 */
@RestController
@RequestMapping(value = "/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ResourceRangeRepository resourceRangeRepository;

    public static final Permission[] SUPPORT_PERMISSIONS = { BasePermission.READ, BasePermission.WRITE };


    /**
     * 获取一个或者多个ResourceRange权限
     * 多个ResourceRange用,分割
     */
    @RequestMapping(value = "/{resourceRangeIds}", method = RequestMethod.GET, produces = "application/json")
    @PostFilter("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(filterObject.convertToPermissionWrapper(@resourceRangeRepository).getResourceRange(), 'read')")
    public Set<ResourceRangePermissionWrapperDTO> getByResourceRanges(@PathVariable("resourceRangeIds") String resourceRangeIds) throws PermissionNotFoundException,IllegalArgumentException{
        Set<ResourceRangePermissionWrapperDTO> wrappers = new HashSet<>();

        String[] arrResourceRangeIds = resourceRangeIds.split(",");

        for(String id : arrResourceRangeIds){
            try{
                long resourceRangeId = Long.valueOf(id);
                ResourceRangePermissionWrapper wrapper = this.permissionService.getByResourceRange(resourceRangeId);
                if(wrapper != null) {
                    ResourceRangePermissionWrapperDTO dto = ResourceRangePermissionWrapperDTO.convertFromPermissionWrapper(wrapper);
                    wrappers.add(dto);
                }
            }catch(NumberFormatException e){
                throw new IllegalArgumentException("ResourceRangeId必须是数字");
            }
        }
        return wrappers;
    }

    /**
     * 保存权限
     * @return int  插入的权限记录数
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(#wrapperDTO.convertToPermissionWrapper(@resourceRangeRepository).getResourceRange(), 'write')")
    @Transactional
    public int save(@RequestBody ResourceRangePermissionWrapperDTO wrapperDTO) {

        this.permissionService.deleteResourceRangeAllPermissions(wrapperDTO.getResourceRangeId(), true);

        int insertNumber = 0;

        for (PermissionDTO pDTO : wrapperDTO.getPermissions()) {
            Optional<Permission> permission = Arrays.stream(SUPPORT_PERMISSIONS)
                    .filter(pm -> pm.getMask() == pDTO.getMask())
                    .findFirst();
            if(permission.isPresent()) {
                try {
                    this.permissionService.insertPermission(wrapperDTO.getResourceRangeId(), permission.get());
                    insertNumber++;
                } catch (Exception e ) {

                }
            }
        }
        return insertNumber;
    }
}
