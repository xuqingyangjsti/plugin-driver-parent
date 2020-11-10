package com.github.thestyleofme.driver.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.thestyleofme.driver.core.app.service.session.DriverSession;
import com.github.thestyleofme.driver.core.app.service.session.SqlResponse;
import com.github.thestyleofme.driver.core.infra.meta.*;
import com.github.thestyleofme.driver.core.infra.utils.SqlParserUtil;
import com.github.thestyleofme.driver.mysql.session.MysqlDriverSessionFactory;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * Session接口测试
 * </p>
 *
 * @author JupiterMouse 2020/08/03
 * @since 1.0
 */
public class MysqlDriverSessionTest {

    private DriverSession driverSession;

    @Before
    public void buildMysqlSession() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://172.23.16.63:23306/auto_laolu_test?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root#edc456");
        dataSource.setSchema("auto_laolu_test");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.addDataSourceProperty("useInformationSchema", "true");
        MysqlDriverSessionFactory mysqlDriverSessionFactory = new MysqlDriverSessionFactory();
        mysqlDriverSessionFactory.setDataSource(dataSource);
        this.driverSession = mysqlDriverSessionFactory.getDriverSession();
    }

    //===============================================================================
    //  SchemaSession
    //===============================================================================

    @Test
    public void testTablesNameAndDesc() {
        List<Table> tables = driverSession.tablesNameAndDesc("hdsp_dispatch");
        System.out.println(tables);
        assertFalse(CollectionUtils.isEmpty(tables));
    }

    @Test
    public void columnMetadataBySql() {
        List<Column> columns = driverSession.columnMetaDataBySql("hdsp_dispatch", "select executors1.*,executors2.* from executors executors1 left join executors executors2 on executors1.id=executors2.id where 1=0");
        System.out.println(columns);
        assertFalse(CollectionUtils.isEmpty(columns));
    }

    @Test
    public void testSchemaList() {
        List<String> schemaList = driverSession.schemaList();
        System.out.println(schemaList);
        assertFalse(CollectionUtils.isEmpty(schemaList));
    }

    @Test
    public void testSchemaCreate() {
        boolean schemaCreateFlag = driverSession.schemaCreate("test123");
        assertTrue(schemaCreateFlag);
    }

    @Test
    public void testQueryCount() {
        String sql = "select * from hzero_platform.iam_user;";
        Long count = driverSession.queryCount(null, sql);
        System.out.println(count);
        assertNotNull(count);
    }

    @Test
    public void testCurrentSchema() {
        String currentSchema = driverSession.currentSchema();
        System.out.println(currentSchema);
        // mysql currentSchema为NULL
        assertTrue(currentSchema.isEmpty());
    }

    @Test
    public void testCurrentCatalog() {
        String currentCatalog = driverSession.currentCatalog();
        System.out.println(currentCatalog);
        assertFalse(currentCatalog.isEmpty());
    }


    @Test
    public void testExecuteAll() {
        List<String> tableList = driverSession.tableList(null);
        assertFalse(CollectionUtils.isEmpty(tableList));
        // 增
        String showCreateTableSql = "show create table " + tableList.get(0);
        Map<String, SqlResponse> responseMap = driverSession.executeAllDetail("hdsp_dispatch", showCreateTableSql);
        System.out.println();
        // 插
        // 查
        // 删除
        StringBuilder text = new StringBuilder();
        text.append(showCreateTableSql).append("\n");

//        List<List<Map<String, Object>>> executeAll = driverSession.executeAll("hdsp_dispatch", text.toString(), true);
//        assertFalse(executeAll.isEmpty());
    }


    //===============================================================================
    //  TableSession
    //===============================================================================

    @Test
    public void testTableList() {
        List<String> tableList = driverSession.tableList(driverSession.currentCatalog());
        tableList.forEach(System.out::println);
        assertFalse(CollectionUtils.isEmpty(tableList));
    }

    @Test
    public void testTablePk() {
        List<PrimaryKey> primaryKeys = driverSession.tablePk("plugin_test", "iam_user");
        primaryKeys.forEach(System.out::println);
        assertFalse(CollectionUtils.isEmpty(primaryKeys));
    }

    @Test
    public void testTableFk() {
        List<ForeignKey> foreignKeys = driverSession.tableFk("plugin_test", "iam_user");
        foreignKeys.forEach(System.out::println);
        assertFalse(CollectionUtils.isEmpty(foreignKeys));
    }

    @Test
    public void testTableIndex() {
        List<IndexKey> indexKeyList = driverSession.tableIndex("plugin_test", "iam_user");
        indexKeyList.forEach(System.out::println);
        assertFalse(CollectionUtils.isEmpty(indexKeyList));
    }

    @Test
    public void testTableExists() {
        boolean tableExists = driverSession.tableExists("plugin_test", "iam_user");
        System.out.println(tableExists);
        assertTrue(tableExists);
    }

    @Test
    public void testViews() {
        List<String> views = driverSession.viewList("hdsp_dispatch");
        views.forEach(System.out::println);
        assertNotNull(views);
    }

    @Test
    public void testTableQuery() {
        List<Map<String, Object>> tableQuery = driverSession.tableQuery("mysql", "user");
        System.out.println(tableQuery);
        assertFalse(tableQuery.isEmpty());
    }

    @Test
    public void tableInsert() {

    }

    //===============================================================================
    //  TableSession
    //===============================================================================
    @Test
    public void testTableMetaData() {
        Table table = driverSession.tableMetaData("plugin_test", "iam_user");
        System.out.println(table);
        assertTrue(Objects.nonNull(table));
    }

    @Test
    public void tableMetaExtra() {
        Table table = driverSession.tableMetaExtra("apps", "dl_job_info_01_copy");
        System.out.println(table);
        assertTrue(Objects.nonNull(table));
    }

    @Test
    public void isValid() {
        boolean valid = driverSession.isValid();
        assertTrue(valid);
    }

    @Test
    public void columnMetaData() {
        List<Column> columns = driverSession.columnMetaData("hdsp_dispatch", "xcor_datasource");
        columns.forEach(System.out::println);
        assertTrue(columns.size() > 0);
    }

    @Test
    public void testParserFields() {
        List<String> list = SqlParserUtil.parserFields("select id as big_id,username from test_partition");
        list.forEach(System.out::println);
        assertEquals(2, list.size());
    }

    @Test
    public void testsSchemaMetaExtra() {
        Schema schema = driverSession.schemaMetaExtra(driverSession.currentCatalog());
        System.out.println(schema);
        assertTrue(Objects.nonNull(schema));
    }

    @Test
    public void testCatalogMetaExtra() {
        Catalog catalog = driverSession.catalogMetaExtra();
        System.out.println(catalog);
        assertTrue(Objects.nonNull(catalog));
    }

    @Test
    public void testShowCreateSql() {
        String showTableSql = driverSession.showCreateSql("hdsp_dispatch", "xcor_datasource", ShowType.TABLE);
        System.out.println(showTableSql);
        assertFalse(showTableSql.isEmpty());

        String showViewSql = driverSession.showCreateSql("hdsp_dispatch", "xcor_datasource_assign_v", ShowType.VIEW);
        System.out.println(showViewSql);
        assertFalse(showViewSql.isEmpty());

    }

    @Test
    public void testInsert() {
        driverSession.executeOneUpdate("hdsp_test", "insert into cq_test_table081203(tenant_id,birthday,hdsp_batch_id,score,name,ctime,time,age) VALUES ('10','2020-08-12','10058','100','热气球','2020-08-12 20:32:59','2020-08-12 20:32:54','23')");
        System.out.println();
        assertTrue(true);
    }

    @Test
    public void testUpdateComment() {
        List<Column> columns = new ArrayList<>();
        columns.add(Column.builder()
                .tableName("mysql_all_col3_0825v1")
                .tableSchema("auto_laolu_test")
                .columnName("col_time")
                .typeName("time")
                .remarks("66667")
                .build());
        columns.add(Column.builder()
                .tableName("mysql_all_col3_0825v1")
                .tableSchema("auto_laolu_test")
                .columnName("col_char")
                .columnSize(30)
                .typeName("char")
                .remarks("66667")
                .build());
        driverSession.updateComment(columns);
    }
}
