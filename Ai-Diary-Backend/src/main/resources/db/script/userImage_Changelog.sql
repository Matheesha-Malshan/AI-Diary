CREATE TABLE user_image (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                createDate DATE,
                imageUrl VARCHAR(100),
                CONSTRAINT fk_user_image
                    FOREIGN KEY (user_id) REFERENCES user(userId)
);