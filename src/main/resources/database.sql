-- Create the DEPARTMENT table with an ID and NAME column
CREATE TABLE DEPARTMENT
(
    ID   INT,
    NAME VARCHAR2(30) NOT NULL UNIQUE -- Ensure NAME is unique
);

-- Set the ID column to not null
ALTER TABLE DEPARTMENT
    ALTER COLUMN ID SET NOT NULL;

-- Add a primary key constraint to the ID column
ALTER TABLE DEPARTMENT
    ADD CONSTRAINT PK_DEPARTMENT PRIMARY KEY (ID);

-- Set the ID column to auto-increment
ALTER TABLE DEPARTMENT
    ALTER COLUMN ID INT AUTO_INCREMENT;

-- Create the EMPLOYEE table with an ID, FIRST_NAME, LAST_NAME, and D_ID column
CREATE TABLE EMPLOYEE
(
    ID         INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME VARCHAR2(30) NOT NULL,
    LAST_NAME  VARCHAR2(30) NOT NULL,
    D_ID       INT          NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (D_ID) REFERENCES DEPARTMENT (ID)
);

-- Add a foreign key constraint to the D_ID column referencing the DEPARTMENT table
ALTER TABLE EMPLOYEE
    ADD CONSTRAINT FK_EMPLOYEE_DEPARTMENT FOREIGN KEY (D_ID) REFERENCES DEPARTMENT (ID);

-- Insert sample data into the DEPARTMENT and EMPLOYEE tables
INSERT INTO DEPARTMENT (NAME)
VALUES ('Sales'),
       ('Marketing'),
       ('Development'),
       ('HR'),
       ('Finance');

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, D_ID)
VALUES ('John', 'Doe', SELECT ID FROM DEPARTMENT WHERE NAME = 'Sales'),
       ('Jane', 'Smith', SELECT ID FROM DEPARTMENT WHERE NAME = 'Marketing'),
       ('Alice', 'Johnson', SELECT ID FROM DEPARTMENT WHERE NAME = 'Development'),
       ('Bob', 'Brown', SELECT ID FROM DEPARTMENT WHERE NAME = 'HR'),
       ('Charlie', 'Davis', SELECT ID FROM DEPARTMENT WHERE NAME = 'Finance');