package main.refundsapi.repository;

import main.refundsapi.entity.UserTaxResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTaxResultRepository extends JpaRepository<UserTaxResultEntity, Long> {



}
