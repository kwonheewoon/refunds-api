package main.refundsapi.taxinfo.repository;

import main.refundsapi.user.domain.entity.User;
import main.refundsapi.taxinfo.domain.entity.TaxInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {
    Optional<TaxInfo> findByUser(User user);
}
