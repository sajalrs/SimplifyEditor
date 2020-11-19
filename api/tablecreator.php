<?php
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "simplify";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

// sql to create table
$sql = "CREATE TABLE words (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
word VARCHAR(50) NOT NULL,
frequency INT(6) NOT NULL
)";

if ($conn->query($sql) === TRUE) {
  echo "Table words created successfully";
} else {
  echo "Error creating table: " . $conn->error;
}

$conn->close();
?>
