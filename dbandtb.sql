/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.11 : Database - financialtdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`financialtdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `financialtdb`;

/*Table structure for table `bizhong` */

DROP TABLE IF EXISTS `bizhong`;

CREATE TABLE `bizhong` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bzname` varchar(30) DEFAULT NULL COMMENT '币种名称',
  `bindex` int(11) DEFAULT NULL COMMENT '币种排序',
  `bzname2` varchar(30) DEFAULT NULL COMMENT '币种名称2',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)id',
  `bzstatus` int(11) DEFAULT '2' COMMENT '币种状态：1-可看可投资，2-可看不可投资，3-不可看不可投资',
  `sjpyl` int(11) DEFAULT NULL COMMENT '相比0时区时间偏移量',
  `sqmc` varchar(200) DEFAULT NULL COMMENT '时区名称',
  `bzzdgl` varchar(1000) DEFAULT NULL COMMENT '记录该币种长期的涨跌规律',
  `xmrhmclvrunalavalue` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '先买入后卖出利润警报值',
  `xmrhmclvrunlvalavalue` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '先买入后卖出利润率警报值',
  `xmrhmcyhmrjalavalue` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '先买入后卖出银行买入价警报值',
  `xmrhmcyhmcjalavalue` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '先买入后卖出银行卖出价警报值',
  `xmchmrlvrunlvalavalue` varchar(300) DEFAULT '0' COMMENT '先卖出后买入利润率警报值',
  `xmchmrlvrunalavalue` varchar(300) DEFAULT '0' COMMENT '先卖出后买入利润警报值',
  `xmchmryhmrjalavalue` varchar(300) DEFAULT '0' COMMENT '先卖出后买入银行买入价警报值',
  `xmchmryhmcjalavalue` varchar(300) DEFAULT '0' COMMENT '先卖出后买入银行卖出价警报值',
  PRIMARY KEY (`id`),
  KEY `orgid` (`orgid`),
  KEY `bzstatus` (`bzstatus`)
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8;

/*Table structure for table `exchangerate` */

DROP TABLE IF EXISTS `exchangerate`;

CREATE TABLE `exchangerate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bzid` bigint(20) DEFAULT NULL COMMENT '币种主键',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)主键',
  `xhmrj` float DEFAULT NULL COMMENT '现汇买入价',
  `xcmrj` float DEFAULT NULL COMMENT '现钞买入价',
  `xhmcj` float DEFAULT NULL COMMENT '现汇卖出价',
  `xcmcj` float DEFAULT NULL COMMENT '现汇卖出价',
  `fbsj` datetime DEFAULT NULL COMMENT '发布时间',
  `fbsjdate` int(11) DEFAULT NULL COMMENT '发布时间到日的转整型',
  `hqsj` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `bwid` bigint(20) DEFAULT NULL COMMENT '获取的报文id',
  PRIMARY KEY (`id`),
  KEY `fbsj` (`fbsj`),
  KEY `bzid` (`bzid`),
  KEY `orgid` (`orgid`),
  KEY `fbsjdate` (`fbsjdate`)
) ENGINE=InnoDB AUTO_INCREMENT=215905378 DEFAULT CHARSET=utf8;

/*Table structure for table `exchangeratecontent` */

DROP TABLE IF EXISTS `exchangeratecontent`;

CREATE TABLE `exchangeratecontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` mediumtext COMMENT '获取的报文内容',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)主键',
  `gettime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `doctype` int(11) DEFAULT NULL COMMENT '1-结售汇接口获取，2-账户外汇接口获取，3-结售汇网页获取，4-账户外汇网页获取',
  PRIMARY KEY (`id`),
  KEY `orgid` (`orgid`),
  KEY `gettime` (`gettime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='获取汇率返回的报文，可能是html，也可能是其他格式的数据';

/*Table structure for table `jinshu` */

DROP TABLE IF EXISTS `jinshu`;

CREATE TABLE `jinshu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `jsname` varchar(30) DEFAULT NULL COMMENT '金属名称',
  `jsname2` varchar(30) DEFAULT NULL COMMENT '金属名称2',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)id',
  `jsstatus` int(11) DEFAULT '2' COMMENT '金属状态：1-可看可投资，2-可看不可投资，3-不可看不可投资',
  `jszdgl` varchar(1000) DEFAULT NULL COMMENT '记录该金属长期的涨跌规律',
  `xmrhmclvrunalavalue` varchar(300) DEFAULT NULL COMMENT '先买入后卖出利润警报值',
  `xmrhmclvrunlvalavalue` varchar(300) DEFAULT NULL COMMENT '先买入后卖出利润率警报值',
  `xmrhmcyhmrjalavalue` varchar(300) DEFAULT NULL COMMENT '先买入后卖出银行买入价警报值',
  `xmrhmcyhmcjalavalue` varchar(300) DEFAULT NULL COMMENT '先买入后卖出银行卖出价警报值',
  `xmchmrlvrunlvalavalue` varchar(300) DEFAULT NULL COMMENT '先卖出后买入利润率警报值',
  `xmchmrlvrunalavalue` varchar(300) DEFAULT NULL COMMENT '先卖出后买入利润警报值',
  `xmchmryhmrjalavalue` varchar(300) DEFAULT NULL COMMENT '先卖出后买入银行买入价警报值',
  `xmchmryhmcjalavalue` varchar(300) DEFAULT NULL COMMENT '先卖出后买入银行卖出价警报值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='金属表，比如账户贵金属，普通金属等';

/*Table structure for table `organ` */

DROP TABLE IF EXISTS `organ`;

CREATE TABLE `organ` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orgname` varchar(100) DEFAULT NULL COMMENT '机构(比如银行)名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `whjyb` */

DROP TABLE IF EXISTS `whjyb`;

CREATE TABLE `whjyb` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `jybzid` bigint(20) DEFAULT NULL COMMENT '交易币种主键',
  `jyjine` float(10,2) DEFAULT NULL COMMENT '交易金额',
  `cjjine` float(10,2) DEFAULT NULL COMMENT '成交金额（人民币）',
  `jyleixing` int(11) DEFAULT NULL COMMENT '交易类型：1-买入开仓，2-卖出平仓，3-卖出开仓，4-买入平仓',
  `jiaoyisj` datetime DEFAULT NULL COMMENT '交易时间',
  `addtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录生成时间',
  `xjzhyue` float(10,2) DEFAULT NULL COMMENT '现金账户余额',
  `whbzjyue` float(10,2) DEFAULT NULL COMMENT '外汇保证金账户余额',
  `zhgjsbzjyue` float DEFAULT NULL COMMENT '账户贵金属保证金账户余额',
  `jslirun` float DEFAULT '0' COMMENT '结算利润',
  `xmrhmccb` float DEFAULT '0' COMMENT '先买入后卖出成本',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `mcbzid` (`jybzid`),
  KEY `jyleixing` (`jyleixing`),
  KEY `jiaoyisj` (`jiaoyisj`)
) ENGINE=InnoDB AUTO_INCREMENT=3768 DEFAULT CHARSET=utf8 COMMENT='外汇交易表';

/*Table structure for table `whkuaizhao` */

DROP TABLE IF EXISTS `whkuaizhao`;

CREATE TABLE `whkuaizhao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `bzid` bigint(20) DEFAULT NULL COMMENT '币种主键',
  `xmrhmcjine` float(10,2) DEFAULT '0.00' COMMENT '先买入后卖出金额',
  `xmrhmcrmbcb` float(10,2) DEFAULT '0.00' COMMENT '先买入后卖出人民币成本',
  `xmchmrjine` float(10,2) DEFAULT '0.00' COMMENT '先卖出后买入金额',
  `xmchmrrmbcb` float(10,2) DEFAULT '0.00' COMMENT '先卖出后买入人民币成本',
  `modifytime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `bzid` (`bzid`),
  KEY `modifytime` (`modifytime`)
) ENGINE=InnoDB AUTO_INCREMENT=33118 DEFAULT CHARSET=utf8 COMMENT='外汇快照，用于记录每个用户的外汇金额';

/*Table structure for table `yonghub` */

DROP TABLE IF EXISTS `yonghub`;

CREATE TABLE `yonghub` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rmbyue` float(10,2) DEFAULT '0.00' COMMENT '人民币余额',
  `whbzjyue` float(10,2) DEFAULT '0.00' COMMENT '外汇保证金余额',
  `zhgjsbzjyue` float DEFAULT '0' COMMENT '账户贵金属保证金余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `zhgjsprice` */

DROP TABLE IF EXISTS `zhgjsprice`;

CREATE TABLE `zhgjsprice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `jsid` bigint(20) DEFAULT NULL COMMENT '金属主键',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)主键',
  `mrj` float DEFAULT NULL COMMENT '买入价',
  `mcj` float DEFAULT NULL COMMENT '卖出价',
  `fbsj` datetime DEFAULT NULL COMMENT '发布时间',
  `fbsjdate` int(11) DEFAULT NULL COMMENT '发布时间到日的转整型',
  `hqsj` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `bwid` bigint(20) DEFAULT NULL COMMENT '获取的报文id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户贵金属价格表';

/*Table structure for table `zhgjspricecontent` */

DROP TABLE IF EXISTS `zhgjspricecontent`;

CREATE TABLE `zhgjspricecontent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` mediumtext COMMENT '获取的报文内容',
  `orgid` bigint(20) DEFAULT NULL COMMENT '机构(比如银行)主键',
  `gettime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `doctype` int(11) DEFAULT NULL COMMENT '1-网页获取，2-接口获取',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
