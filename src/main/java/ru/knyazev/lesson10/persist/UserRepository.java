package ru.knyazev.lesson10.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    List<User> findUserByUsernameLike(String username);

    Optional<User> findUserByUsername(String username);

    @Query("select u from User u " +
            "where (u.username like :username or :username is null) and " +
            "      (u.age >= :minAge or :minAge is null) and " +
            "      (u.age <= :maxAge or :maxAge is null)")
    List<User> findWithFilter(@Param("username") String usernameFilter,
                              @Param("minAge") Integer minAge,
                              @Param("maxAge") Integer maxAge);

}
