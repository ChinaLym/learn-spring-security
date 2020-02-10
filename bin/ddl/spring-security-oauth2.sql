/*
SQLyog v10.2 
MySQL - 5.7.20 : Database - learn_security
https://gitee.com/ChinaLym/learn-spring-security
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`learn_security` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `learn_security`;

/*Table structure for table `oauth_access_token` */

DROP TABLE IF EXISTS `oauth_access_token`;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(255) NOT NULL COMMENT 'access_token的MD5值',
  `token` blob COMMENT 'OAuth2AccessToken 对象序列化后的二进制数据。真实的AccessToken',
  `authentication_id` varchar(255) DEFAULT NULL COMMENT '唯一性, 其值是根据当前的username(如果有),client_id与scope通过MD5生成的. 具体实现参考DefaultAuthenticationKeyGenerator.java',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登录时的用户名。若客户端没有用户名,则该值等于client_id',
  `client_id` varchar(255) DEFAULT NULL COMMENT '对应 oauth_client_details 主键',
  `authentication` blob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据.',
  `refresh_token` varchar(255) DEFAULT NULL COMMENT '关联refresh_token表主键。refresh_token的值的 MD5',
  `create_time` datetime DEFAULT NULL COMMENT '自行扩展字段：创建时间',
  PRIMARY KEY (`token_id`),
  UNIQUE KEY `authentication_id_index` (`authentication_id`),
  UNIQUE KEY `user_name_index` (`user_name`),
  UNIQUE KEY `refresh_token_index` (`refresh_token`),
  KEY `client_id_index` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='access_token表。见JdbcTokenStore.java. ';

/*Data for the table `oauth_access_token` */

insert  into `oauth_access_token`(`token_id`,`token`,`authentication_id`,`user_name`,`client_id`,`authentication`,`refresh_token`,`create_time`) values ('9104685d6becc325ef07b1a80f353e3b','��\0sr\0Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken��6$��\0L\0additionalInformationt\0Ljava/util/Map;L\0\nexpirationt\0Ljava/util/Date;L\0refreshTokent\0?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\0scopet\0Ljava/util/Set;L\0	tokenTypet\0Ljava/lang/String;L\0valueq\0~\0xpsr\0java.util.Collections$EmptyMapY6�Z���\0\0xpsr\0java.util.Datehj�KYt\0\0xpw\0\0p�rhxsr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/�Gc��ɷ\0L\0\nexpirationq\0~\0xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens�\ncT�^\0L\0valueq\0~\0xpt\0$202ace1b-0556-47db-831d-fdc96c1fcfb0sq\0~\0	w\0\0q©ifxsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xr\0,java.util.Collections$UnmodifiableCollectionB\0��^�\0L\0ct\0Ljava/util/Collection;xpsr\0java.util.LinkedHashSet�l�Z��*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0message:readt\0	user:readt\0message.readt\0\rmessage:writet\0\rmessage.writext\0bearert\0$630d5653-8d26-469d-92cd-7985e4152397','e191ef95b4a020075de84825cb707719','user','demo-client-id','��\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1��\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0��^�\0L\0cq\0~\0xpsr\0java.util.ArrayListx����a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0demo-client-idsr\0%java.util.Collections$UnmodifiableMap��t�B\0L\0mq\0~\0xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0codet\0CpOTgZt\0\ngrant_typet\0authorization_codet\0\rresponse_typet\0codet\0statet\0,T8YAb1JNrscfwOa07OrXxBAnCipPkj4jSzCmBPuuDpI=t\0redirect_urit\0&http://demo.com/login/oauth2/code/demot\0	client_idq\0~\0xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet�l�Z��*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0message:readt\0	user:readt\0message.readt\0\rmessage:writet\0\rmessage.writexsq\0~\0*w\0\0\0?@\0\0\0\0\0\0xsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xt\0&http://demo.com/login/oauth2/code/demopsq\0~\0*w\0\0\0?@\0\0\0\0\0\0xsq\0~\0*w\0\0\0?@\0\0\0\0\0q\0~\0!xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\09sr\0Horg.springframework.security.web.authentication.WebAuthenticationDetails\0\0\0\0\0\0\0L\0\rremoteAddressq\0~\0L\0	sessionIdq\0~\0xpt\0	127.0.0.1t\0 540BAE68DCF6ECF4EE2018374ADB246Dpsr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0\'sr\0java.util.TreeSetݘP���[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0xpt\0user','a6cab609e0a5c4b323dbad8c8db9997c',NULL);

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '唯一的clientId',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '客户端所能访问的资源id集合。多个资源时用逗号(,)分隔.一般为大类，不应该很细',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥。也称作 appSecret',
  `scope` varchar(255) DEFAULT NULL COMMENT '客户端申请的权限范围。如读取用户信息，分享到空间，发表说说等',
  `authorized_grant_types` varchar(255) DEFAULT NULL COMMENT '客户端支持的grant_type。可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: "authorization_code,password".',
  `web_server_redirect_uri` varchar(255) DEFAULT NULL COMMENT '客户端的重定向URI。可空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致.',
  `authorities` varchar(255) DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值。可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER".',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '客户端的access_token的有效时间值(单位:秒)。可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时).',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '客户端的refresh_token的有效时间值(单位:秒)。可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天).',
  `additional_information` text COMMENT '预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据。在实际应用中, 可以用该字段来存储关于客户端的一些其他信息,如客户端的国家,地区,注册时的IP地址等等.',
  `autoapprove` varchar(255) DEFAULT 'false' COMMENT '是否跳过用户同意授权页面, 默认值为 ''false'', 可选值包括 ''true'',''false'', ''read'',''write''.',
  `active` tinyint(1) DEFAULT '1' COMMENT '自行扩展的：是否启用，即实现逻辑删除或停用，以及人工验证信息通过后再启用',
  `create_time` datetime DEFAULT NULL COMMENT '自行扩展的：插入时间',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用于存储可以访问本授权服务器的客户端信息表。见JdbcClientDetailsService';

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`,`active`,`create_time`) values ('demo-client-id',NULL,'secret','message.read,message.write,message:read,message:write,user:read','authorization_code,password,client_credentials,implicit,refresh_token','http://demo.com/login/oauth2/code/demo,http://localhost/login/oauth2/code/demo,http://127.0.0.1/login/oauth2/code/demo',NULL,36000,7200000,NULL,'false',1,NULL);

/*Table structure for table `oauth_code` */

DROP TABLE IF EXISTS `oauth_code`;

CREATE TABLE `oauth_code` (
  `code` varchar(255) DEFAULT NULL COMMENT '存储服务端系统生成的code的值(未加密).',
  `authentication` blob COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据.',
  KEY `code_index` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权码。仅授权码模式使用该表：用于换accessToken。见JdbcAuthorizationCodeServices';

/*Data for the table `oauth_code` */

/*Table structure for table `oauth_refresh_token` */

DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(255) DEFAULT NULL COMMENT 'refresh_token的值的MD5',
  `token` blob COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据.',
  `authentication` blob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据.',
  `create_time` datetime DEFAULT NULL COMMENT '自行扩展的：插入时间',
  KEY `token_id_index` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='refresh_token表。见JdbcTokenStore';

/*Data for the table `oauth_refresh_token` */

insert  into `oauth_refresh_token`(`token_id`,`token`,`authentication`,`create_time`) values ('a6cab609e0a5c4b323dbad8c8db9997c','��\0sr\0Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/�Gc��ɷ\0L\0\nexpirationt\0Ljava/util/Date;xr\0Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens�\ncT�^\0L\0valuet\0Ljava/lang/String;xpt\0$202ace1b-0556-47db-831d-fdc96c1fcfb0sr\0java.util.Datehj�KYt\0\0xpw\0\0q©ifx','��\0sr\0Aorg.springframework.security.oauth2.provider.OAuth2Authentication�@bR\0L\0\rstoredRequestt\0<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\0userAuthenticationt\02Lorg/springframework/security/core/Authentication;xr\0Gorg.springframework.security.authentication.AbstractAuthenticationTokenӪ(~nGd\0Z\0\rauthenticatedL\0authoritiest\0Ljava/util/Collection;L\0detailst\0Ljava/lang/Object;xp\0sr\0&java.util.Collections$UnmodifiableList�%1��\0L\0listt\0Ljava/util/List;xr\0,java.util.Collections$UnmodifiableCollectionB\0��^�\0L\0cq\0~\0xpsr\0java.util.ArrayListx����a�\0I\0sizexp\0\0\0w\0\0\0sr\0Borg.springframework.security.core.authority.SimpleGrantedAuthority\0\0\0\0\0\0\0L\0rolet\0Ljava/lang/String;xpt\0	ROLE_USERxq\0~\0psr\0:org.springframework.security.oauth2.provider.OAuth2Request\0\0\0\0\0\0\0\0Z\0approvedL\0authoritiesq\0~\0L\0\nextensionst\0Ljava/util/Map;L\0redirectUriq\0~\0L\0refresht\0;Lorg/springframework/security/oauth2/provider/TokenRequest;L\0resourceIdst\0Ljava/util/Set;L\0\rresponseTypesq\0~\0xr\08org.springframework.security.oauth2.provider.BaseRequest6(z>�qi�\0L\0clientIdq\0~\0L\0requestParametersq\0~\0L\0scopeq\0~\0xpt\0demo-client-idsr\0%java.util.Collections$UnmodifiableMap��t�B\0L\0mq\0~\0xpsr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0t\0codet\02SXaXgt\0\ngrant_typet\0authorization_codet\0\rresponse_typet\0codet\0statet\0,2JV2hSQXqz1lotcjSbqrYaSLIaWTtvNvv_vroWG06-I=t\0redirect_urit\0\'http://localhost/login/oauth2/code/demot\0	client_idq\0~\0xsr\0%java.util.Collections$UnmodifiableSet��я��U\0\0xq\0~\0	sr\0java.util.LinkedHashSet�l�Z��*\0\0xr\0java.util.HashSet�D�����4\0\0xpw\0\0\0?@\0\0\0\0\0t\0message:readt\0	user:readt\0message.readt\0\rmessage:writet\0\rmessage.writexsq\0~\0*w\0\0\0?@\0\0\0\0\0\0xsq\0~\0\Z?@\0\0\0\0\0\0w\0\0\0\0\0\0\0xt\0\'http://localhost/login/oauth2/code/demopsq\0~\0*w\0\0\0?@\0\0\0\0\0\0xsq\0~\0*w\0\0\0?@\0\0\0\0\0q\0~\0!xsr\0Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\0\0\0\0\0\0\0L\0credentialsq\0~\0L\0	principalq\0~\0xq\0~\0sq\0~\0sq\0~\0\0\0\0w\0\0\0q\0~\0xq\0~\09sr\0Horg.springframework.security.web.authentication.WebAuthenticationDetails\0\0\0\0\0\0\0L\0\rremoteAddressq\0~\0L\0	sessionIdq\0~\0xpt\0	127.0.0.1t\0 5F717EB7DF02F88987FCD268C1BE1658psr\02org.springframework.security.core.userdetails.User\0\0\0\0\0\0\0Z\0accountNonExpiredZ\0accountNonLockedZ\0credentialsNonExpiredZ\0enabledL\0authoritiesq\0~\0L\0passwordq\0~\0L\0usernameq\0~\0xpsq\0~\0\'sr\0java.util.TreeSetݘP���[\0\0xpsr\0Forg.springframework.security.core.userdetails.User$AuthorityComparator\0\0\0\0\0\0\0\0xpw\0\0\0q\0~\0xpt\0user',NULL);

/*Table structure for table `persistent_logins` */

DROP TABLE IF EXISTS `persistent_logins`;

CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `persistent_logins` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
