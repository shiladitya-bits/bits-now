-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 28, 2013 at 09:23 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bitsnow_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE IF NOT EXISTS `courses` (
  `cid` varchar(10) NOT NULL DEFAULT '',
  `cname` varchar(255) DEFAULT NULL,
  `did` int(11) NOT NULL,
  `ic_id` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`cid`),
  KEY `did` (`did`),
  KEY `ic_id` (`ic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`cid`, `cname`, `did`, `ic_id`) VALUES
('cs c342', 'software engineering', 1, NULL),
('is c345', 'database systems', 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE IF NOT EXISTS `departments` (
  `depId` int(11) NOT NULL AUTO_INCREMENT,
  `depName` varchar(255) NOT NULL,
  `depMgr` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`depId`),
  KEY `depMgr` (`depMgr`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `departments`
--

INSERT INTO `departments` (`depId`, `depName`, `depMgr`) VALUES
(1, 'cs/is', NULL),
(2, 'eee', NULL),
(3, 'eni', NULL),
(4, 'mechanical', NULL),
(5, 'chemical', NULL),
(6, 'physics', NULL),
(7, 'chemistry', NULL),
(8, 'biology', NULL),
(9, 'economics', NULL),
(10, 'mathematics', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `registered`
--

CREATE TABLE IF NOT EXISTS `registered` (
  `sid` varchar(12) NOT NULL,
  `tid` varchar(12) NOT NULL,
  `cid` varchar(10) NOT NULL DEFAULT '',
  `sem` int(11) NOT NULL,
  PRIMARY KEY (`sid`,`tid`,`cid`),
  KEY `tid` (`tid`),
  KEY `cid` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registered`
--

INSERT INTO `registered` (`sid`, `tid`, `cid`, `sem`) VALUES
('2010c6ps632g', '2004cs100g', 'is c345', 6),
('2010c6ps632g', '2008cs120g', 'cs c342', 6);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE IF NOT EXISTS `students` (
  `bitsid` varchar(12) NOT NULL,
  `name` varchar(255) NOT NULL,
  `hostel` varchar(5) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  `did` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`bitsid`),
  KEY `did` (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`bitsid`, `name`, `hostel`, `room`, `email`, `address`, `contact`, `did`, `password`) VALUES
('2010c6ps632g', 'shiladitya', 'ah7', 309, 'f2010632', '48/e jm lane kolkata 700027', '2147483647', 1, '1234567');

-- --------------------------------------------------------

--
-- Table structure for table `teachers`
--

CREATE TABLE IF NOT EXISTS `teachers` (
  `tid` varchar(12) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  `did` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`tid`),
  KEY `did` (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teachers`
--

INSERT INTO `teachers` (`tid`, `name`, `email`, `address`, `contact`, `did`, `password`) VALUES
('2004cs100g', 'bharat deshpande', 'bmd', 'block 34, sector A, bits pilani goa', '1111111111', 1, ''),
('2008cs120g', 'mahadev gawas', 'gawas', 'block 10, sector A, bits pilani goa', '1010101010', 1, '');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`did`) REFERENCES `departments` (`depId`),
  ADD CONSTRAINT `courses_ibfk_2` FOREIGN KEY (`ic_id`) REFERENCES `teachers` (`tid`),
  ADD CONSTRAINT `courses_ibfk_3` FOREIGN KEY (`ic_id`) REFERENCES `teachers` (`tid`);

--
-- Constraints for table `departments`
--
ALTER TABLE `departments`
  ADD CONSTRAINT `departments_ibfk_1` FOREIGN KEY (`depMgr`) REFERENCES `teachers` (`tid`);

--
-- Constraints for table `registered`
--
ALTER TABLE `registered`
  ADD CONSTRAINT `registered_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `students` (`bitsid`),
  ADD CONSTRAINT `registered_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `teachers` (`tid`),
  ADD CONSTRAINT `registered_ibfk_3` FOREIGN KEY (`cid`) REFERENCES `courses` (`cid`);

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `students_ibfk_1` FOREIGN KEY (`did`) REFERENCES `departments` (`depId`);

--
-- Constraints for table `teachers`
--
ALTER TABLE `teachers`
  ADD CONSTRAINT `teachers_ibfk_1` FOREIGN KEY (`did`) REFERENCES `departments` (`depId`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
