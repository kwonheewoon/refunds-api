package main.refundsapi.repository;

import main.refundsapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserIdAndPassword(String userId, String password);

    Optional<UserEntity> findByUserIdAndName(String userId, String name);

    Optional<UserEntity> findByUserId(String userId);

}
