package pw.ewen.WLPT.core.services;

import pw.ewen.WLPT.core.domains.entities.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pw.ewen.WLPT.core.repositories.ResourceTypeRepository;
import pw.ewen.WLPT.core.repositories.specifications.ResourceTypeSpecificationBuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by wen on 17-7-11.
 */
@Service("resourceTypeService")
public class ResourceTypeService {

    private ResourceTypeRepository resourceTypeRepository;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
    }

    /**
     * 获取单个ResourceType
     * @param className 类全限定名
     * @return  ResourceType
     */
    public ResourceType findByClassName(String className){
        return this.resourceTypeRepository.findByClassName(className);
    }

    /**获取ResourceTypes根据classNames
     * @param classNames 类全限定名（,分隔）
     * @return
     */
    public Collection<ResourceType> findByClassNames(String classNames){
        String[] arrClassNames = classNames.split(",");
        ArrayList<ResourceType> arrResourceTypes = new ArrayList<>();
        for(String className : arrClassNames){
            ResourceType rt = this.resourceTypeRepository.findByClassName(className);
            arrResourceTypes.add(rt);
        }
        return arrResourceTypes;
    }

    public ResourceType findOne(Long id){
        return this.resourceTypeRepository.findOne(id);
    }

    public Collection<ResourceType> findByIds(Collection<Long> ids){
        ArrayList<ResourceType> arrResourceTypes = new ArrayList<>();
        for(Long id : ids){
            ResourceType resourceType = this.resourceTypeRepository.findOne(id);
            arrResourceTypes.add(resourceType);
        }
        return arrResourceTypes;
    }

    /**
     * 服务器分页
     * @param pageIndex
     * @param pageSize
     * @param filter
     * @return
     */
    public Page<ResourceType> getResourcesWithPage(int pageIndex, int pageSize, String filter){
        ResourceTypeSpecificationBuilder builder = new ResourceTypeSpecificationBuilder();
        if(filter.isEmpty()){
            return resourceTypeRepository.findAll(new PageRequest(pageIndex, pageSize, new Sort(Sort.Direction.ASC, "name")));
        }else{
            return resourceTypeRepository.findAll(builder.build(filter), new PageRequest(pageIndex, pageSize, new Sort(Sort.Direction.ASC, "name")));
        }
    }

    public Collection<ResourceType> findAll(String filter){
        ResourceTypeSpecificationBuilder builder = new ResourceTypeSpecificationBuilder();
        if(filter.isEmpty()){
            return this.resourceTypeRepository.findAll();
        } else {
            return this.resourceTypeRepository.findAll(builder.build(filter));
        }
    }

    public ResourceType save(ResourceType resourceType){
        return this.resourceTypeRepository.save(resourceType);
    }

    /**
     * 保存ResourceType
     * @param resourceTypeClassName ResourceType的全限定名
     * @return 保存后的ResourceType
     */
    public ResourceType save(String resourceTypeClassName){
        return this.save(resourceTypeClassName, resourceTypeClassName);
    }

    /**
     * 保存ResourceType
     * @param resourceTypeName  资源类简称
     * @param resourceTypeClassName 资源类全限定名
     * @return
     */
    public ResourceType save(String resourceTypeName,String resourceTypeClassName){
        ResourceType resourceType = new ResourceType(resourceTypeClassName, resourceTypeName);
        return this.save(resourceType);
    }

    /**
     * 删除（如果当前资源类型下有资源范围则软删除）
     * @param resourceTypes
     */
    public void delete(Collection<ResourceType> resourceTypes){
        for(ResourceType resourceType : resourceTypes){
            //检查该ResourceType下是否还有ResourceRange,有则软删除，没有则真删除
            if(resourceType.getResourceRanges().size() > 0){
                resourceType.setDeleted(true);
                this.resourceTypeRepository.save(resourceType);
            } else {
                this.resourceTypeRepository.delete(resourceType);
            }
        }

    }
}
