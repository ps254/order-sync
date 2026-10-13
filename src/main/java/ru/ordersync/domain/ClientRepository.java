package ru.ordersync.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {

	List<ClientEntity> findAllByOrderByPhoneAsc();

}
