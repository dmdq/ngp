/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : coc

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:08 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `app_message`
-- ----------------------------
DROP TABLE IF EXISTS `app_message`;
CREATE TABLE `app_message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `from_uid` bigint(20) NOT NULL,
  `to_uid` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `attach` varchar(255) DEFAULT NULL,
  `attach_status` tinyint(4) DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `battle_log`
-- ----------------------------
DROP TABLE IF EXISTS `battle_log`;
CREATE TABLE `battle_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `attacker` bigint(20) NOT NULL,
  `defenser` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `base_data` text,
  `json_data` text NOT NULL,
  `battle_time` datetime NOT NULL,
  `duration` bigint(20) NOT NULL,
  `tm` bigint(20) DEFAULT NULL,
  `trophy` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_attacker` (`attacker`),
  KEY `idx_tm` (`tm`)
) ENGINE=InnoDB AUTO_INCREMENT=317736 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `battle_log_history`
-- ----------------------------
DROP TABLE IF EXISTS `battle_log_history`;
CREATE TABLE `battle_log_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `attacker` bigint(20) NOT NULL,
  `defenser` bigint(20) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `json_data` text NOT NULL,
  `battle_time` datetime NOT NULL,
  `duration` bigint(20) NOT NULL,
  `tm` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_attacker` (`attacker`),
  KEY `idx_tm` (`tm`)
) ENGINE=InnoDB AUTO_INCREMENT=903 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `coc_clan`
-- ----------------------------
DROP TABLE IF EXISTS `coc_clan`;
CREATE TABLE `coc_clan` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `icon_urn` varchar(255) DEFAULT NULL,
  `notice` varchar(255) DEFAULT NULL,
  `creator` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `type` tinyint(4) NOT NULL,
  `trophy` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `coc_clan_user`
-- ----------------------------
DROP TABLE IF EXISTS `coc_clan_user`;
CREATE TABLE `coc_clan_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clan_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`clan_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `coc_ios_order`
-- ----------------------------
DROP TABLE IF EXISTS `coc_ios_order`;
CREATE TABLE `coc_ios_order` (
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
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `coc_keep`
-- ----------------------------
DROP TABLE IF EXISTS `coc_keep`;
CREATE TABLE `coc_keep` (
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
--  Table structure for `coc_product`
-- ----------------------------
DROP TABLE IF EXISTS `coc_product`;
CREATE TABLE `coc_product` (
  `product_id` varchar(250) NOT NULL,
  `quantity` bigint(50) NOT NULL,
  `amount` decimal(8,2) NOT NULL,
  PRIMARY KEY (`product_id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=435 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=5505677 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=662995 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_data`
-- ----------------------------
DROP TABLE IF EXISTS `user_data`;
CREATE TABLE `user_data` (
  `user_id` bigint(20) unsigned NOT NULL,
  `nick` varchar(20) DEFAULT NULL,
  `status_detail` varchar(255) DEFAULT NULL,
  `json_data` text NOT NULL,
  `trophy` int(11) NOT NULL,
  `shield_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `touch_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `action_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `search_lock` tinyint(4) NOT NULL,
  `lock_time` timestamp NULL DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `coin` bigint(20) NOT NULL,
  `level` int(11) NOT NULL,
  `town_level` int(11) NOT NULL,
  `avatar_urn` varchar(150) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `ulld` varchar(40) DEFAULT NULL,
  `gem` int(11) DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_data_bak`
-- ----------------------------
DROP TABLE IF EXISTS `user_data_bak`;
CREATE TABLE `user_data_bak` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `nick` varchar(20) DEFAULT NULL,
  `status_detail` varchar(255) DEFAULT NULL,
  `json_data` text NOT NULL,
  `trophy` int(11) NOT NULL,
  `shield_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `touch_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `action_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `search_lock` tinyint(4) NOT NULL,
  `lock_time` timestamp NULL DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `coin` bigint(20) NOT NULL,
  `level` int(11) NOT NULL,
  `town_level` int(11) NOT NULL,
  `avatar_urn` varchar(150) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `ulld` varchar(40) DEFAULT NULL,
  `gem` bigint(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_trophy` (`trophy`) USING BTREE,
  KEY `idx_shieldTime` (`shield_time`),
  KEY `idx_actionTime` (`action_time`),
  KEY `idx_searchLock` (`search_lock`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_friend`
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `fa` bigint(20) NOT NULL COMMENT 'A方，好友关系发起方，user表的id',
  `fb` bigint(20) NOT NULL COMMENT 'B方，被加好友方，user表的id',
  `fs` tinyint(4) NOT NULL COMMENT '好友关系，0，B方未确认；1，B方已确认',
  `create_time` datetime NOT NULL,
  `msg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
