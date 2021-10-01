CREATE TABLE IF NOT EXISTS folder_sub_folders
(
    sub_folders_id bigint references folder,
    folder_id      bigint references folder
)
