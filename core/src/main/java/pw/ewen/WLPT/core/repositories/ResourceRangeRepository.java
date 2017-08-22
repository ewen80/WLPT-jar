package pw.ewen.WLPT.core.repositories;

import pw.ewen.WLPT.core.domains.entities.ResourceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenliang on 17-2-28.
 * 资源范围仓储接口
 */
@Repository
public interface ResourceRangeRepository extends JpaRepository<ResourceRange, Long>, JpaSpecificationExecutor<ResourceRange>{
    /**
     * 根据角色和资源找出资源范围
     * @param roleId
     * @param resourceTypeClassName
     * @return
     */
    List<ResourceRange> findByRole_idAndResourceType_className(Long roleId, String resourceTypeClassName);
}
