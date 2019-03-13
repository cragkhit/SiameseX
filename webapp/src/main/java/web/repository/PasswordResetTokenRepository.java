package web.repository;

import org.springframework.data.repository.CrudRepository;
import web.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {
}
