-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 23, 2015 at 06:32 PM
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

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE IF NOT EXISTS `cities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`id`, `city_name`, `description`, `lat`, `lng`) VALUES
(1, 'delhi', 'Delhi, officially the National Capital Territory of Delhi, is the Capital territory of India. During the British Raj, Delhi was part of the province of Punjab and is still historically and culturally connected to the Punjab region.', 28.6139, 77.209),
(2, 'mumbai', 'Mumbai, formerly called Bombay, is a sprawling, densely populated city on Indiaâ€™s west coast. On the Mumbai Harbour waterfront stands the iconic Gateway of India stone arch, built by the British Raj in 1924. Offshore, nearby Elephanta Island holds ancient cave temples dedicated to Shiva. The city is also famous as the heart of the Hindi-language Bollywood film industry.', 19.076, 72.8777),
(3, 'bangalore', 'Bengaluru (aka Bangalore) is the capital of India''s southern Karnataka state. The center of India''s high-tech industry, the city is also known for its green spaces and nightlife. Near Cubbon Park, Vidhana Soudha is its massive Neo-Dravidian legislative building. Former royal residences include 19th-century Bangalore Palace, modeled after England''s Windsor Castle, and Tipu Sultan''s Summer Palace, an 18th-century teak structure.', 12.9538, 77.3507),
(5, 'Chennai', 'Chennai, on the Bay of Bengal in eastern India, is the capital of the Tamil Nadu state. It''s home to Fort St. George, built in 1644 and now a museum showcasing the city''s roots as a British military garrison and East India Company trading outpost. Religious sites include Kapaleeshwarar Temple, adorned with carved and painted gods, and St. Mary''s, a 17th-century Anglican church.', 13.0475, 80.0689),
(6, 'kolkata', 'Kolkata (formerly Calcutta) is the capital of India''s West Bengal state. Founded as an East India Company trading post, it was India''s capital under the British Raj from 1773-1911. Today itâ€™s known for its grand colonial architecture, art galleries and cultural festivals. Itâ€™s also home to Mother House, headquarters of the Missionaries of Charity, founded by Mother Teresa, whose tomb is on site.', 22.5726, 88.3639),
(7, 'hyderabad', 'Hyderabad is the capital of southern India''s Telangana state. A major center for the technology industry, it''s home to many upscale restaurants and shops. Its historic sites include Golconda Fort, a former diamond-trading center that was once the Qutb Shahi dynastic capital. The Charminar, a 16th-century mosque whose 4 arches support towering minarets, is an old city landmark near the long-standing Laad Bazaar.', 17.385, 78.4867),
(8, 'ahmedabad', 'Ahmedabad, in western India, is the largest city in the state of Gujarat. The Sabarmati River runs through its center. On the western bank is the Gandhi Ashram at Sabarmati, which displays the spiritual leaderâ€™s living quarters and artifacts. Across the river, the Calico Museum of Textiles, once a cloth merchant''s mansion, has a significant collection of antique and modern fabrics.', 23.0225, 72.5714),
(9, 'pune', 'Pune, a sprawling city in the Indian state of Maharashtra, was once the base of the peshwas (prime ministers) of the Maratha Empire. It''s known for the grand Aga Khan Palace, built in 1892 and now a memorial to Mahatma Gandhi, whose ashes are preserved in the garden. The 8th-century Pataleshwar Cave Temple is dedicated to the Hindu god Shiva.', 18.5204, 73.8567),
(10, 'jaipur', 'Jaipur, the capital of Indiaâ€™s Rajasthan state, evokes the royal family that once ruled the region and that, in 1727, founded what is now called the Old City, or â€œPink Cityâ€ for its trademark building color. At the center of its stately street grid (notable in India) stands the opulent, collonaded City Palace complex, which today houses several museum collections of textiles and art.', 26.9124, 75.7873),
(11, 'visakhapatnam', 'Visakhapatnam is the largest city, both in terms of area and population in the Indian state of Andhra Pradesh.', 17.6868, 83.2185),
(12, 'goa', 'Goa is a state in western India with coastlines stretching along the Arabian Sea. Its long history as a Portuguese colony prior to 1961 is evident in its preserved 16th-century churches and the areaâ€™s tropical spice plantations. Goa is also known for its beaches, ranging from popular stretches at Baga and Palolem to laid-back fishing villages such as Agonda.', 15.2993, 74.124);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `email` varchar(500) NOT NULL,
  `password` varchar(200) NOT NULL,
  `active` int(11) NOT NULL DEFAULT '0',
  `mood` int(5) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `password`, `active`, `mood`) VALUES
(1, 'Prabhakar Gupta', 'prabhakargupta267@gmail.com', '71ee9f9342eb0a66a10238c978e26ff8', 1, 2),
(7, 'prabhakar', 'prabhakar@asd.asd', '040b7cf4a55014e185813e0644502ea9', 0, 17),
(3, 'Swati', 'swati@gmail.com', 'f823937995b27ffcbf668da4a22d5bd3', 0, 0),
(5, 'Swati Garg', 'swati4@gmail.com', 'f823937995b27ffcbf668da4a22d5bd3', 1, 60),
(6, 'pl', 'pl@pl', 'e8879c79275b04769b779e2f14fab76c', 1, 0),
(8, 'Suryansh', 'suryansh.tibarewal@gmail.com', 'f62bc6c62003b2fee334ac06cdf14426', 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
