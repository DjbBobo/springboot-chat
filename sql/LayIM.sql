/*
 Navicat Premium Data Transfer

 Source Server         : chat
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 39.97.241.159:3306
 Source Schema         : LayIM

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 03/07/2020 00:59:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'root', '1234');

-- ----------------------------
-- Table structure for chat_msg_friend
-- ----------------------------
DROP TABLE IF EXISTS `chat_msg_friend`;
CREATE TABLE `chat_msg_friend`  (
  `id` bigint(20) NOT NULL,
  `from` bigint(20) NULL DEFAULT NULL,
  `to` bigint(20) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sign` bigint(10) NOT NULL DEFAULT 0 COMMENT '0:未签收  1:已签收',
  `time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_msg_friend
-- ----------------------------
INSERT INTO `chat_msg_friend` VALUES (1278732409591930880, 2, 1, '你好', 1, 1593708538802);

-- ----------------------------
-- Table structure for chat_msg_group
-- ----------------------------
DROP TABLE IF EXISTS `chat_msg_group`;
CREATE TABLE `chat_msg_group`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL,
  `from` bigint(20) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friend_group_rel
-- ----------------------------
DROP TABLE IF EXISTS `friend_group_rel`;
CREATE TABLE `friend_group_rel`  (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NULL DEFAULT NULL,
  `friend_group_id` bigint(20) NULL DEFAULT NULL,
  `fid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friend_group_rel
-- ----------------------------
INSERT INTO `friend_group_rel` VALUES (127428561677055712, 3, 3, 1);
INSERT INTO `friend_group_rel` VALUES (1274285616770586712, 1, 1, 3);
INSERT INTO `friend_group_rel` VALUES (1276878801321684992, 1, 1, 2);
INSERT INTO `friend_group_rel` VALUES (1276878801833390080, 2, 2, 1);

-- ----------------------------
-- Table structure for friend_request
-- ----------------------------
DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE `friend_request`  (
  `id` bigint(20) NOT NULL,
  `my_id` bigint(20) NULL DEFAULT NULL,
  `friend_id` bigint(20) NULL DEFAULT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(10) NULL DEFAULT 0 COMMENT '0:未读 1:已读 2:同意 3:拒绝',
  `type` int(10) NULL DEFAULT NULL COMMENT '0:好友 1:群组',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_msg_sign
-- ----------------------------
DROP TABLE IF EXISTS `group_msg_sign`;
CREATE TABLE `group_msg_sign`  (
  `id` bigint(20) NOT NULL,
  `group_msg_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `to` bigint(20) NULL DEFAULT NULL,
  `sign` int(10) NOT NULL DEFAULT 0 COMMENT '0: 未签收 1:签收',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`  (
  `id` bigint(20) NOT NULL,
  `groupname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `owner_uid` bigint(20) NULL DEFAULT NULL,
  `sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES (1, '交流群', 'http://tp2.sinaimg.cn/2211874245/180/40050524279/0', 1, '我是交流群');

-- ----------------------------
-- Table structure for qq_user
-- ----------------------------
DROP TABLE IF EXISTS `qq_user`;
CREATE TABLE `qq_user`  (
  `qq_account` bigint(64) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`qq_account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 805898753 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL,
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sign` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实的状态',
  `fakestatus` bigint(10) NULL DEFAULT NULL COMMENT '0:在线 1:隐身',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123', '123', '徐小峥', '代码在囧途，也要写到底!', 'http://tp2.sinaimg.cn/1783286485/180/5677568891/1', 'offline', 1);
INSERT INTO `user` VALUES (2, '321', '321', '贤心', '我是贤心!', 'http://tva2.sinaimg.cn/crop.0.0.180.180.180/5db11ff4jw1e8qgp5bmzyj2050050aa8.jpg', 'offline', 1);
INSERT INTO `user` VALUES (3, 'root', '123', 'a小云', '我瘋了！這也太準了吧  超級笑點低', 'http://tva1.sinaimg.cn/crop.0.0.180.180.180/7fde8b93jw1e8qgp5bmzyj2050050aa8.jpg', 'offline', 1);

-- ----------------------------
-- Table structure for user_friend_group
-- ----------------------------
DROP TABLE IF EXISTS `user_friend_group`;
CREATE TABLE `user_friend_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NULL DEFAULT NULL,
  `groupname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_friend_group
-- ----------------------------
INSERT INTO `user_friend_group` VALUES (1, 1, '好友');
INSERT INTO `user_friend_group` VALUES (2, 2, '好友');
INSERT INTO `user_friend_group` VALUES (3, 3, '好友');

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group`  (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NULL DEFAULT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
