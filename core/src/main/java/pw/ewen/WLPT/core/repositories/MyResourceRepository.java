package pw.ewen.WLPT.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import domains.entities.MyResource;

/**
 * Created by wen on 17-2-24.
 */
@Repository
public interface MyResourceRepository extends JpaRepository<MyResource, Long>, JpaSpecificationExecutor<MyResource> {
}
