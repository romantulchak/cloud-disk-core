ALTER TABLE folder
    ADD COLUMN old_name varchar(195);
ALTER TABLE file
    ADD COLUMN old_name varchar(195);