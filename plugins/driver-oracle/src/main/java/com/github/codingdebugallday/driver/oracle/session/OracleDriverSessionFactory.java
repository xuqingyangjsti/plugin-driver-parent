package com.github.codingdebugallday.driver.oracle.session;

import javax.sql.DataSource;

import com.github.codingdebugallday.driver.core.app.service.session.DriverSession;
import com.github.codingdebugallday.driver.core.infra.function.DriverSessionFunction;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;


/**
 * <p>
 * 主程序定义SessionService接口的实现类
 * </p>
 *
 * @author xinkai.chen@hand-china.com 2020/8/5 15:33
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Slf4j
@Extension
public class OracleDriverSessionFactory implements DriverSessionFunction<DataSource> {

    private DataSource dataSource;

    @Override
    public Class<DataSource> getDataSource() {
        return DataSource.class;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DriverSession getDriverSession() {
        return new OracleDriverSession(dataSource);
    }

}
