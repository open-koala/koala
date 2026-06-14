package org.openkoala.gqc.application.impl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Seeds local development data sources after the GQC persistence context is ready.
 */
@Named
public class DefaultDataSourceInitializer implements ApplicationListener<ContextRefreshedEvent> {

	private static final String DEFAULT_CONFIG_SOURCE_ID = "gqc_config";

	private static final String DEFAULT_SAMPLE_SOURCE_ID = "gqc_sample";

	private boolean initialized;

	@Inject
	@Named("transactionManager_gqc")
	private PlatformTransactionManager transactionManager;

	@Inject
	@Named("gqcEntityManager")
	private EntityManager entityManager;

	@Value("${gqc.db.jdbc.driver}")
	private String jdbcDriver;

	@Value("${gqc.db.jdbc.connection.url}")
	private String connectUrl;

	@Value("${gqc.db.jdbc.username}")
	private String username;

	@Value("${gqc.db.jdbc.password:}")
	private String password;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		if (context.getParent() != null || initialized) {
			return;
		}
		initialized = true;
		new TransactionTemplate(transactionManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				createIfMissing(DEFAULT_CONFIG_SOURCE_ID, "GQC 内置配置库", jdbcDriver, connectUrl, username, password);
				createIfMissing(DEFAULT_SAMPLE_SOURCE_ID, "本地 H2 示例库", "org.h2.Driver",
						"jdbc:h2:mem:gqc_sample;DB_CLOSE_DELAY=-1", "sa", "");
			}
		});
	}

	private void createIfMissing(String dataSourceId, String description, String driver, String url, String user,
			String pass) {
		if (exists(dataSourceId)) {
			return;
		}
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceType(DataSourceType.CUSTOM_DATA_SOURCE);
		dataSource.setDataSourceId(dataSourceId);
		dataSource.setDataSourceDescription(description);
		dataSource.setJdbcDriver(driver);
		dataSource.setConnectUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pass);
		dataSource.testConnection();
		entityManager.persist(dataSource);
	}

	private boolean exists(String dataSourceId) {
		Number count = (Number) entityManager
				.createQuery("select count(_dataSource) from DataSource _dataSource where _dataSource.dataSourceId = :dataSourceId")
				.setParameter("dataSourceId", dataSourceId).getSingleResult();
		return count.longValue() > 0;
	}
}
