package no.noroff.hvz.repositories;

import no.noroff.hvz.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository  extends JpaRepository<AppUser, Long> {

    AppUser getAppUserByOpenId(String openId);
    boolean existsAppUserByOpenId(String openId);
}
