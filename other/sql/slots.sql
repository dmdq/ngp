/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : slots

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:34 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `data_count`
-- ----------------------------
DROP TABLE IF EXISTS `data_count`;
CREATE TABLE `data_count` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `type` varchar(200) NOT NULL,
  `create_time` datetime NOT NULL,
  `i1` bigint(20) DEFAULT NULL,
  `i2` bigint(20) DEFAULT NULL,
  `i3` bigint(20) DEFAULT NULL,
  `i4` bigint(20) DEFAULT NULL,
  `i5` bigint(20) DEFAULT NULL,
  `s1` varchar(200) DEFAULT NULL,
  `s2` varchar(200) DEFAULT NULL,
  `s3` varchar(200) DEFAULT NULL,
  `s4` varchar(200) DEFAULT NULL,
  `s5` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1127 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `event`
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(4) NOT NULL DEFAULT '0',
  `cur_unit` varchar(20) NOT NULL COMMENT '本周周期',
  `next_unit` varchar(20) DEFAULT NULL COMMENT '下周周期',
  `bak_start_time` bigint(20) DEFAULT NULL COMMENT '返回时间',
  `refresh_start_time` bigint(20) DEFAULT NULL COMMENT '刷新时间',
  `update_time` datetime NOT NULL,
  `cur_total_millis` bigint(20) NOT NULL COMMENT '本周周期毫秒',
  `next_total_millis` bigint(20) NOT NULL COMMENT '下周周期毫秒',
  `period` int(11) NOT NULL COMMENT '第几期',
  `cur_type` tinyint(4) NOT NULL COMMENT '本周类型',
  `next_type` tinyint(4) NOT NULL COMMENT '下周类型',
  `last_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '上一周类型',
  `cur_status` tinyint(4) NOT NULL COMMENT '本周状态',
  `next_status` tinyint(4) NOT NULL COMMENT '下周状态',
  `close_unit` varchar(20) DEFAULT NULL COMMENT '关闭周期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ios_order`
-- ----------------------------
DROP TABLE IF EXISTS `ios_order`;
CREATE TABLE `ios_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` varchar(50) NOT NULL,
  `item_id` varchar(50) DEFAULT NULL,
  `quantity` varchar(50) DEFAULT NULL,
  `bid` varchar(50) DEFAULT NULL,
  `bvrs` varchar(50) DEFAULT NULL,
  `purchase_date` varchar(50) NOT NULL,
  `json_data` text NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `user_id` bigint(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `unique_vendor_identifier` varchar(50) DEFAULT NULL,
  `purchase_date_ms` varchar(50) DEFAULT NULL,
  `original_transaction_id` varchar(50) DEFAULT NULL,
  `original_purchases_date_ms` varchar(50) DEFAULT NULL,
  `unique_idetifier` varchar(50) DEFAULT NULL,
  `original_purchases_date_pst` varchar(50) DEFAULT NULL,
  `transaction_id` varchar(50) NOT NULL,
  `purchase_date_pst` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=592 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sale`
-- ----------------------------
DROP TABLE IF EXISTS `sale`;
CREATE TABLE `sale` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ad` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  `json_data` text,
  `ip` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sale_history`
-- ----------------------------
DROP TABLE IF EXISTS `sale_history`;
CREATE TABLE `sale_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ad` varchar(255) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  `json_data` text,
  `user_id` bigint(20) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=488 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `slots_buy`
-- ----------------------------
DROP TABLE IF EXISTS `slots_buy`;
CREATE TABLE `slots_buy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `nick` varchar(50) DEFAULT NULL,
  `product_id` varchar(50) NOT NULL,
  `level` varchar(50) DEFAULT NULL,
  `coins` varchar(50) DEFAULT NULL,
  `rank` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `app_id` varchar(255) NOT NULL,
  `bundle_id` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=440 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `slots_data`
-- ----------------------------
DROP TABLE IF EXISTS `slots_data`;
CREATE TABLE `slots_data` (
  `user_id` bigint(20) unsigned NOT NULL,
  `status` tinyint(4) NOT NULL,
  `status_detail` varchar(255) DEFAULT NULL,
  `collect_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `collect_status` tinyint(4) NOT NULL,
  `activity_data` text,
  `json_data` text NOT NULL,
  `touch_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `action_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL,
  `ulld` varchar(40) DEFAULT NULL,
  `nick` varchar(40) DEFAULT NULL,
  `integral` int(11) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `eligible` tinyint(4) NOT NULL,
  `rewards` tinyint(4) NOT NULL,
  `level` tinyint(11) NOT NULL,
  `coins` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `cpk` (`user_id`),
  KEY `idx_integral` (`integral`),
  KEY `idx_eligible` (`eligible`),
  KEY `idx_touchTime` (`touch_time`),
  KEY `idx_collectTime` (`collect_time`),
  KEY `idx_collectStatus` (`collect_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `slots_integral_history`
-- ----------------------------
DROP TABLE IF EXISTS `slots_integral_history`;
CREATE TABLE `slots_integral_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `ranking` int(11) NOT NULL DEFAULT '0',
  `week_num` varchar(6) NOT NULL,
  `integral` int(11) NOT NULL,
  `activity_data` text,
  `collect_status` tinyint(4) NOT NULL,
  `collect_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `json_data` text,
  `create_time` datetime NOT NULL,
  `eligible` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_week_num` (`week_num`),
  KEY `idx_collect_time` (`collect_time`),
  KEY `idx_collect_status` (`collect_status`)
) ENGINE=InnoDB AUTO_INCREMENT=175523 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `slots_keep`
-- ----------------------------
DROP TABLE IF EXISTS `slots_keep`;
CREATE TABLE `slots_keep` (
  `create_time` date NOT NULL,
  `insert_number` bigint(20) DEFAULT '0',
  `login_number` bigint(20) DEFAULT '0',
  `tomorrow_keep` bigint(20) DEFAULT '0',
  `seven_keep` bigint(20) DEFAULT '0',
  `fifteen_keep` bigint(20) DEFAULT '0',
  `firty_keep` bigint(20) DEFAULT '0',
  PRIMARY KEY (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `slots_product`
-- ----------------------------
DROP TABLE IF EXISTS `slots_product`;
CREATE TABLE `slots_product` (
  `product_id` varchar(250) NOT NULL,
  `description` varchar(250) NOT NULL,
  `amount` decimal(8,2) NOT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_activity`
-- ----------------------------
DROP TABLE IF EXISTS `user_activity`;
CREATE TABLE `user_activity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `action` varchar(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `app_version` varchar(20) NOT NULL,
  `device_id` varchar(50) NOT NULL,
  `engine_id` varchar(20) NOT NULL,
  `ip` varchar(40) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5505792 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_activity_history`
-- ----------------------------
DROP TABLE IF EXISTS `user_activity_history`;
CREATE TABLE `user_activity_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `action` varchar(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `app_version` varchar(20) NOT NULL,
  `device_id` varchar(50) NOT NULL,
  `engine_id` varchar(20) NOT NULL,
  `ip` varchar(40) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=664659 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
