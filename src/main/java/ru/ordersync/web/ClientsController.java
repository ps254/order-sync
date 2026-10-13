package ru.ordersync.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ordersync.service.SyncService;

@RestController
public class ClientsController {

	private final SyncService syncService;

	public ClientsController(SyncService syncService) {
		this.syncService = syncService;
	}

	@GetMapping({"/", "/api/clients"})
	public ClientsResponse clients() {
		return syncService.current();
	}

	@PostMapping("/api/sync")
	public ClientsResponse sync() {
		return syncService.synchronize();
	}

}
