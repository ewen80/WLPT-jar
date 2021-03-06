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
import pw.ewen.WLPT.core.domains.entities.ResourceType;
import pw.ewen.WLPT.core.repositories.ResourceTypeRepository;
import pw.ewen.WLPT.core.repositories.specifications.ResourceTypeSpecificationBuilder;
import pw.ewen.WLPT.core.services.ResourceTypeService;

import java.util.Collection;

/**
 * Created by wen on 17-3-12.
 */
@RestController
@RequestMapping(value = "/resourcetypes")
public class ResourceTypeController {

    @Autowired
    private ResourceTypeRepository resourceTypeRepository;
    @Autowired
    private ResourceTypeService resourceTypeService;

//    /**
//     * 获取资源类型（分页，查询）
//     * @param pageIndex 第几页
//     * @param pageSize  每页多少条
//     */
//    public Page<ResourceType> getResourcesWithPage(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
//                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                                   @RequestParam(value = "filter", defaultValue = "") String filter){
//       return this.resourceTypeService.getResourcesWithPage(pageIndex, pageSize, filter);
//    }

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    @PostFilter("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(filterObject, 'read')")
    public Collection<ResourceType> getResources(@RequestParam(value = "filter", defaultValue = "") String filter){
        return  this.resourceTypeService.findAll(filter);
    }

    /**
     * 获取一个资源类型
     * @param className 资源类全限定名
     */
    @RequestMapping(value="/{className}", method=RequestMethod.GET, produces="application/json")
    @PostAuthorize("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(returnObject, 'read')")
    public ResourceType findByClassName(@PathVariable("className") String className){
        return this.resourceTypeService.findByClassName(className.replace('$','.'));
    }

    /**
     * 保存
     * @param resourceType 资源类型
     */
    @RequestMapping(method=RequestMethod.POST, produces = "application/json")
    @PreAuthorize("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(#resourceType, 'write')")
    public ResourceType save(@RequestBody ResourceType resourceType){
        return this.resourceTypeService.save(resourceType);
    }

    /**
     * 删除,如果有对应的ResourceRange则软删除
     */
    @RequestMapping(method=RequestMethod.DELETE, produces = "application/json")
    @PreFilter("hasAuthority(@propertyConfig.getDefaultAdminRoleId()) || hasPermission(@resourceTypeService.findOne(filterObject ), 'write')")
    public void delete(@RequestBody Collection<Long> ids){
        Collection<ResourceType> resourceTypes = this.resourceTypeService.findByIds(ids);
        this.resourceTypeService.delete(resourceTypes);
    }
}
