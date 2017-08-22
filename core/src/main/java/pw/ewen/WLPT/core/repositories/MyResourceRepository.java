package pw.ewen.WLPT.core.repositories;

import pw.ewen.WLPT.core.domains.entities.MyResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by wen on 17-2-24.
 */
@Repository
public interface MyResourceRepository extends JpaRepository<MyResource, Long>, JpaSpecificationExecutor<MyResource> {
}
