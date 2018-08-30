/**
 * created on 2018年4月17日 上午11:51:07
 */
package com.utstar.uapollo.custom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
@Configuration
@EnableJpaRepositories("com.utstar.uapollo.custom.repositry")
public class DatabaseCustomConfiguration {

}
