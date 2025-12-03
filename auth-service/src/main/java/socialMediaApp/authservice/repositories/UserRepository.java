package socialMediaApp.authservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socialMediaApp.authservice.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    void deleteById(int id);
    User findByEmail(String email);
    
}