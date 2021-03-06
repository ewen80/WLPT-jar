package pw.ewen.WLPT.core.domains.DTOs;

import org.springframework.util.Assert;
import pw.ewen.WLPT.core.domains.entities.ResourceRange;
import pw.ewen.WLPT.core.domains.entities.ResourceType;
import pw.ewen.WLPT.core.domains.entities.Role;
import pw.ewen.WLPT.core.repositories.ResourceTypeRepository;
import pw.ewen.WLPT.core.repositories.RoleRepository;

import java.io.Serializable;

/**
 * Created by wen on 17-4-9.
 */
public class ResourceRangeDTO implements Serializable {
    private long id;
    private String filter;
    private long roleId;
    private String resourceTypeClassName;
    private boolean matchAll = false;

    //实现DTOConvert接口的内部类
    private static class ResourceRangeConverter implements DTOConvert<ResourceRangeDTO, ResourceRange>{

        private RoleRepository roleRepository;
        private ResourceTypeRepository resourceTypeRepository;

        public ResourceRangeConverter(){}

        public ResourceRangeConverter(RoleRepository roleRepository, ResourceTypeRepository resourceTypeRepository) {
            this.roleRepository = roleRepository;
            this.resourceTypeRepository = resourceTypeRepository;
        }

        @Override
        public ResourceRange doForward(ResourceRangeDTO dto) {
            Assert.notNull(this.roleRepository);
            Assert.notNull(this.resourceTypeRepository);

            ResourceRange range = new ResourceRange();
            range.setId(dto.getId());
            range.setFilter(dto.getFilter());
            range.setMatchAll(dto.isMatchAll());
            Role role = roleRepository.findOne(Long.valueOf(dto.getRoleId()));
            if(role != null){
                range.setRole(role);
            }
            ResourceType type = resourceTypeRepository.findByClassName((dto.getResourceTypeClassName()));
            if(type != null){
                range.setResourceType(type);
            }
            return range;
        }

        @Override
        public ResourceRangeDTO doBackward(ResourceRange range) {
            ResourceRangeDTO dto = new ResourceRangeDTO();
            dto.setId(range.getId());
            dto.setFilter(range.getFilter());
            dto.setMatchAll(range.isMatchAll());
            dto.setResourceTypeClassName(range.getResourceType().getClassName());
            dto.setRoleId(range.getRole().getId());
            return dto;
        }
    }

    /**
     * 转化DTO为ResourceRange对象
     */
    public ResourceRange convertToResourceRange(RoleRepository roleRepository, ResourceTypeRepository resourceTypeRepository){
        ResourceRangeConverter converter = new ResourceRangeConverter(roleRepository, resourceTypeRepository);
        return converter.doForward(this);
    }

    /**
     * 转化ResourceRange对象为DTO对象
     */
    public static ResourceRangeDTO convertFromResourceRange(ResourceRange range){
        ResourceRangeConverter converter = new ResourceRangeConverter();
        return converter.doBackward(range);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getResourceTypeClassName() {
        return resourceTypeClassName;
    }

    public void setResourceTypeClassName(String resourceTypeClassName) {
        this.resourceTypeClassName = resourceTypeClassName;
    }

    public boolean isMatchAll() {
        return matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }
}
