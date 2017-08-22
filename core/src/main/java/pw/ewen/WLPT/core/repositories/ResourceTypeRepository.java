package pw.ewen.WLPT.core.repositories;

import pw.ewen.WLPT.core.domains.entities.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by wen on 17-3-12.
 * 资源列型仓储
 */
@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long>, JpaSpecificationExecutor<ResourceType> {
    ResourceType findByClassName(String className);
}
