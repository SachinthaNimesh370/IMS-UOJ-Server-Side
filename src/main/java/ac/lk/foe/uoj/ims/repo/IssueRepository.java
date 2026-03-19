package ac.lk.foe.uoj.ims.repo;

import ac.lk.foe.uoj.ims.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<IssueRecord, Long> {
    List<IssueRecord> findByInventoryItemId(Long inventoryItemId);
    List<IssueRecord> findByStatus(String status);
    List<IssueRecord> findByIssuedById(Long issuedById);

    @Query("SELECT ir FROM IssueRecord ir JOIN InventoryItem ii ON ir.inventoryItemId = ii.id WHERE ii.departmentId = :deptId")
    List<IssueRecord> findAllByDepartmentId(@Param("deptId") Long deptId);
    
    @Query("SELECT ir FROM IssueRecord ir JOIN InventoryItem ii ON ir.inventoryItemId = ii.id WHERE ii.departmentId = :deptId AND ir.status = 'ISSUED'")
    List<IssueRecord> findActiveByDepartmentId(@Param("deptId") Long deptId);
}
