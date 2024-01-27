package main.refundsapi.user.repository;

import main.refundsapi.user.domain.entity.UserJoinAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJoinAccessRepository extends JpaRepository<UserJoinAccess, Long> {

    Optional<UserJoinAccess> findByNameAndRegNo(String name, String regNo);

    int countByNameAndRegNo(String name, String regNo);

}
