package main.refundsapi.repository;

import main.refundsapi.entity.UserJoinAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJoinAccessRepository extends JpaRepository<UserJoinAccessEntity, Long> {

    Optional<UserJoinAccessEntity> findByNameAndRegNo(String name, String regNo);

}
