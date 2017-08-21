package pw.ewen.WLPT.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import domains.entities.ResourceRange;
import pw.ewen.WLPT.core.repositories.ResourceRangeRepository;
import pw.ewen.WLPT.core.repositories.ResourceTypeRepository;
import pw.ewen.WLPT.core.repositories.RoleRepository;
import pw.ewen.WLPT.core.repositories.specifications.ResourceRangeSpecificationBuilder;

import java.util.Collection;
import java.util.List;

/**
 * Created by wenliang on 17-4-12.
 */
@Service
public class ResourceRangeService {

    private ResourceRangeRepository resourceRangeRepository;
    private RoleRepository roleRepository;
    private ResourceTypeRepository resourceTypeRepository;
    private PermissionService permissionService;

    @Autowired
    public ResourceRangeService(ResourceRangeRepository resourceRangeRepository,
                                RoleRepository roleRepository,
                                ResourceTypeRepository resourceTypeRepository,
                                PermissionService permissionService) {
        this.resourceRangeRepository = resourceRangeRepository;
        this.roleRepository = roleRepository;
        this.resourceTypeRepository = resourceTypeRepository;
        this.permissionService = permissionService;
    }

//    public List<ResourceRange> getByResourceType(String resourceTypeClassName){
//        Assert.hasText(resourceTypeClassName);
//
//        ResourceRangeSpecificationBuilder builder = new ResourceRangeSpecificationBuilder();
//        return this.resourceRangeRepository.findAll(builder.build("resourceType.className:"+resourceTypeClassName));
//    }

    public List<ResourceRange> getByResourceType(Long resourceTypeId){
        Assert.notNull(resourceTypeId);

        ResourceRangeSpecificationBuilder builder = new ResourceRangeSpecificationBuilder();
        return this.resourceRangeRepository.findAll(builder.build("resourceType.id:"+resourceTypeId));
    }

    public ResourceRange findOne(Long id){
        return this.resourceRangeRepository.findOne(id);
    }

    public ResourceRange save(ResourceRange range){
        return this.resourceRangeRepository.save(range);
    }

    public void delete(Collection<Long> ids){
        for(Long id : ids){
            this.resourceRangeRepository.delete(id);
            //删除ACL权限
            this.permissionService.deleteResourceRangesAllPermissions(ids, true);
        }
    }
}
