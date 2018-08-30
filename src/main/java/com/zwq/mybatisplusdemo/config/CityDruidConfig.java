package com.zwq.mybatisplusdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ven
 * @date 2018/1/2.
 * @email Ven@qq.com
 */
@Configuration
//@EnableTransactionManagement
@MapperScan(basePackages = CityDruidConfig.MAPPER_PACKAGE, sqlSessionFactoryRef = "manageSqlSessionFactory")
//@MapperScan("com.admin.dao.manageMapper")
public class CityDruidConfig {
    // 精确到 cluster 目录，以便跟其他数据源隔离
    private static final String POJO_PACKAGE = "com.zwq.mybatisplusdemo.pojo";
    static final String MAPPER_PACKAGE = "com.zwq.mybatisplusdemo.dao.city";
    private static final String MAPPER_LOCATION = "classpath*:/mapper/city/*.xml";
    @Value("${spring.datasource.manage.url}")
    private String url;
    @Value("${spring.datasource.manage.username}")
    private String username;
    @Value("${spring.datasource.manage.password}")
    private String password;
    @Value("${spring.datasource.manage.filters}")
    private String filters;
    @Value("${spring.datasource.manage.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.manage.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.manage.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.manage.maxActive}")
    private int maxActive;

    @Bean(name = "manageDataSource")
    public DataSource manageDataSource() {
        DruidDataSource manageDataSource = new DruidDataSource();
        manageDataSource.setUrl(url);
        manageDataSource.setUsername(username);
        manageDataSource.setPassword(password);
        manageDataSource.setDriverClassName(driverClassName);
        manageDataSource.setInitialSize(initialSize);
        manageDataSource.setMinIdle(minIdle);
        manageDataSource.setMaxActive(maxActive);
        try {
            manageDataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return manageDataSource;
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");

        conf.setIdType(0);
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
    @Bean("manageSqlSessionFactory")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("manageDataSource") DataSource manageDataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(manageDataSource);
        mybatisPlus.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(CityDruidConfig.MAPPER_LOCATION));
        mybatisPlus.setTypeAliasesPackage(CityDruidConfig.POJO_PACKAGE);

        mybatisPlus.setGlobalConfig(globalConfiguration());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        mybatisPlus.setConfiguration(configuration);
        return mybatisPlus;
    }

    @Bean(name = "manageTransactionManager")
    public DataSourceTransactionManager manageTransactionManager() {
        return new DataSourceTransactionManager(manageDataSource());
    }
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("manageTransactionManager");
//        mapperScannerConfigurer.setBasePackage(MAPPER_PACKAGE);
//        return mapperScannerConfigurer;
//    }
//    @Bean(name = "masterSqlSessionFactory")
//    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
//            throws Exception {
//        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(masterDataSource);
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources(ManageDruidConfig.MAPPER_LOCATION));
//        return sessionFactory.getObject();
//    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*,*.html");
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");

        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("resetEnable", "false");
        initParameters.put("allow", "");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

/*
  @Bean
  public DruidStatInterceptor getDruidStatInterceptor(){
    return new DruidStatInterceptor();
  }

  @Bean
  @Scope("prototype")
  public JdkRegexpMethodPointcut getJdkRegexpMethodPointcut(){
    JdkRegexpMethodPointcut pointcut=new JdkRegexpMethodPointcut();
    String[] str={"com.len.service.*","com.len.mapper.*"};
    pointcut.setPatterns(str);
    return pointcut;
  }*/

}
