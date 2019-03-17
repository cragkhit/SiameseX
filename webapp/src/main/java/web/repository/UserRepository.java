package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}