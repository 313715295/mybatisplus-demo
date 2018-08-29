package com.zwq.mybatisplusdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = UserDruidConfig.MAPPER_PACKAGE, sqlSessionFactoryRef = "sysSqlSessionFactory")
//@MapperScan("com.admin.dao.sysMapper")
public class UserDruidConfig {
    // 精确到 cluster 目录，以便跟其他数据源隔离
    static final String POJO_PACKAGE = "com.zwq.mybatisplusdemo.pojo";
    static final String MAPPER_PACKAGE = "com.zwq.mybatisplusdemo.dao.user";
    static final String MAPPER_LOCATION = "classpath*:/mapper/user/*.xml";
    @Value("${spring.datasource.sys.url}")
    private String url;
    @Value("${spring.datasource.sys.username}")
    private String username;
    @Value("${spring.datasource.sys.password}")
    private String password;
    @Value("${spring.datasource.sys.filters}")
    private String filters;
    @Value("${spring.datasource.sys.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.sys.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.sys.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.sys.maxActive}")
    private int maxActive;

    @Primary
    @Bean(name = "sysDataSource")
    public DataSource sysDataSource() {
        DruidDataSource sysDataSource = new DruidDataSource();
        try {
            sysDataSource.setUrl(url);
            sysDataSource.setUsername(username);
            sysDataSource.setPassword(password);
            sysDataSource.setDriverClassName(driverClassName);
            sysDataSource.setInitialSize(initialSize);
            sysDataSource.setMinIdle(minIdle);
            sysDataSource.setMaxActive(maxActive);
            sysDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sysDataSource;
    }

    @Primary
    @Bean
    public GlobalConfiguration globalConfiguration(){
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setIdType(0);
        conf.setFieldStrategy(2);
//        conf.setLogicDeleteValue("-1");
//        conf.setLogicNotDeleteValue("1");
        conf.setDbColumnUnderline(true);
        conf.setRefresh(true);
        conf.setDbType("mysql");
        return conf;
    }



    /**
     * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
     * 配置文件和mybatis-boot的配置文件同步
     * @return
     */
    @Primary
    @Bean("sysSqlSessionFactory")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("sysDataSource") DataSource sysDataSource, GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(sysDataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
//        mybatisPlus.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(UserDruidConfig.MAPPER_LOCATION));
        mybatisPlus.setTypeAliasesPackage(UserDruidConfig.POJO_PACKAGE);
        mybatisPlus.setGlobalConfig(globalConfiguration);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        mybatisPlus.setConfiguration(configuration);
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        mybatisPlus.setConfiguration(configuration);
        return mybatisPlus;
    }

    @Primary
    @Bean(name = "clusterTransactionManager")
    public DataSourceTransactionManager clusterTransactionManager() {
        return new DataSourceTransactionManager(sysDataSource());
    }
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sysSqlSessionFactory");
//        mapperScannerConfigurer.setBasePackage(MAPPER_PACKAGE);
//        return mapperScannerConfigurer;
//    }
//    @Bean(name = "sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("sysDataSource") DataSource sysDataSource)
//            throws Exception {
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(sysDataSource);
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources(SysDruidConfig.MAPPER_LOCATION));
//        return sessionFactory.getObject();
//    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        //reg.addInitParameter("allow", "127.0.0.1"); //白名单
        reg.addInitParameter("resetEnable","false");
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<String, String>();
        //设置忽略请求
        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName","USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName","");
        filterRegistrationBean.addInitParameter("aopPatterns","com.admin.service");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
