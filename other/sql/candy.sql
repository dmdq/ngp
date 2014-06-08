/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : candy

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:03 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ad_track`
-- ----------------------------
DROP TABLE IF EXISTS `ad_track`;
CREATE TABLE `ad_track` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ad_appid` varchar(100) NOT NULL,
  `ad_action` varchar(20) NOT NULL,
  `app_id` varchar(100) NOT NULL,
  `device_id` varchar(100) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_time_ms` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_data`
-- ----------------------------
DROP TABLE IF EXISTS `app_data`;
CREATE TABLE `app_data` (
  `user_id` bigint(20) unsigned NOT NULL,
  `ulld` varchar(60) DEFAULT NULL,
  `nick` varchar(20) DEFAULT NULL,
  `status_detail` varchar(40) DEFAULT NULL,
  `json_data` text NOT NULL,
  `coin` bigint(20) NOT NULL DEFAULT '0',
  `touch_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `action_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `level` int(11) NOT NULL DEFAULT '0',
  `update_time` datetime NOT NULL,
  `avatar_urn` varchar(150) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `candy_device`
-- ----------------------------
DROP TABLE IF EXISTS `candy_device`;
CREATE TABLE `candy_device` (
  `id` varchar(100) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `model` varchar(50) DEFAULT NULL,
  `app_version` varchar(50) NOT NULL,
  `os_version` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `candy_top_history`
-- ----------------------------
DROP TABLE IF EXISTS `candy_top_history`;
CREATE TABLE `candy_top_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `week_num` varchar(6) NOT NULL,
  `score` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `ranking` int(11) NOT NULL,
  `json_data` text,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `event`
-- ----------------------------
DROP TABLE IF EXISTS `event`;
CREATE TABLE `event` (
  `id` int(4) NOT NULL DEFAULT '0',
  `cur_unit` varchar(20) NOT NULL COMMENT '活动周期',
  `next_unit` varchar(20) DEFAULT NULL,
  `bak_start_time` bigint(20) DEFAULT NULL,
  `refresh_start_time` bigint(20) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `cur_total_millis` bigint(20) NOT NULL,
  `next_total_millis` bigint(20) NOT NULL,
  `period` int(11) NOT NULL,
  `cur_type` tinyint(4) NOT NULL,
  `next_type` tinyint(4) NOT NULL,
  `last_type` tinyint(4) NOT NULL DEFAULT '0',
  `cur_status` tinyint(4) NOT NULL,
  `next_status` tinyint(4) NOT NULL,
  `close_unit` varchar(20) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `paypal_indent`
-- ----------------------------
DROP TABLE IF EXISTS `paypal_indent`;
CREATE TABLE `paypal_indent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) DEFAULT NULL,
  `custom` varchar(255) DEFAULT NULL,
  `total_amount` varchar(255) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `quantity` varchar(255) DEFAULT NULL,
  `payer_id` varchar(255) DEFAULT NULL,
  `payer_email` varchar(255) DEFAULT NULL,
  `receiver_email` varchar(255) DEFAULT NULL,
  `payment_date` varchar(255) DEFAULT NULL,
  `json_data` text,
  `payment_date_ms` bigint(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `paypal_order`
-- ----------------------------
DROP TABLE IF EXISTS `paypal_order`;
CREATE TABLE `paypal_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `txn_id` varchar(50) DEFAULT NULL,
  `txn_type` varchar(50) DEFAULT NULL,
  `custom` varchar(50) DEFAULT NULL,
  `business` varchar(50) DEFAULT NULL,
  `btn_id` varchar(50) DEFAULT NULL,
  `item_number` varchar(50) DEFAULT NULL,
  `item_name` varchar(50) DEFAULT NULL,
  `quantity` varchar(50) DEFAULT NULL,
  `payer_id` varchar(50) DEFAULT NULL,
  `payer_email` varchar(50) DEFAULT NULL,
  `receiver_id` varchar(50) DEFAULT NULL,
  `receiver_email` varchar(50) DEFAULT NULL,
  `payment_date` varchar(50) DEFAULT NULL,
  `ipn_track_id` varchar(50) DEFAULT NULL,
  `json_data` text,
  `payment_date_ms` bigint(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_txn_id` (`txn_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `popularize_count`
-- ----------------------------
DROP TABLE IF EXISTS `popularize_count`;
CREATE TABLE `popularize_count` (
  `id` tinyint(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `device_id` varchar(100) NOT NULL,
  `i1` tinyint(11) DEFAULT NULL,
  `i2` tinyint(11) DEFAULT NULL,
  `i3` tinyint(11) DEFAULT NULL,
  `i4` tinyint(11) DEFAULT NULL,
  `i5` tinyint(11) DEFAULT NULL,
  `i6` tinyint(11) DEFAULT NULL,
  `i7` tinyint(11) DEFAULT NULL,
  `i8` tinyint(11) DEFAULT NULL,
  `i9` tinyint(11) DEFAULT NULL,
  `i10` tinyint(11) DEFAULT NULL,
  `s1` varchar(50) DEFAULT NULL,
  `s2` varchar(50) DEFAULT NULL,
  `s3` varchar(50) DEFAULT NULL,
  `s4` varchar(50) DEFAULT NULL,
  `s5` varchar(50) DEFAULT NULL,
  `s6` varchar(50) DEFAULT NULL,
  `s7` varchar(50) DEFAULT NULL,
  `s8` varchar(50) DEFAULT NULL,
  `s9` varchar(50) DEFAULT NULL,
  `s10` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_i1` (`i1`),
  KEY `idx_i2` (`i2`),
  KEY `idx_i3` (`i3`),
  KEY `idx_i4` (`i4`),
  KEY `idx_i5` (`i5`),
  KEY `idx_s1` (`s1`),
  KEY `idx_s2` (`s2`),
  KEY `idx_s3` (`s3`),
  KEY `idx_s4` (`s4`),
  KEY `idx_s5` (`s5`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` varchar(50) NOT NULL,
  `quantity` bigint(50) NOT NULL,
  `amount` decimal(8,2) NOT NULL,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `picture` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `push_token`
-- ----------------------------
DROP TABLE IF EXISTS `push_token`;
CREATE TABLE `push_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) NOT NULL,
  `device_id` varchar(200) NOT NULL,
  `token` varchar(200) NOT NULL,
  `OS` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`app_id`,`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=14837 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=5505787 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=663481 DEFAULT CHARSET=utf8;

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
