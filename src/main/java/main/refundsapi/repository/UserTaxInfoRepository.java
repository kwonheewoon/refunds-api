package main.refundsapi.repository;

import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTaxInfoRepository extends JpaRepository<UserTaxInfoEntity, Long> {

    Optional<UserTaxInfoEntity> findByUserEntityAndYear(UserEntity userEntity, int year);

    Long countByUserEntityAndYear(UserEntity userEntity, int year);
}
