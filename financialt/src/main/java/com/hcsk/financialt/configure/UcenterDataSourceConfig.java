package com.hcsk.financialt.configure;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * mysql主库配置类
 * @日期： 2018年7月5日 下午10:05:25
 * @作者： Chendb
 */
@Configuration
@MapperScan(basePackages = "com.hcsk.financialt.ucenterdb.dao",sqlSessionTemplateRef = "ucenterSqlSessionTemplate")
public class UcenterDataSourceConfig {

    /**
     * 创建数据源
     *@return DataSource
     */
    @Bean(name = "ucenterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ucenter")
    public DataSource ucenterDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 创建工厂
     *@param dataSource
     *@throws Exception
     *@return SqlSessionFactory
     */
    @Bean(name = "ucenterSqlSessionFactory")
    public SqlSessionFactory ucenterSqlSessionFactory(@Qualifier("ucenterDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/hcsk/itwebisb/ucenterdb/mapper/*.xml"));
        return bean.getObject();
    }

    /**
     * 创建事务
     *@param dataSource
     *@return DataSourceTransactionManager
     */
    @Bean(name = "ucenterTransactionManager")
    public DataSourceTransactionManager ucenterDataSourceTransactionManager(@Qualifier("ucenterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建模板
     *@param sqlSessionFactory
     *@return SqlSessionTemplate
     */
    @Bean(name = "ucenterSqlSessionTemplate")
    public SqlSessionTemplate ucenterSqlSessionTemplate(@Qualifier("ucenterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}