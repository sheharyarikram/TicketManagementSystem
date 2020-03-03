CREATE TABLE ticket (
  --id			INTEGER AUTO_INCREMENT PRIMARY KEY,
  id			INTEGER PRIMARY KEY,
  description	VARCHAR(255) NOT NULL,
  status 		VARCHAR(255) NOT NULL,
  assignee		VARCHAR(255) NOT NULL,
  created_date 	DATE NOT NULL,
  closed_date 	DATE
  );
	