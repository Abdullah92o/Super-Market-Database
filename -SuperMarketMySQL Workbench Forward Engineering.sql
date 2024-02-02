- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema SuperMarket
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SuperMarket
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SuperMarket` DEFAULT CHARACTER SET utf8 ;
USE `SuperMarket` ;

-- -----------------------------------------------------
-- Table `SuperMarket`.`Supplier`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`Supplier` (
  `SupplierId` INT NOT NULL,
  `Email` VARCHAR(45) NULL,
  `ContactName` VARCHAR(45) NULL,
  PRIMARY KEY (`SupplierId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Warehouse`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`Warehouse` (
  `Address` VARCHAR(45) NOT NULL,
  `WarehouseName` VARCHAR(45) NULL,
  `Capacity` INT NULL,
  PRIMARY KEY (`Address`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Proudct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`Proudct` (
  `ProudctId` INT NOT NULL AUTO_INCREMENT,
  `ProudctName` VARCHAR(45) NULL,
  `QuantityInStock` INT NULL,
  `Category` VARCHAR(45) NULL,
  `Enddate` DATE NULL,
  `Price` FLOAT NULL,
  `Warehouse_Address` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ProudctId`),
  CONSTRAINT `fk_Proudct_Warehouse1`
    FOREIGN KEY (`Warehouse_Address`)
    REFERENCES `SuperMarket`.`Warehouse` (`Address`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`Customer` (
  `CustomerId` INT NOT NULL,
  `FirstName` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NULL,
  `Address` VARCHAR(45) NULL,
  `Email` VARCHAR(45) NULL,
  `Phone` INT NULL,
  PRIMARY KEY (`CustomerId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`LoyaltyCard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`LoyaltyCard` (
  `CardNumber` INT NOT NULL,
  `Points` INT NULL,
  `Customer_CustomerId` INT NOT NULL,
  PRIMARY KEY (`CardNumber`),
  CONSTRAINT `fk_LoyaltyCard_Customer1`
    FOREIGN KEY (`Customer_CustomerId`)
    REFERENCES `SuperMarket`.`Customer` (`CustomerId`)
    ON DELETE  CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`. `ordes` (
	`OrderId` INT(11) NOT NULL,
	`TotalAmount` FLOAT NULL DEFAULT NULL,
	`Date` DATE NULL DEFAULT NULL,
	`IsPaid` TINYINT(4) NULL DEFAULT NULL COMMENT 'MySQL does not have a dedicated Boolean data type like some other database systems do (e.g., PostgreSQL has a BOOLEAN data type). Instead, MySQL typically uses TINYINT to represent boolean values. This is a common practice in relational database systems.\',
	`Customer_CustomerId` INT(11) NOT NULL,
	PRIMARY KEY (`OrderId`, `Customer_CustomerId`) USING BTREE,
	INDEX `fk_Order_Customer1` (`Customer_CustomerId`) USING BTREE,
	CONSTRAINT `fk_Order_Customer1` FOREIGN KEY (`Customer_CustomerId`) REFERENCES `customer` (`CustomerId`) ON UPDATE NO ACTION ON DELETE CASCADE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Supplier supplies Proudct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`Supplier supplies Proudct` (
  `Proudct_ProudctId` INT NOT NULL,
  `Supplier_SupplierId` INT NOT NULL,
  `UnitCost` FLOAT NULL,
  PRIMARY KEY (`Proudct_ProudctId`, `Supplier_SupplierId`),
  CONSTRAINT `fk_Proudct_has_Supplier_Proudct1`
    FOREIGN KEY (`Proudct_ProudctId`)
    REFERENCES `SuperMarket`.`Proudct` (`ProudctId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Proudct_has_Supplier_Supplier1`
    FOREIGN KEY (`Supplier_SupplierId`)
    REFERENCES `SuperMarket`.`Supplier` (`SupplierId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SuperMarket`.`Order contain Proudct`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`order contain proudct` (
	`Proudct_ProudctId` INT(11) NOT NULL,
	`Order_OrderId` INT(11) NOT NULL,
	`Order_Custumer_CustomerId` INT(11) NOT NULL,
	`Quantity` VARCHAR(45) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	PRIMARY KEY (`Proudct_ProudctId`, `Order_OrderId`, `Order_Custumer_CustomerId`) USING BTREE,
	INDEX `fk_Proudct_has_Order_Order1` (`Order_OrderId`, `Order_Custumer_CustomerId`) USING BTREE,
	CONSTRAINT `fk_Proudct_has_Order_Order1` FOREIGN KEY (`Order_OrderId`, `Order_Custumer_CustomerId`) REFERENCES `ordes` (`OrderId`, `Customer_CustomerId`) ON UPDATE NO ACTION ON DELETE CASCADE,
	CONSTRAINT `fk_Proudct_has_Order_Proudct1` FOREIGN KEY (`Proudct_ProudctId`) REFERENCES `proudct` (`ProudctId`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- -----------------------------------------------------
-- Placeholder table for view `SuperMarket`.`view1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SuperMarket`.`view1` (`id` INT);

-- -----------------------------------------------------
-- View `SuperMarket`.`view1`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SuperMarket`.`view1`;
USE `SuperMarket`;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
