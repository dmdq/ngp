/*
 Navicat Premium Data Transfer

 Source Server         : 42.120.9.103
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 42.120.9.103
 Source Database       : ngp_user

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 04/25/2014 09:56:29 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `login_user`
-- ----------------------------
DROP TABLE IF EXISTS `login_user`;
CREATE TABLE `login_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `type` int(4) DEFAULT NULL COMMENT '用户类型',
  `status` int(4) DEFAULT NULL COMMENT '状态',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
