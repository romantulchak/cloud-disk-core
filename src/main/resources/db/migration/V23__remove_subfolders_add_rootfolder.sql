DROP TABLE IF EXISTS folder_sub_folders;

ALTER TABLE folder ADD COLUMN IF NOT EXISTS root_folder uuid;