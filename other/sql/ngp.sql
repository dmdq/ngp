/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : ngp

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:23 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `aaa`
-- ----------------------------
DROP TABLE IF EXISTS `aaa`;
CREATE TABLE `aaa` (
  `name` varchar(50) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app`
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `base_id` varchar(50) NOT NULL,
  `base_name` varchar(100) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `icon_urn` varchar(100) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `status_desc` varchar(200) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL,
  `data_base` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_base_id` (`base_id`),
  KEY `idx_name` (`name`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_config`
-- ----------------------------
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `app_id` varchar(50) NOT NULL,
  `app_name` varchar(100) DEFAULT NULL,
  `json_all` text,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_config_base`
-- ----------------------------
DROP TABLE IF EXISTS `app_config_base`;
CREATE TABLE `app_config_base` (
  `app_id` varchar(50) NOT NULL,
  `app_name` varchar(100) DEFAULT NULL,
  `json_all` text,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_file`
-- ----------------------------
DROP TABLE IF EXISTS `app_file`;
CREATE TABLE `app_file` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `urn` varchar(100) NOT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_version`
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) NOT NULL,
  `version` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_zone`
-- ----------------------------
DROP TABLE IF EXISTS `app_zone`;
CREATE TABLE `app_zone` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) NOT NULL,
  `is_baid` tinyint(4) NOT NULL,
  `zone_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `avatar_file`
-- ----------------------------
DROP TABLE IF EXISTS `avatar_file`;
CREATE TABLE `avatar_file` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `urn` varchar(100) NOT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `coin_history`
-- ----------------------------
DROP TABLE IF EXISTS `coin_history`;
CREATE TABLE `coin_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `incrmt` bigint(20) NOT NULL,
  `incrmt_key` varchar(255) NOT NULL,
  `app_id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `device`
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` varchar(36) NOT NULL,
  `mac_id` varchar(40) NOT NULL DEFAULT 'macId',
  `idfa` varchar(40) NOT NULL DEFAULT 'idfa',
  `mac` varchar(100) DEFAULT NULL,
  `brand` varchar(100) DEFAULT NULL,
  `model` varchar(100) DEFAULT NULL,
  `os` varchar(50) DEFAULT NULL,
  `udid` varchar(200) DEFAULT NULL,
  `open_udid` varchar(200) DEFAULT NULL,
  `json_all` text,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_macId` (`mac_id`),
  KEY `idx_idfa` (`idfa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `engine`
-- ----------------------------
DROP TABLE IF EXISTS `engine`;
CREATE TABLE `engine` (
  `id` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `rated` bigint(20) NOT NULL,
  `loading` bigint(20) NOT NULL,
  `host` varchar(200) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `status_desc` varchar(200) DEFAULT NULL,
  `status_lock` smallint(6) NOT NULL,
  `touch_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `file`
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `urn` varchar(100) NOT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `from_uid` bigint(20) NOT NULL,
  `to_uid` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `attach` varchar(255) DEFAULT NULL,
  `attach_status` tinyint(4) DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` varchar(50) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `login_name` varchar(50) NOT NULL,
  `amount` varchar(50) NOT NULL,
  `status` varchar(4) NOT NULL,
  `pay_time` varchar(50) NOT NULL,
  `device_id` varchar(50) NOT NULL,
  `pid` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `json_data` text,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `placard`
-- ----------------------------
DROP TABLE IF EXISTS `placard`;
CREATE TABLE `placard` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `title_size` varchar(20) DEFAULT NULL,
  `title_color` varchar(20) DEFAULT NULL,
  `body` text,
  `body_size` varchar(20) DEFAULT NULL,
  `body_color` varchar(20) DEFAULT NULL,
  `status` smallint(6) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `placard_target`
-- ----------------------------
DROP TABLE IF EXISTS `placard_target`;
CREATE TABLE `placard_target` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `placard_id` bigint(20) NOT NULL,
  `app_id` varchar(20) DEFAULT NULL,
  `app_version` varchar(20) DEFAULT NULL,
  `is_baid` tinyint(4) DEFAULT NULL,
  `zone_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `promo`
-- ----------------------------
DROP TABLE IF EXISTS `promo`;
CREATE TABLE `promo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  `icon_urn` varchar(255) NOT NULL,
  `idx` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_turn` (`idx`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `promo_count`
-- ----------------------------
DROP TABLE IF EXISTS `promo_count`;
CREATE TABLE `promo_count` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `promo_id` int(11) NOT NULL,
  `click_count` bigint(20) NOT NULL,
  `count_date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `promo_file`
-- ----------------------------
DROP TABLE IF EXISTS `promo_file`;
CREATE TABLE `promo_file` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `urn` varchar(100) NOT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `res_file`
-- ----------------------------
DROP TABLE IF EXISTS `res_file`;
CREATE TABLE `res_file` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `urn` varchar(100) NOT NULL,
  `data` longblob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `res_update`
-- ----------------------------
DROP TABLE IF EXISTS `res_update`;
CREATE TABLE `res_update` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_id` varchar(20) NOT NULL,
  `old_version` int(11) NOT NULL,
  `new_version` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `res_update_file`
-- ----------------------------
DROP TABLE IF EXISTS `res_update_file`;
CREATE TABLE `res_update_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(200) NOT NULL,
  `file_urn` varchar(200) NOT NULL,
  `ru_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1830 DEFAULT CHARSET=utf8;

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
  PRIMARY KEY (`id`),
  KEY `idx_appId` (`app_id`),
  KEY `idx_deviceId` (`device_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3210612 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=170222 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sns_data`
-- ----------------------------
DROP TABLE IF EXISTS `sns_data`;
CREATE TABLE `sns_data` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_key` varchar(100) NOT NULL,
  `key_type` varchar(20) NOT NULL,
  `sns_json` text,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sns_friend`
-- ----------------------------
DROP TABLE IF EXISTS `sns_friend`;
CREATE TABLE `sns_friend` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `fa_uid` bigint(20) NOT NULL,
  `fb_uk` varchar(100) NOT NULL,
  `fb_kt` varchar(20) NOT NULL,
  `fb_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `nick` varchar(100) DEFAULT NULL,
  `opswd` varchar(50) DEFAULT NULL,
  `epswd` varchar(100) DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `status_detail` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4972534 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_account`
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `user_id` bigint(20) unsigned NOT NULL,
  `coin` bigint(20) NOT NULL,
  `coin_reset` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=28051693 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=663363 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_app`
-- ----------------------------
DROP TABLE IF EXISTS `user_app`;
CREATE TABLE `user_app` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`user_id`,`app_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18919119 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_app_data`
-- ----------------------------
DROP TABLE IF EXISTS `user_app_data`;
CREATE TABLE `user_app_data` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL,
  `baid` varchar(255) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `status_detail` varchar(255) DEFAULT NULL,
  `json_data` text NOT NULL,
  `touch_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `action_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `update_time` datetime NOT NULL,
  `ulld` varchar(40) DEFAULT NULL,
  `nick` varchar(40) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`user_id`,`baid`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=419 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_friend_copy`
-- ----------------------------
DROP TABLE IF EXISTS `user_friend_copy`;
CREATE TABLE `user_friend_copy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `fa` bigint(20) NOT NULL COMMENT 'A方，好友关系发起方，user表的id',
  `fb` bigint(20) NOT NULL COMMENT 'B方，被加好友方，user表的id',
  `fs` tinyint(4) NOT NULL COMMENT '好友关系，0，B方未确认；1，B方已确认',
  `create_time` datetime NOT NULL,
  `msg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=417 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_key`
-- ----------------------------
DROP TABLE IF EXISTS `user_key`;
CREATE TABLE `user_key` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_key` varchar(100) NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `key_type` varchar(20) NOT NULL,
  `key_from` varchar(50) NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`user_key`,`key_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3213968 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_profile`
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `user_id` bigint(20) unsigned NOT NULL,
  `country` varchar(255) DEFAULT NULL,
  `age` tinyint(4) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL,
  `avatar_urn` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `json_all` text,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
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

-- ----------------------------
--  Table structure for `user_session`
-- ----------------------------
DROP TABLE IF EXISTS `user_session`;
CREATE TABLE `user_session` (
  `id` varchar(100) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `app_version` varchar(20) NOT NULL,
  `device_id` varchar(50) NOT NULL,
  `engine_id` varchar(20) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `ip` varchar(200) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_session_count`
-- ----------------------------
DROP TABLE IF EXISTS `user_session_count`;
CREATE TABLE `user_session_count` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `engine_id` varchar(20) NOT NULL,
  `count` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=148793 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `user_sns`
-- ----------------------------
DROP TABLE IF EXISTS `user_sns`;
CREATE TABLE `user_sns` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_key` varchar(100) NOT NULL,
  `key_type` varchar(20) NOT NULL,
  `sns_json` text,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1112 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `zone`
-- ----------------------------
DROP TABLE IF EXISTS `zone`;
CREATE TABLE `zone` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `recommend` tinyint(4) NOT NULL,
  `fav_engine_id` varchar(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `zone_engine`
-- ----------------------------
DROP TABLE IF EXISTS `zone_engine`;
CREATE TABLE `zone_engine` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `zone_id` bigint(10) unsigned NOT NULL,
  `engine_id` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
