-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 22, 2016 at 10:21 AM
-- Server version: 5.6.22-72.0-log
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `csinseew_pg_tie`
--
CREATE DATABASE IF NOT EXISTS `csinseew_pg_tie` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `csinseew_pg_tie`;

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
CREATE TABLE IF NOT EXISTS `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `number_of_trips` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35 ;

-- --------------------------------------------------------

--
-- Table structure for table `city_facts`
--

DROP TABLE IF EXISTS `city_facts`;
CREATE TABLE IF NOT EXISTS `city_facts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) NOT NULL,
  `fact` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=73 ;

-- --------------------------------------------------------

--
-- Table structure for table `city_images`
--

DROP TABLE IF EXISTS `city_images`;
CREATE TABLE IF NOT EXISTS `city_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) NOT NULL,
  `image_url` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=37 ;

-- --------------------------------------------------------

--
-- Table structure for table `monuments`
--

DROP TABLE IF EXISTS `monuments`;
CREATE TABLE IF NOT EXISTS `monuments` (
  `monument_id` int(11) NOT NULL AUTO_INCREMENT,
  `monument_name` varchar(200) NOT NULL,
  `monument_description` text,
  `monument_image` varchar(500) NOT NULL,
  `city_id` int(11) NOT NULL,
  PRIMARY KEY (`monument_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `monuments_estimote`
--

DROP TABLE IF EXISTS `monuments_estimote`;
CREATE TABLE IF NOT EXISTS `monuments_estimote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `UID` varchar(40) NOT NULL,
  `major` int(10) NOT NULL,
  `minor` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `trips`
--

DROP TABLE IF EXISTS `trips`;
CREATE TABLE IF NOT EXISTS `trips` (
  `trip_id` int(11) NOT NULL AUTO_INCREMENT,
  `city_id` int(11) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `start_time` int(11) NOT NULL,
  `end_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`trip_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=28 ;

-- --------------------------------------------------------

--
-- Table structure for table `trip_images`
--

DROP TABLE IF EXISTS `trip_images`;
CREATE TABLE IF NOT EXISTS `trip_images` (
  `trip_image_id` int(11) NOT NULL AUTO_INCREMENT,
  `trip_id` int(11) NOT NULL,
  `image_url` varchar(500) NOT NULL,
  PRIMARY KEY (`trip_image_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `trip_users`
--

DROP TABLE IF EXISTS `trip_users`;
CREATE TABLE IF NOT EXISTS `trip_users` (
  `trip_user_id` int(11) NOT NULL AUTO_INCREMENT,
  `trip_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`trip_user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=29 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `contact` varchar(20) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contact` (`contact`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
