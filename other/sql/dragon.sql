/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : dragon

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:14 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `dragon_clan`
-- ----------------------------
DROP TABLE IF EXISTS `dragon_clan`;
CREATE TABLE `dragon_clan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL COMMENT ' 名称',
  `creator` bigint(20) NOT NULL COMMENT '会长ID',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `notice` varchar(255) DEFAULT NULL,
  `icon_urn` varchar(100) DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  `limits` varchar(200) DEFAULT NULL COMMENT '入会条件，json对象',
  `mem_num` int(11) NOT NULL COMMENT '帮会成员数',
  `level` int(11) NOT NULL COMMENT '帮会等级',
  `max_num` int(11) NOT NULL COMMENT '人数上限',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `dragon_clan_user`
-- ----------------------------
DROP TABLE IF EXISTS `dragon_clan_user`;
CREATE TABLE `dragon_clan_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `clan_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cpk` (`clan_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `dragon_data`
-- ----------------------------
DROP TABLE IF EXISTS `dragon_data`;
CREATE TABLE `dragon_data` (
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
--  Table structure for `dragon_message`
-- ----------------------------
DROP TABLE IF EXISTS `dragon_message`;
CREATE TABLE `dragon_message` (
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
  `active` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消息具体类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1002490 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=5505719 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=662863 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=424 DEFAULT CHARSET=utf8;

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
