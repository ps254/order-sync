package ru.ordersync.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupSync implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(StartupSync.class);

	private final SyncService syncService;
	private final Environment environment;

	public StartupSync(SyncService syncService, Environment environment) {
		this.syncService = syncService;
		this.environment = environment;
	}

	@Override
	public void run(ApplicationArguments args) {
		int port = environment.getProperty("local.server.port", Integer.class, 8080);
		try {
			syncService.synchronize();
			log.info("Результат: http://localhost:{}/api/clients", port);
		}
		catch (RuntimeException ex) {
			log.error("Стартовая синхронизация не выполнена. Повторите POST /api/sync", ex);
		}
	}

}
